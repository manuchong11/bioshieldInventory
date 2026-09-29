package com.example.bioshield.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "lab_personnel")
@Data
public class LabPersonnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String role;
    
    private String email;

    @OneToMany(mappedBy = "labPersonnel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("labPersonnel")
    private List<CalibrationLog> calibrationLogs;
}
