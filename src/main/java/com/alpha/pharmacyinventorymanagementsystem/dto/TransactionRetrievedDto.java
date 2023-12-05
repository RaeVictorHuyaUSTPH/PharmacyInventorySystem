package com.alpha.pharmacyinventorymanagementsystem.dto;

import com.alpha.pharmacyinventorymanagementsystem.constant.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionRetrievedDto implements Serializable {
    @Schema(example = "1", description = "This is the transaction id.")
    private int transactionId;
    @Schema(example = "1", description = "This is the medicine id.")
    private int medicineId;
    @Schema(example = "1", description = "This is the user id.")
    private int userId;
    @Schema(example = "1", description = "This is the quantity.")
    private int quantity;
    @Schema(example = "PURCHASE", description = "This is the transaction type.")
    private TransactionType transactionType;
}
