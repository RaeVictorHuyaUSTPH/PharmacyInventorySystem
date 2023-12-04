package com.alpha.pharmacyinventorymanagementsystem.controller;

import com.alpha.pharmacyinventorymanagementsystem.dto.CreateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.StockMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.UpdateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineAlreadyExistsException;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.exception.NegativeStockException;

import com.alpha.pharmacyinventorymanagementsystem.service.MedicineService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/pharmacy")
@OpenAPIDefinition(info = @Info(title = "Pharmacy Inventory API", version = "1.0",
        description = "REST API for managing the inventory of Pharmacy."))
@Tag(name = "Medicine Inventory", description = "API for managing the inventory of medicines.")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    @Operation(summary = "Adding medicine to the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success,Medicine added to the inventory",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request, check your input",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateMedicineDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "Conflict, Check whether your medicine name is already existed in the database",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database",
                    content = @Content)
    })

    @PostMapping("/addMedicine")
    public ResponseEntity<MedicineDto> addMedicine(@RequestBody CreateMedicineDto createMedicineDto) {
        log.info("Adding new medicine");
        MedicineDto medicineDto;
        try {
            medicineDto = medicineService.addMedicine(createMedicineDto);
        } catch (MedicineAlreadyExistsException mae) {

            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(medicineDto, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Retrieving all the medicines in the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success,Retrieval of all medicine in the inventory was successful.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @GetMapping("/medicines")
    public List<MedicineDto> findAllMedicines() {
        log.info("Fetching all medicine");
        return medicineService.findAllMedicine();
    }

    @Operation(summary = "Retrieving specific medicine in the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine updated successfully.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "404", description = "Medicine not found in the inventory.",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @GetMapping("/medicine/{id}")
    public ResponseEntity<MedicineDto>
    findMedicineById(@Parameter(description = "Medicine ID that needs to be retrieved.") @PathVariable int id) {
        log.info("Fetching specific medicine");
        MedicineDto medicineDto;
        try {
            medicineDto = medicineService.findMedicine(id);
        } catch (MedicineNotFoundException mnfe) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(medicineDto, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Update existing medicine in the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine Stock updated successfully.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request, check your input",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateMedicineDto.class))),
            @ApiResponse(responseCode = "404", description = "Medicine not found in the inventory.",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @PutMapping("/updateMedicine/{id}")
    public ResponseEntity<MedicineDto> updateMedicine(@Parameter(description = "Medicine ID that needs to be updated.")
                                                      @PathVariable int id,
                                                      @RequestBody UpdateMedicineDto updateMedicineDto) {
        log.info("Update existing medicine");
        MedicineDto medicineDto;
        try {
            medicineDto = medicineService.updateMedicine(id, updateMedicineDto);
        } catch (MedicineNotFoundException mnfe) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(medicineDto, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Restock medicine in the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine Stock updated successfully.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request, check your input",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StockMedicineDto.class))),
            @ApiResponse(responseCode = "404", description = "Medicine not found in the inventory.",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Conflict, Check whether your medicine stock will result in negative value",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @PutMapping("/addStock/id={id}")
    public ResponseEntity<MedicineDto> addStock(@Parameter(description = "Medicine ID that needs stock to be updated.")
                                                @PathVariable int id,
                                                @RequestBody StockMedicineDto stockMedicineDto) {
        log.info("Adding the stock of medicine");
        MedicineDto medicineDto;
        try {
            medicineDto = medicineService.addStock(id, stockMedicineDto);
        } catch (MedicineNotFoundException mnfe) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (NegativeStockException nse) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(medicineDto, new HttpHeaders(), HttpStatus.OK);

    }

    @Operation(summary = "Destock medicine in the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine Stock updated successfully.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request, check your input",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StockMedicineDto.class))),
            @ApiResponse(responseCode = "404", description = "Medicine not found in the inventory.",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Conflict, Check whether your medicine stock will result in negative value",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @PutMapping("/reduceStock/id={id}")
    public ResponseEntity<MedicineDto>
    reduceStock(@Parameter(description = "Medicine ID that needs stock to be updated.") @PathVariable int id,
                @RequestBody StockMedicineDto stockMedicineDto) {
        log.info("Reducing the stock of medicine");
        MedicineDto medicineDto;
        try {
            medicineDto = medicineService.reduceStock(id, stockMedicineDto);
        } catch (MedicineNotFoundException mnfe) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (NegativeStockException nse) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(medicineDto, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Removing the medicine in the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removing this medicine to the inventory.",
                    content = {@Content}),

            @ApiResponse(responseCode = "404", description = "Medicine not found in the inventory.",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Database Error, There is something wrong with the Database.",
                    content = @Content)
    })
    @DeleteMapping("/deleteMedicine/{id}")
    public ResponseEntity<StringBuilder>
    deleteMedicine(@Parameter(description = "Medicine ID that needs to be deleted.") @PathVariable int id) {
        log.info("Deleting medicine");
        try {
            medicineService.deleteMedicine(id);
        } catch (MedicineNotFoundException mnfe) {
            return new ResponseEntity<>(new StringBuilder("Failed to delete Medicine ")
                    .append(id).append(" to the inventory because ").append(id)
                    .append(" is not found in the database."), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new StringBuilder("Successfully deleted Medicine ").append(id).
                append(" to the inventory."), new HttpHeaders(), HttpStatus.OK);
    }
}