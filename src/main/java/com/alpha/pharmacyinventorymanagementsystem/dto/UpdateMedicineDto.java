package com.alpha.pharmacyinventorymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateMedicineDto implements Serializable {
    private String medicineName;
    private String medicineDescription;
    private double medicinePrice;
}
