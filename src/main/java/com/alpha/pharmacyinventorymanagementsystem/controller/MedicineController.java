package com.alpha.pharmacyinventorymanagementsystem.controller;

import com.alpha.pharmacyinventorymanagementsystem.dto.CreateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.StockMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.UpdateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineAlreadyExistsException;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.exception.NegativeStockException;

import com.alpha.pharmacyinventorymanagementsystem.service.InputFormatException;
import com.alpha.pharmacyinventorymanagementsystem.service.MedicineService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            
    })
    @PostMapping("/addMedicine")
    public MedicineDto addMedicine(@RequestBody CreateMedicineDto createMedicineDto) throws MedicineAlreadyExistsException {
        log.info("Adding new medicine");
        return medicineService.addMedicine(createMedicineDto);
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
    public MedicineDto updateMedicine(@PathVariable int id,@RequestBody UpdateMedicineDto updateMedicineDto) throws MedicineNotFoundException {
        log.info("Update existing medicine");
        return medicineService.updateMedicine(id,updateMedicineDto);
    }

    @Operation(summary = "Restock medicine in the inventory")
    @PostMapping("/addStock/id={id}&stock={updateStock}")
    public MedicineDto addStock(@PathVariable int id, @RequestBody StockMedicineDto stockMedicineDto) throws MedicineNotFoundException,
            NegativeStockException {
        log.info("Adding the stock of medicine");
        return medicineService.addStock(id, stockMedicineDto);
    }

    @Operation(summary = "Destock medicine in the inventory")
    @PostMapping("/reduceStock/id={id}")
    public MedicineDto reduceStock(@PathVariable int id, @RequestBody StockMedicineDto stockMedicineDto) throws  InputFormatException,MedicineNotFoundException,
            NegativeStockException {
        int medicineStock;
try{
    medicineStock=Integer.parseInt(String.valueOf(stockMedicineDto.getMedicineStock()));
}catch(NumberFormatException e){
    log.error("Stock must be integer");
    throw new InputFormatException("Stock must be integer");
}
        log.info("Reducing the stock of medicine");
        return medicineService.reduceStock(id, stockMedicineDto);
    }

    @Operation(summary = "Removing the medicine in the inventory")
    @PostMapping("/deleteMedicine/{id}")
    public StringBuilder deleteMedicine(@PathVariable int id) throws MedicineNotFoundException {
        log.info("Deleting medicine");
        return medicineService.deleteMedicine(id);
    }
}