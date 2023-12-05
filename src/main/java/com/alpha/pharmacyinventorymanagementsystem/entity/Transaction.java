package com.alpha.pharmacyinventorymanagementsystem.entity;

import com.alpha.pharmacyinventorymanagementsystem.constant.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "TRANSACTION")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private int transactionId;
    @Column(name = "medicineId")
    private int medicineId;
    @Column(name = "userId")
    private int userId;
    @Column(name = "Quantity")
    private int quantity;
    @Enumerated(EnumType.STRING)
    @Column(name = "transactionType")
    private TransactionType transactionType;
}
