package com.alpha.pharmacyinventorymanagementsystem.service;

import com.alpha.pharmacyinventorymanagementsystem.dto.CreateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.StockMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.UpdateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineAlreadyExistsException;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineNotFoundException;
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

    private static final String NEGATIVESTOCKERROR = "Stock should not be a negative value";
    private static final String MEDICINENOTFOUNDERROR = "Medicine Not Found in the Database";

    public MedicineDto addMedicine(CreateMedicineDto createMedicineDto) throws MedicineAlreadyExistsException {
        if (medicineRepository.existsByMedicineName(createMedicineDto.getMedicineName())) {
            String medicineExistsError = "Medicine already exists in the database";
            log.error(medicineExistsError);
            throw new MedicineAlreadyExistsException(medicineExistsError);
        }
        Medicine medicine = new Medicine();
        BeanUtils.copyProperties(createMedicineDto, medicine);
        medicine.setMedicineId(medicine.getMedicineId());
        medicineRepository.save(medicine);
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(medicine, medicineDto);
        log.info("Adding Medicine:{}:{}: to the inventory.", medicineDto.getMedicineId(),
                medicineDto.getMedicineName());
        return medicineDto;
    }

    public List<MedicineDto> findAllMedicine() {
        List<Medicine> medicines = medicineRepository.findAll();
        List<MedicineDto> medicineDtos = new ArrayList<>();
        for (Medicine medicine : medicines) {
            MedicineDto medicineDto = new MedicineDto();
            BeanUtils.copyProperties(medicine, medicineDto);
            medicineDtos.add(medicineDto);
        }
        log.info("Retrieving all the medicine in the inventory");
        return medicineDtos;
    }

    public MedicineDto findMedicine(Integer id) throws MedicineNotFoundException {
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        MedicineDto medicineDto = new MedicineDto();
        if (medicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        BeanUtils.copyProperties(medicine, medicineDto);
        log.info("Retrieving Medicine:{}:{}: data", medicineDto.getMedicineId(), medicineDto.getMedicineName());
        return medicineDto;
    }

    public MedicineDto updateMedicine(int id, UpdateMedicineDto updateMedicineDto) throws MedicineNotFoundException {
        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        BeanUtils.copyProperties(updateMedicineDto, editMedicine);
        editMedicine.setMedicineId(editMedicine.getMedicineId());
        medicineRepository.save(editMedicine);
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(editMedicine, medicineDto);
        log.info("{} was updated successfully.", medicineDto.getMedicineName());
        return medicineDto;
    }

    public MedicineDto reduceStock(int id, StockMedicineDto stockMedicineDto) throws MedicineNotFoundException,
            NegativeStockException {
        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        int update = editMedicine.getMedicineStock() - stockMedicineDto.getMedicineStock();
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
        return medicineDto;
    }

    public MedicineDto addStock(int id, StockMedicineDto stockMedicineDto) throws MedicineNotFoundException,
            NegativeStockException {
        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        int update = editMedicine.getMedicineStock() + stockMedicineDto.getMedicineStock();
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
        return medicineDto;
    }

    public StringBuilder deleteMedicine(int id) throws MedicineNotFoundException {
        Medicine deleteMedicine = medicineRepository.findById(id).orElse(null);
        if (deleteMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        MedicineDto medicineDto = new MedicineDto();
        BeanUtils.copyProperties(deleteMedicine, medicineDto);
        log.info("Deleting Medicine id: {}: {}: to the inventory.", medicineDto.getMedicineId(),
                medicineDto.getMedicineName());
        medicineRepository.deleteById(id);
        return new StringBuilder("This medicine with the medicine id of ")
                .append(id).append(" has been removed from the inventory successfully.");
    }
}