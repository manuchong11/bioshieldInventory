import { Component, OnInit, NgZone, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Equipment } from '../../models/equipment.model'
import { EquipmentService } from '../../services/equipment';

@Component({
  selector: 'app-equipment-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './equipment-dashboard.component.html',
  styleUrls: ['./equipment-dashboard.css']
})
export class EquipmentDashboardComponent implements OnInit {
  isLoggedIn: boolean = false;
  loginData = { username: "", password: "" };
  loginError: string = "";
  equipmentList: Equipment[] = [];
  filteredEquipment: Equipment[] = [];
  searchTerm: string = '';
  statusFilter: string = '';
  availableStatuses: string[] = [];
  currentDate: Date = new Date();
  newAsset: Equipment = {
    equipmentType: 'THERMAL',
    assetTag: '',
    name: '',
    manufacturer: '',
    model: '',
    serialNumber: '',
    location: '',
    status: 'ACTIVE',
    purchaseDate: new Date().toISOString().split('T')[0],
    calibrationDueDate: ''
  };
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private equipmentService: EquipmentService, private ngZone: NgZone, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadAllEquipment();

    // Fetch dynamic statuses for the dropdown menu
    this.equipmentService.getStatuses().subscribe({
      next: (statuses) => {
        this.availableStatuses = statuses;
        this.cdr.detectChanges();
      },
      error: (err) => console.error("Failed to load statuses", err)
    });
  }

  loadAllEquipment(): void {
    this.equipmentService.getEquipment().subscribe({
      next: (data: Equipment[]) => { // <-- Explicit type added to fix TS7006
        this.equipmentList = data;
        this.applyFilters();
        this.cdr.detectChanges();
      },
      error: (err: any) => { // <-- Explicit type added to fix TS7006
        this.errorMessage = 'Could not establish connection to BioShield backend services.';
        console.error(err);
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters(): void {
    this.filteredEquipment = this.equipmentList.filter(asset => {
      const matchesSearch =
        asset.assetTag.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        asset.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        asset.location.toLowerCase().includes(this.searchTerm.toLowerCase());

      const matchesStatus = this.statusFilter === '' || asset.status === this.statusFilter;
      return matchesSearch && matchesStatus;
    });
  }

  onCreateAsset(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.newAsset.id) {
      // Update Existing
      this.equipmentService.updateEquipment(this.newAsset.id, this.newAsset).subscribe({
        next: (updatedAsset: Equipment) => {
          const index = this.equipmentList.findIndex(e => e.id === updatedAsset.id);
          if (index !== -1) {
            this.equipmentList[index] = updatedAsset;
          }
          this.applyFilters();
          this.resetForm();
          this.showSuccess('Equipment updated successfully.');
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          this.errorMessage = "Failed to update equipment.";
          this.cdr.detectChanges();
        }
      });
    }
    else {
      // Create New
      this.equipmentService.addEquipment(this.newAsset).subscribe({
        next: (savedAsset: Equipment) => {
          this.equipmentList.push(savedAsset);
          this.applyFilters();
          this.resetForm();
          this.showSuccess('Equipment registered successfully.');
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          let msg = err.error;
          if (typeof msg === 'object') msg = msg?.message || JSON.stringify(msg);
          this.errorMessage = msg || "Failed to register equipment.";
          this.cdr.detectChanges();
        }
      });
    }
  }

  private showSuccess(msg: string): void {
    this.successMessage = msg;
    setTimeout(() => {
      this.successMessage = '';
      this.cdr.detectChanges();
    }, 5000);
  }

  private resetForm(): void {
    this.newAsset = {
      assetTag: '',
      name: '',
      manufacturer: '',
      model: '',
      serialNumber: '',
      location: '',
      status: 'ACTIVE',
      purchaseDate: new Date().toISOString().split('T')[0],
      calibrationDueDate: ''
    };
  }

  onDeleteAsset(id: number | undefined): void {
    if (!id) return;

    if (confirm("Are you sure you want to delete this equipment?")) {
      this.equipmentService.deleteEquipment(id).subscribe({
        next: (res) => {
          this.errorMessage = '';
          this.successMessage = res.message;
          this.equipmentList = this.equipmentList.filter(e => e.id !== id);
          this.applyFilters();
          this.cdr.detectChanges();

          setTimeout(() => {
            this.successMessage = '';
            this.cdr.detectChanges();
          }, 5000);
        },
        error: (err) => {
          let msg = err.error || err.message;
          if (typeof msg === 'object') msg = msg?.message || JSON.stringify(msg);
          this.errorMessage = msg || "Failed to delete equipment.";
          console.error(err);
          this.cdr.detectChanges();
        }
      });
    }
  }

  onEditAsset(asset: Equipment): void {
    // For a simple implementation, we populate the newAsset form with this asset's data.
    // When the user clicks save, we will check if it already has an ID to call update.
    this.newAsset = { ...asset };
  }

  onLogin(): void {
    this.loginError = "";
    fetch("https://bioshield-backend-106281319461.us-central1.run.app/api/auth/login", {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(this.loginData)
    })
      .then(res => {
        this.ngZone.run(() => {
          if (res.ok) {
            this.isLoggedIn = true;
          } else {
            this.loginError = "Invalid credentials. Please try admin / admin123";
          }
          this.cdr.detectChanges();
        });
      })
      .catch(err => {
        this.loginError = "Server connection failed.";
        this.cdr.detectChanges();
      });
  }

  // Support Ticket Form Logic
  supportTicket = {
    subject: '',
    description: '',
    urgency: 'Medium'
  };
  ticketSuccessMessage: string = '';
  ticketErrorMessage: string = '';

  onSubmitTicket(): void {
    fetch("https://bioshield-backend-106281319461.us-central1.run.app/api/support/ticket", {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(this.supportTicket)
    })
      .then(res => {
        if (res.ok) {
          this.ticketSuccessMessage = "Ticket successfully routed to IT Support.";
          this.ticketErrorMessage = "";
          this.supportTicket = { subject: '', description: '', urgency: 'Medium' };
          this.cdr.detectChanges();
          setTimeout(() => {
            this.ticketSuccessMessage = '';
            this.cdr.detectChanges();
          }, 5000);
        } else {
          this.ticketErrorMessage = "Failed to submit ticket.";
          this.cdr.detectChanges();
        }
      })
      .catch(err => {
        this.ticketErrorMessage = "Server connection failed.";
        this.cdr.detectChanges();
      });
  }

  trackByAssetId(index: number, asset: any): number {
    return asset.id; // Tells Angular to only re-render the row that actually changed
  }
}
