package com.alpha.pharmacyinventorymanagementsystem.dto;

import com.alpha.pharmacyinventorymanagementsystem.constant.TransactionType;
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
public class TransactionDto implements Serializable {
    @Schema(example = "1", description = "This is the transaction id.")
    private int transactionId;
    @Schema(example = "1", description = "This is the medicine id.")
    private int medicineId;
    @Schema(example = "Medicine Name", description = "This is the medicine name.")
    private String medicineName;
    @Schema(example = "1", description = "This is the user id.")
    private int userId;
    @Schema(example = "name", description = "This is the user name.")
    private String userName;
    @Schema(example = "1", description = "This is the quantity.")
    private int quantity;
    @Schema(example = "1.00", description = "This is the medicine price.")
    private double medicinePrice;
    @Schema(example = "1", description = "This is the stock of medicine in the inventory.")
    private int medicineStock;
    @Schema(example = "PURCHASE", description = "This is the transaction type.")
    private TransactionType transactionType;
}
