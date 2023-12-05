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
public class CreateMedicineDto implements Serializable {
    @Schema(example = "Medicine Name", description = "This is the medicine name.")
    @NotNull
    private String medicineName;
    @Schema(example = "This is use to treat...", description = "This is the medicine description.")
    @NotNull
    private String medicineDescription;
    @Schema(example = "1.00", description = "This is the medicine price.")
    @NotNull
    private double medicinePrice;
    @Schema(example = "1", description = "This is the stock of medicine in the inventory.")
    @NotNull
    private int medicineStock;
    @Schema(example = "capsule", description = "This is the type of medicine.")
    @NotNull
    private String medicineType;
}
