package com.alpha.pharmacyinventorymanagementsystem.service;

import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MedicineDTOMapper implements Function<Medicine, MedicineDto> {
    @Override
    public MedicineDto apply(Medicine medicine) {
        return new MedicineDto(medicine.getMedicineId(),
                medicine.getMedicineName(), medicine.getMedicinePrice());
    }
}
