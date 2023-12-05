package com.alpha.pharmacyinventorymanagementsystem.repository;

import com.alpha.pharmacyinventorymanagementsystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
