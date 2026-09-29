import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EquipmentDashboardComponent} from './components/equipment-dashboard/equipment-dashboard.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, EquipmentDashboardComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'bioshield-frontend';
}
