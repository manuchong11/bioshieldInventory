INSERT INTO service_vendors (vendor_name, contact_name, phone_number, email) VALUES ('Thermo Scientific', 'Alice Smith', '555-0100', 'alice@thermo.example.com');
INSERT INTO service_vendors (vendor_name, contact_name, phone_number, email) VALUES ('Bruker', 'Bob Johnson', '555-0101', 'bob@bruker.example.com');

INSERT INTO lab_personnel (employee_id, first_name, last_name, role, email) VALUES ('EMP-001', 'Jane', 'Doe', 'Medical Technologist', 'jane.doe@apex.example.com');
INSERT INTO lab_personnel (employee_id, first_name, last_name, role, email) VALUES ('EMP-002', 'John', 'Smith', 'Laboratory Manager', 'john.smith@apex.example.com');

INSERT INTO equipment (asset_tag, name, manufacturer, model, serial_number, location, status, purchase_date, calibration_due_date, equipment_type, min_temperature_celsius, max_temperature_celsius, vendor_id) VALUES ('EQ-2025', 'Microbiology Incubator', 'Thermo Scientific', 'Heracell 150i', 'SN-987654', 'Microbiology Lab Room 4', 'ACTIVE', '2026-01-15', '2026-08-05', 'THERMAL', 4.0, 37.0, 1);

INSERT INTO equipment (asset_tag, name, manufacturer, model, serial_number, location, status, purchase_date, calibration_due_date, equipment_type, fluid_channels, vendor_id) VALUES ('EQ-2027', 'Automated Mass Spectrometer', 'Bruker', 'Biotyper', 'SN-112233', 'Immunology Dept', 'IN_CALIBRATION', '2026-03-22', '2026-07-10', 'ANALYZER', 12, 2);

INSERT INTO equipment (asset_tag, name, manufacturer, model, serial_number, location, status, purchase_date, calibration_due_date, equipment_type, min_temperature_celsius, max_temperature_celsius, vendor_id) VALUES ('EQ-2028', 'Centrifuge', 'Eppendorf', '5810R', 'SN-554433', 'Chemistry Lab', 'ACTIVE', '2025-05-10', '2026-12-31', 'THERMAL', -9.0, 40.0, 1);

INSERT INTO calibration_logs (calibration_date, next_due_date, status, notes, equipment_id, personnel_id) VALUES ('2026-06-01', '2026-12-31', 'PASSED', 'Routine maintenance passed.', 3, 1);