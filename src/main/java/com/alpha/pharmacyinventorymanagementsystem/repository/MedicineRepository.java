package com.alpha.pharmacyinventorymanagementsystem.repository;

import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    boolean existsByMedicineName(String medicineName);

    List<Medicine> findByMedicineType(String medicineType);
}
