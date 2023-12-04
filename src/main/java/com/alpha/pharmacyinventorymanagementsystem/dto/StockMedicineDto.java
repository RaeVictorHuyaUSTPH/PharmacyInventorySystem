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
public class StockMedicineDto implements Serializable {
    @Schema(example = "1", description = "This is the stock of medicine in the inventory.")
    @NotNull
    private int medicineStock;
}
