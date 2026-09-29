package com.example.bioshield.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("THERMAL")
@Data
@EqualsAndHashCode(callSuper = false)
public class ThermalEquipment extends Equipment {

    private double minTemperatureCelsius;
    private double maxTemperatureCelsius;

    @Override
    public String generateComplianceRiskAssesment() {
        return "MEDIUM RISK - Thermal asset requires continuous digital temperature monitoring and annual sensor certification.";
    }
}
