package com.alpha.pharmacyinventorymanagementsystem.controller;

import com.alpha.pharmacyinventorymanagementsystem.dto.CreateTransactionDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.TransactionDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.TransactionRetrievedDto;
import com.alpha.pharmacyinventorymanagementsystem.exception.ElementNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.exception.NegativeStockException;
import com.alpha.pharmacyinventorymanagementsystem.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/transaction")
@Tag(name = "Transaction Management", description = "API for managing the transaction.")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Adding transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, User added to the database",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request, check your input if it follows the required data type",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateTransactionDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found, Medicine/User not found in the database.",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Conflict, Check whether the Medicine Stock will result in negative value.",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database",
                    content = @Content)
    })

    @PostMapping("/addTransaction")
    public ResponseEntity<TransactionDto> addTransaction(@RequestBody CreateTransactionDto createTransactionDto) {
        log.info("Adding new user");
        TransactionDto transactionDto;
        try {
            transactionDto = transactionService.pharmacyTransaction(createTransactionDto);
        } catch (ElementNotFoundException userNotFoundException) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (NegativeStockException negativeStockException) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(transactionDto, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Retrieving all the transaction in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success, Retrieval of all transaction in the database was successful.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionRetrievedDto.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @GetMapping("/transactions")
    public List<TransactionRetrievedDto> findAllTransaction() {
        log.info("Fetching all transaction");
        return transactionService.findAllTransaction();
    }

    @Operation(summary = "Retrieving specific transaction in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, Transaction retrieved successfully.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionRetrievedDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found, Transaction not found in the database.",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionRetrievedDto>
    findTransactionById(@Parameter(description = "Transaction ID that needs to be retrieved.", example = "1")
                        @PathVariable int id) {
        log.info("Fetching specific transaction");
        TransactionRetrievedDto transactionRetrievedDto;
        try {
            transactionRetrievedDto = transactionService.findTransaction(id);
        } catch (ElementNotFoundException userNotFoundException) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transactionRetrievedDto, new HttpHeaders(), HttpStatus.OK);
    }
}
