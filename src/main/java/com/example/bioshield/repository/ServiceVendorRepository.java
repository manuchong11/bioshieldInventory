package com.example.bioshield.repository;

import com.example.bioshield.model.ServiceVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceVendorRepository extends JpaRepository<ServiceVendor, Long> {
}
