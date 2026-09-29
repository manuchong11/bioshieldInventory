import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquipmentDashboard } from './equipment-dashboard.component';

describe('EquipmentDashboard', () => {
  let component: EquipmentDashboard;
  let fixture: ComponentFixture<EquipmentDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EquipmentDashboard],
    }).compileComponents();

    fixture = TestBed.createComponent(EquipmentDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
