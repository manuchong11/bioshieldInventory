package com.example.bioshield.controller;

import com.example.bioshield.model.CalibrationLog;
import com.example.bioshield.service.CalibrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calibrations")
@CrossOrigin(origins = {"http://localhost:4200", "https://bioshield-capstone.web.app", "https://bioshield-capstone.firebaseapp.com"})
public class CalibrationController {

    @Autowired
    private CalibrationService calibrationService;

    // 1. GET ALL LOGS: Used for generating facility wide compliance reports
    @GetMapping
    public List<CalibrationLog> getAllLogs() {
        return calibrationService.getAllLogs();
    }

    // 2. GET BY EQUIPMENT: Fetches the entire calibration history grid for one specific asset
    @GetMapping("/equipment/{equipmentId}")
    public List<CalibrationLog> getLogsByEquipment(@PathVariable Long equipmentId) {
        return calibrationService.getLogsForEquipment(equipmentId);
    }

    // 3. POST (ADD LOG): Securely saves a log entry and triggers automated 180 day timeline logic
    @PostMapping("/equipment/{equipmentId}")
    public ResponseEntity<?> addCalibrationLog(@PathVariable Long equipmentId, @RequestBody CalibrationLog log) {
        try {
            CalibrationLog savedLog = calibrationService.addCalibrationLog(equipmentId, log);
            return new ResponseEntity<>(savedLog, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Catches validation failures (e.g., missing status, non-existent equipment ID)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
