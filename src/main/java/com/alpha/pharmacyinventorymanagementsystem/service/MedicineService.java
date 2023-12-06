package com.alpha.pharmacyinventorymanagementsystem.service;

import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.CreateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.UpdateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.CreateTransactionDto;
import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import com.alpha.pharmacyinventorymanagementsystem.exception.ElementNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineAlreadyExistsException;
import com.alpha.pharmacyinventorymanagementsystem.exception.NegativeStockException;
import com.alpha.pharmacyinventorymanagementsystem.repository.MedicineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    private static final String NEGATIVESTOCKERROR = "Stock should not be a negative value.";
    private static final String MEDICINENOTFOUNDERROR = "Medicine Not Found in the Database.";
    private static final
    String MEDICINETYPENOTFOUNDERROR = "Medicine With Requested Medicine Type was not found in the Database.";

    public MedicineDto addMedicine(CreateMedicineDto createMedicineDto) throws MedicineAlreadyExistsException {
        if (medicineRepository.existsByMedicineName(createMedicineDto.getMedicineName())) {
            String medicineExistsError = "Medicine already exists in the database.";
            log.error(medicineExistsError);
            throw new MedicineAlreadyExistsException(medicineExistsError);
        }
        Medicine medicine = new Medicine();
        medicine.setMedicineId(medicine.getMedicineId());
        BeanUtils.copyProperties(createMedicineDto, medicine);
        medicineRepository.save(medicine);
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(medicine, medicineDto);
        log.info("Adding Medicine:{}:{}: to the inventory.", medicineDto.getMedicineId(),
                medicineDto.getMedicineName());
        return medicineDto;
    }

    public List<MedicineDto> filterMedicineByType(String medicineType) throws ElementNotFoundException {
        List<Medicine> sortMedicine = medicineRepository.findByMedicineType(medicineType);
        List<MedicineDto> sortMedicineDtos = new ArrayList<>();
        if (sortMedicine.isEmpty()) {
            log.error(MEDICINETYPENOTFOUNDERROR);
            throw new ElementNotFoundException(MEDICINETYPENOTFOUNDERROR);
        }
        for (Medicine medicine : sortMedicine) {
            MedicineDto medicineDto = new MedicineDto();
            BeanUtils.copyProperties(medicine, medicineDto);
            sortMedicineDtos.add(medicineDto);
        }
        return sortMedicineDtos;
    }

    public List<MedicineDto> findAllMedicine() {
        List<Medicine> medicines = medicineRepository.findAll();
        List<MedicineDto> medicineDtos = new ArrayList<>();
        for (Medicine medicine : medicines) {
            MedicineDto medicineDto = new MedicineDto();
            BeanUtils.copyProperties(medicine, medicineDto);
            medicineDtos.add(medicineDto);
        }
        log.info("Retrieving all the medicine in the inventory.");
        return medicineDtos;
    }

    public MedicineDto findMedicine(Integer id) throws ElementNotFoundException {
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        if (medicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new ElementNotFoundException(MEDICINENOTFOUNDERROR);
        }
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(medicine, medicineDto);
        log.info("Retrieving Medicine:{}:{}: data.", medicineDto.getMedicineId(), medicineDto.getMedicineName());
        return medicineDto;
    }

    public MedicineDto updateMedicine(int id, UpdateMedicineDto updateMedicineDto) throws ElementNotFoundException {
        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new ElementNotFoundException(MEDICINENOTFOUNDERROR);
        }
        editMedicine.setMedicineId(editMedicine.getMedicineId());
        editMedicine.setMedicineStock(editMedicine.getMedicineStock());
        BeanUtils.copyProperties(updateMedicineDto, editMedicine);
        medicineRepository.save(editMedicine);
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(editMedicine, medicineDto);
        log.info("{} was updated successfully.", medicineDto.getMedicineName());
        return medicineDto;
    }

    public void reduceStock(int id, CreateTransactionDto transactionDto) throws ElementNotFoundException,
            NegativeStockException {
        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new ElementNotFoundException(MEDICINENOTFOUNDERROR);
        }
        int update = editMedicine.getMedicineStock() - transactionDto.getQuantity();
        if (update < 0) {
            log.error(NEGATIVESTOCKERROR);
            throw new NegativeStockException(NEGATIVESTOCKERROR);
        }
        editMedicine.setMedicineStock(update);
        medicineRepository.save(editMedicine);
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(editMedicine, medicineDto);
        log.info("Reducing Stock to {}, was updated successfully. the updated stock is now {}.",
                medicineDto.getMedicineName(), medicineDto.getMedicineStock());
    }

    public void addStock(int id, CreateTransactionDto transactionDto) throws ElementNotFoundException,
            NegativeStockException {
        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new ElementNotFoundException(MEDICINENOTFOUNDERROR);
        }
        int update = editMedicine.getMedicineStock() + transactionDto.getQuantity();
        if (update < 0) {
            log.error(NEGATIVESTOCKERROR);
            throw new NegativeStockException(NEGATIVESTOCKERROR);
        }
        editMedicine.setMedicineStock(update);
        medicineRepository.save(editMedicine);
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(editMedicine, medicineDto);
        log.info("Adding Stock to {}, was updated successfully. the updated stock is now {}.",
                medicineDto.getMedicineName(), medicineDto.getMedicineStock());
    }

    public void deleteMedicine(int id) throws ElementNotFoundException {
        Medicine deleteMedicine = medicineRepository.findById(id).orElse(null);
        if (deleteMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new ElementNotFoundException(MEDICINENOTFOUNDERROR);
        }
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(deleteMedicine, medicineDto);
        log.info("Deleting Medicine id: {}: {}: to the inventory.", medicineDto.getMedicineId(),
                medicineDto.getMedicineName());
        medicineRepository.deleteById(id);
    }
}