package com.alpha.pharmacyinventorymanagementsystem.service;

import com.alpha.pharmacyinventorymanagementsystem.constant.Role;
import com.alpha.pharmacyinventorymanagementsystem.constant.TransactionType;
import com.alpha.pharmacyinventorymanagementsystem.dto.CreateTransactionDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.TransactionDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.TransactionRetrievedDto;
import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import com.alpha.pharmacyinventorymanagementsystem.entity.Transaction;
import com.alpha.pharmacyinventorymanagementsystem.entity.User;
import com.alpha.pharmacyinventorymanagementsystem.exception.ElementNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.exception.NegativeStockException;
import com.alpha.pharmacyinventorymanagementsystem.repository.MedicineRepository;
import com.alpha.pharmacyinventorymanagementsystem.repository.TransactionRepository;
import com.alpha.pharmacyinventorymanagementsystem.repository.UserRepository;
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
    private MedicineRepository medicineRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MedicineService medicineService;
    private static final String MEDICINENOTFOUNDERROR = "Medicine Not Found in the Database";
    private static final String USERNOTFOUNDERROR = "User Not Found in the Database";
    private static final String TRANSACTIONNOTFOUNDERROR = "Transaction Not Found in the Database";

    public TransactionDto pharmacyTransaction(CreateTransactionDto createTransactionDto)
            throws ElementNotFoundException, NegativeStockException {
        Medicine medicine = medicineRepository.findById(createTransactionDto.getMedicineId()).orElse(null);
        if (medicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new ElementNotFoundException(MEDICINENOTFOUNDERROR);
        }
        User user = userRepository.findById(createTransactionDto.getUserId()).orElse(null);
        if (user == null) {
            log.error(USERNOTFOUNDERROR);
            throw new ElementNotFoundException(USERNOTFOUNDERROR);
        }
        Transaction transaction = new Transaction();
        if (user.getUserRole() == Role.CUSTOMER) {
            medicineService.reduceStock(medicine.getMedicineId(), createTransactionDto);
            transaction.setTransactionType(TransactionType.SALE);

        } else if (user.getUserRole() == Role.PHARMACIST) {
            medicineService.addStock(medicine.getMedicineId(), createTransactionDto);
            transaction.setTransactionType(TransactionType.PURCHASE);
        }
        transaction.setTransactionId(transaction.getTransactionId());
        BeanUtils.copyProperties(createTransactionDto, transaction);
        transactionRepository.save(transaction);
        TransactionDto transactionDto = new TransactionDto();
        BeanUtils.copyProperties(createTransactionDto, transactionDto);
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
        log.info("Retrieving all the transactions in the database");
        return transactionDtos;
    }

    public TransactionRetrievedDto findTransaction(Integer id) throws ElementNotFoundException {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        TransactionRetrievedDto transactionRetrievedDto = new TransactionRetrievedDto();
        if (transaction == null) {
            log.error(TRANSACTIONNOTFOUNDERROR);
            throw new ElementNotFoundException(TRANSACTIONNOTFOUNDERROR);
        }
        BeanUtils.copyProperties(transaction, transactionRetrievedDto);
        log.info("Retrieving Transaction:{}: data", transactionRetrievedDto.getUserId());
        return transactionRetrievedDto;
    }

}
