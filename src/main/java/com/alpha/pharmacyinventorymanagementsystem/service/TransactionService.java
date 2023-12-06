package com.alpha.pharmacyinventorymanagementsystem.service;

import com.alpha.pharmacyinventorymanagementsystem.constant.Role;
import com.alpha.pharmacyinventorymanagementsystem.constant.TransactionType;
import com.alpha.pharmacyinventorymanagementsystem.dto.CreateTransactionDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.TransactionDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.TransactionRetrievedDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.UserDTO;
import com.alpha.pharmacyinventorymanagementsystem.entity.Transaction;
import com.alpha.pharmacyinventorymanagementsystem.exception.ElementNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.exception.NegativeStockException;
import com.alpha.pharmacyinventorymanagementsystem.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionService {
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    private static final String MEDICINENOTFOUNDERROR = "Medicine Not Found in the Database.";
    private static final String USERNOTFOUNDERROR = "User Not Found in the Database.";
    private static final String TRANSACTIONNOTFOUNDERROR = "Transaction Not Found in the Database.";

    public TransactionDto pharmacyTransaction(CreateTransactionDto createTransactionDto)
            throws ElementNotFoundException, NegativeStockException {
        MedicineDto medicine = medicineService.findMedicine(createTransactionDto.getMedicineId());
        if (medicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new ElementNotFoundException(MEDICINENOTFOUNDERROR);
        }
        UserDTO user = userService.findUser(createTransactionDto.getUserId());
        if (user == null) {
            log.error(USERNOTFOUNDERROR);
            throw new ElementNotFoundException(USERNOTFOUNDERROR);
        }
        Transaction transaction = new Transaction();
        if (user.getUserRole() == Role.CUSTOMER) {
            medicineService.reduceStock(medicine.getMedicineId(), createTransactionDto);
            transaction.setTransactionType(TransactionType.SALE);
        } else {
            medicineService.addStock(medicine.getMedicineId(), createTransactionDto);
            transaction.setTransactionType(TransactionType.PURCHASE);
        }
        transaction.setTransactionId(transaction.getTransactionId());
        BeanUtils.copyProperties(createTransactionDto, transaction);
        transactionRepository.save(transaction);
        TransactionDto transactionDto = new TransactionDto();
        BeanUtils.copyProperties(transaction, transactionDto);
        BeanUtils.copyProperties(medicine, transactionDto);
        BeanUtils.copyProperties(user, transactionDto);
        return transactionDto;
    }

    public List<TransactionRetrievedDto> findAllTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionRetrievedDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionRetrievedDto transactionRetrievedDto = new TransactionRetrievedDto();
            BeanUtils.copyProperties(transaction, transactionRetrievedDto);
            transactionDtos.add(transactionRetrievedDto);
        }
        log.info("Retrieving all the transactions in the database.");
        return transactionDtos;
    }

    public TransactionRetrievedDto findTransaction(Integer id) throws ElementNotFoundException {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            log.error(TRANSACTIONNOTFOUNDERROR);
            throw new ElementNotFoundException(TRANSACTIONNOTFOUNDERROR);
        }
        TransactionRetrievedDto transactionRetrievedDto = new TransactionRetrievedDto();
        BeanUtils.copyProperties(transaction, transactionRetrievedDto);
        log.info("Retrieving Transaction:{}: data.", transactionRetrievedDto.getUserId());
        return transactionRetrievedDto;
    }
}
