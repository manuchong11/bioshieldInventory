package com.example.bioshield.service;

import com.example.bioshield.model.Equipment;
import com.example.bioshield.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    // Fetch all equipment for our dashboard grid display
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    // Securely find a single piece of equipment by its ID
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    // Custom Search Functionality: Find equipment matching an explicit asset tag
    // search
    public Optional<Equipment> getEquipmentByTag(String assetTag) {
        return equipmentRepository.findByAssetTag(assetTag);
    }

    // Business Logic Validation: Ensure We don't save duplicate asset tag
    public Equipment saveEquipment(Equipment equipment) {
        // 1. Manual String Manipulation
        // Trim leading and trailing whitespace from the name
        if (equipment.getName() != null) {
            equipment.setName(equipment.getName().trim());
        }
        if (equipment.getAssetTag() != null) {
            equipment.setAssetTag(equipment.getAssetTag().trim());

            // Regex Validation: Ensure Asset Tag follows a specific format (e.g.,
            // "EQ-1234")
            // this regex means it must start with "EQ-" followed by 4 digits
            if (!equipment.getAssetTag().matches("^EQ-\\d{4}$")) {
                throw new IllegalArgumentException(
                        "Validation Error: Asset tag must be in format 'EQ-XXXX' where X is a number.");
            }
        }

        Optional<Equipment> existing = equipmentRepository.findByAssetTag(equipment.getAssetTag());
        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    "Validation Error: An asset with tag" + equipment.getAssetTag() + " already exists");
        }

        // Enforce a default status if none is provided upon registration
        if (equipment.getStatus() == null || equipment.getStatus().isEmpty()) {
            equipment.setStatus("ACTIVE");
        }
        return equipmentRepository.save(equipment);

    }

    // Secure Delete Functionality
    public void deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Cannot delete: Equipment ID" + id + " not found");
        }
        equipmentRepository.deleteById(id);
    }

    // update equipment
    public Equipment updateEquipment(Long id, Equipment updatedData) {
        // 1. check if equipment exists
        Equipment existingEquipment = equipmentRepository.findById(id).orElseThrow(
                () -> new jakarta.persistence.EntityNotFoundException("Equipment ID " + id + " not found."));

        // 2. update the fields
        existingEquipment.setName(updatedData.getName());
        existingEquipment.setManufacturer(updatedData.getManufacturer());
        existingEquipment.setModel(updatedData.getModel());
        existingEquipment.setSerialNumber(updatedData.getSerialNumber());
        existingEquipment.setLocation(updatedData.getLocation());
        existingEquipment.setStatus(updatedData.getStatus());

        // 3. save and return the updated entity
        return equipmentRepository.save(existingEquipment);

    }

}
