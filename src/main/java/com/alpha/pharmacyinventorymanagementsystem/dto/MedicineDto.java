package com.alpha.pharmacyinventorymanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MedicineDto implements Serializable {
    @Schema(example = "101", description = "This is the medicine id.")
    private int medicineId;
    @Schema(example = "Medicine Name", description = "This is the medicine name.")
    private String medicineName;
    @Schema(example = "This is use to treat...", description = "This is the medicine description.")
    private String medicineDescription;
    @Schema(example = "1.00", description = "This is the medicine price.")
    private double medicinePrice;
    @Schema(example = "1", description = "This is the stock of medicine in the inventory.")
    private int medicineStock;
    @Schema(example = "capsule", description = "This is the type of medicine.")
    private String medicineType;
}
