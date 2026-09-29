package com.example.bioshield.service;

import com.example.bioshield.model.CalibrationLog;
import com.example.bioshield.model.Equipment;
import com.example.bioshield.repository.CalibrationLogRepository;
import com.example.bioshield.repository.EquipmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalibrationService {

    @Autowired
    private CalibrationLogRepository calibrationLogRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    // fetch historical log rows for a specific asset to support multi-row reporting displays
    public List<CalibrationLog> getLogsForEquipment(Long equipmentId) {
        return calibrationLogRepository.findByEquipmentId(equipmentId);
    }

    // fetch all logs across the entire facility for broad reporting metrics
    public List<CalibrationLog> getAllLogs() {
        return calibrationLogRepository.findAll();
    }

    // Core Logi
    @Transactional
    public CalibrationLog addCalibrationLog(Long equipmentId, CalibrationLog log) {
        // 1. Validation: Make sure the target equipment actually exists in our facility
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow(() -> new IllegalArgumentException("Validation Error : Equipment with ID " + equipmentId + " does not exist."));

        // 2. Encapsulation Linkage: Securely attach the parent equipment to this log entry
        log.setEquipment(equipment);

        // 3. Validation: Ensure the technician did not enter a blank status
        if (log.getStatus() == null || log.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Validation Error: Calibration status must be explicitly specified (PASS or FAIL).");
        }

        // 4. Programmatic Business Logic: Automate the 180-day compliance window calculation
        log.setNextDueDate(log.getCalibrationDate().plusDays(180));

        // 5. Cascade State Business Logic: Mutate parent equipment status dynamically based on performance outcomes
        if ("FAIL".equalsIgnoreCase(log.getStatus())) {
            equipment.setStatus("OUT_OF_SERVICE");
        } else if ("PASS".equalsIgnoreCase(log.getStatus())) {
            equipment.setStatus("ACTIVE");
        }

        // 6. Persist changes securely to our database layers
        equipmentRepository.save(equipment);
        return calibrationLogRepository.save(log);

        }


}
