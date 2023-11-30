package com.alpha.pharmacyinventorymanagementsystem.repository;

import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    boolean existsByMedicineName(String medicineName);
}
