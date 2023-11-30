package com.alpha.pharmacyinventorymanagementsystem.controller;

import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineAlreadyExistsException;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.exception.NegativeStockException;

import com.alpha.pharmacyinventorymanagementsystem.service.MedicineService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@RestController
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Test API", version = "1.0", description = "Test Controller"))

public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    @Operation(summary = "Adding medicine to the inventory")
    @PostMapping("/addMedicine")
    public MedicineDto addMedicine(@RequestBody Medicine medicine) throws MedicineAlreadyExistsException {
        log.info("Adding new medicine");
        return medicineService.addMedicine(medicine);
    }

    @Operation(summary = "Retrieving all the medicines in the inventory")
    @GetMapping("/medicines")
    public List<MedicineDto> findAllMedicines() {
        log.info("Fetching all medicine");
        return medicineService.findAllMedicine();
    }

    @Operation(summary = "Retrieving specific medicine in the inventory")
    @GetMapping("/medicine/{id}")
    public MedicineDto findMedicineById(@PathVariable int id) throws MedicineNotFoundException {
        log.info("Fetching specific medicine");
        return medicineService.findMedicine(id);
    }

    @Operation(summary = "Update existing medicine in the inventory")
    @PostMapping("/updateMedicine/{id}")
    public MedicineDto updateMedicine(@PathVariable int id) throws MedicineNotFoundException {
        log.info("Update existing medicine");
        return medicineService.updateMedicine(id);
    }

    @Operation(summary = "Restock medicine in the inventory")
    @PostMapping("/addStock/id={id}&stock={updateStock}")
    public MedicineDto addStock(@PathVariable int id, @PathVariable int updateStock) throws MedicineNotFoundException,
            NegativeStockException {
        log.info("Adding the stock of medicine");
        return medicineService.addStock(id, updateStock);
    }

    @Operation(summary = "Destock medicine in the inventory")
    @PostMapping("/reduceStock/id={id}&stock={updateStock}")
    public MedicineDto reduceStock(@PathVariable int id, @PathVariable int updateStock) throws MedicineNotFoundException,
            NegativeStockException {
        log.info("Reducing the stock of medicine");
        return medicineService.reduceStock(id, updateStock);
    }

    @Operation(summary = "Removing the medicine in the inventory")
    @PostMapping("/deleteMedicine/{id}")
    public StringBuilder deleteMedicine(@PathVariable int id) throws MedicineNotFoundException {
        log.info("Deleting medicine");
        return medicineService.deleteMedicine(id);
    }
}