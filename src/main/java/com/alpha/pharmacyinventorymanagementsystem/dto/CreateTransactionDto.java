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
public class CreateTransactionDto implements Serializable {
    @Schema(example = "1", description = "This is the medicine id.")
    @NotNull
    private int medicineId;
    @Schema(example = "1", description = "This is the user id.")
    @NotNull
    private int userId;
    @Schema(example = "1", description = "This is the quantity.")
    @NotNull
    private int quantity;
}
