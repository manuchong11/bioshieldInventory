package com.example.bioshield.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "calibration_logs")
@Data
public class CalibrationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "calibration_date", nullable = false)
    private LocalDate calibrationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnel_id")
    @JsonIgnoreProperties("calibrationLogs")
    private LabPersonnel labPersonnel;

    @Column(nullable = false)
    private String status;

    @Column(length = 1000)
    private String notes;

    @Column(name = "next_due_date")
    private LocalDate nextDueDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_id", nullable = false)
    @JsonIgnoreProperties("calibrationsLogs")
    private Equipment equipment;

}
