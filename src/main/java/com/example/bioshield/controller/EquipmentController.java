package com.example.bioshield.controller;

import com.example.bioshield.model.Equipment;
import com.example.bioshield.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin(origins = {"http://localhost:4200", "https://bioshield-capstone.web.app", "https://bioshield-capstone.firebaseapp.com"})
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    // 1. GET ALL: Fetches all rows to populate multiple row front end displays
    @GetMapping
    public List<Equipment> getAllEquipments() {
        return equipmentService.getAllEquipments();
    }

    // 2. GET BY ID: Securely finds a single piece of equipment
    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable long id) {
        return equipmentService.getEquipmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. GET BY TAG (SEARCH): Multi row layout target search
    @GetMapping("/search")
    public ResponseEntity<Equipment> getEquipmentByTag(@RequestParam String assetTag) {
        return equipmentService.getEquipmentByTag(assetTag)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. POST (ADD): Securely inserts new equipment with server validation mapping
    @PostMapping
    public ResponseEntity<?> createEquipment(@RequestBody Equipment equipment) {
        try {
            Equipment saved = equipmentService.saveEquipment(equipment);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Returns a clean 400 Bad Request error if validation parameters fail
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5. DELETE: Securely removes a row record from the system
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipmentById(@PathVariable Long id) {
        try {
            equipmentService.deleteEquipment(id);
            return ResponseEntity.ok()
                    .body((java.util.Map.of("message", "Equipment wit ID " + id + " was successfully deleted.")));

        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 6. PUT(UPDATE): Securely modifies an existing piece of equipment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEquipment(@PathVariable Long id, @RequestBody Equipment equipment) {
        try {
            Equipment updated = equipmentService.updateEquipment(id, equipment);
            return ResponseEntity.ok(updated);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    // 7. GET STATUSES: Returns a list of valid statuses for dynamic dropdowns
    @GetMapping("/statuses")
    public List<String> getAvailableStatuses() {
        return List.of("ACTIVE", "WARNING", "EXPIRED", "IN_CALIBRATION", "OUT_OF_SERVICE");
    }
}
