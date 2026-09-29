package com.example.bioshield.repository;

import com.example.bioshield.model.CalibrationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalibrationLogRepository extends JpaRepository<CalibrationLog, Long> {

    List<CalibrationLog> findByEquipmentId (Long equipmentId);
}
