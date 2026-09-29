package com.example.bioshield.service;

import com.example.bioshield.model.Equipment;
import com.example.bioshield.repository.EquipmentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CalibrationSchedulerService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    // Run this method on startup so we can see the changes immediately without waiting for the cron schedule
    @PostConstruct
    public void init() {
        updateComplianceStatuses();
    }

    // Runs every day at midnight server time
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateComplianceStatuses() {
        List<Equipment> allEquipment = equipmentRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Equipment equipment : allEquipment) {
            if (equipment.getCalibrationDueDate() == null) {
                // If there's no due date, we can assume it's out of service or just skip. Let's skip.
                continue;
            }

            long daysUntilDue = ChronoUnit.DAYS.between(today, equipment.getCalibrationDueDate());

            if (daysUntilDue < 0) {
                // Past deadline -> hard-stop red alert
                equipment.setStatus("EXPIRED");
            } else if (daysUntilDue <= 30) {
                // Within 30 days -> amber warning
                equipment.setStatus("WARNING");
            } else {
                // Safe window -> green active
                // Only reset if it was previously WARNING/EXPIRED. Don't overwrite IN_CALIBRATION arbitrarily,
                // but for this capstone we can simplify and set it to ACTIVE.
                if ("WARNING".equals(equipment.getStatus()) || "EXPIRED".equals(equipment.getStatus())) {
                    equipment.setStatus("ACTIVE");
                }
            }
        }
        
        equipmentRepository.saveAll(allEquipment);
    }
}
