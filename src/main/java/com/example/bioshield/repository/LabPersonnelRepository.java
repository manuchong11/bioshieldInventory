package com.example.bioshield.repository;

import com.example.bioshield.model.LabPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabPersonnelRepository extends JpaRepository<LabPersonnel, Long> {
}
