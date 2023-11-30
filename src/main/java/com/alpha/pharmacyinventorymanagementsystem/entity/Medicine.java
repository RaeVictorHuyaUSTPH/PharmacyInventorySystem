package com.alpha.pharmacyinventorymanagementsystem.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "MEDICINE")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "medicineId")
    private int medicineId;
    @Column(name = "medicineName")
    private String medicineName;
    @Column(name = "medicineDescription")
    private String medicineDescription;
    @Column(name = "medicinePrice")
    private double medicinePrice;
    @Column(name = "medicineStock")
    private int medicineStock;
}
