package com.example.bioshield.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "equipment")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "equipment_type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "equipmentType")
@JsonSubTypes({
                @JsonSubTypes.Type(value = AnalyzerEquipment.class, name = "ANALYZER"),
                @JsonSubTypes.Type(value = ThermalEquipment.class, name = "THERMAL")
})
@Data
public abstract class Equipment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "asset_tag", unique = true, nullable = false)
        private String assetTag;

        @Column(nullable = false)
        private String name;

        private String manufacturer;
        private String model;

        @Column(name = "serial_number")
        private String serialNumber;

        private String location;
        private String status;

        @Column(name = "purchase_date")
        private LocalDate purchaseDate;

        @Column(name = "calibration_due_date")
        private LocalDate calibrationDueDate;

        @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonIgnoreProperties("equipment")
        private List<CalibrationLog> calibrationLogs;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "vendor_id")
        @JsonIgnoreProperties("equipmentList")
        private ServiceVendor serviceVendor;

        public abstract String generateComplianceRiskAssesment();

}
