export interface CalibrationLog {
  id?: number;
  calibrationDate: string;
  technicianName: string;
  status: string;           // 'PASS' or 'FAIL'
  notes: string;
  nextDueDate?: string;
}
