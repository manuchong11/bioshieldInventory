import { CalibrationLog } from './calibration-log.model';

export interface Equipment {
  equipmentType?: string;
  id?: number;
  assetTag: string;
  name: string;
  manufacturer: string;
  model: string;
  serialNumber: string;
  location: string;
  status: string;           // 'ACTIVE', 'OUT_OF_SERVICE', 'IN_CALIBRATION'
  purchaseDate: string;
  calibrationDueDate?: string; // Optional because legacy data might not have it
  calibrationLogs?: CalibrationLog[];
}
