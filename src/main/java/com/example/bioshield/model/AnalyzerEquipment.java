package com.example.bioshield.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("ANALYZER")
@Data
@EqualsAndHashCode(callSuper = false)
public class AnalyzerEquipment extends Equipment {
    private String reagentType;
    private int fluidChannels;

    @Override
    public String generateComplianceRiskAssesment() {
        return "HIGH RISK - Analyzer requires precision fluidic calibration every 180 days due to chemical reagents";
    }
}
