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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

@Autowired
private ConvertService convertService;
    private static final String NEGATIVESTOCKERROR = "Stock should not be a negative value";
    private static final String MEDICINENOTFOUNDERROR = "Medicine Not Found in the Database";

    public MedicineDto addMedicine(CreateMedicineDto createMedicineDto) throws MedicineAlreadyExistsException {

        if (medicineRepository.existsByMedicineName(createMedicineDto.getMedicineName())) {
            String medicineExistsError = "Medicine already exists in the database";
            log.error(medicineExistsError);
            throw new MedicineAlreadyExistsException(medicineExistsError);
        }
        Medicine medicine=new Medicine();
        medicine.setMedicineId(medicine.getMedicineId());
        medicine.setMedicineName(createMedicineDto.getMedicineName());
        medicine.setMedicineDescription(createMedicineDto.getMedicineDescription());
        medicine.setMedicinePrice(createMedicineDto.getMedicinePrice());
        medicine.setMedicineStock(createMedicineDto.getMedicineStock());
        return  convertService.convertEntityToMedicineDto(medicineRepository.save(medicine));
    }

    public List<MedicineDto> findAllMedicine() {
        List<Medicine> medicines = medicineRepository.findAll();
        return medicines.stream().map(convertService::convertEntityToMedicineDto).toList();

    }

    public MedicineDto findMedicine(Integer id) throws MedicineNotFoundException {

        MedicineDto medicine = medicineRepository.findById(id).map(convertService::convertEntityToMedicineDto).orElse(null);
        if (medicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }

        return medicine;
    }

    public MedicineDto updateMedicine(int id, UpdateMedicineDto updateMedicineDto) throws MedicineNotFoundException {
        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        editMedicine.setMedicineId(editMedicine.getMedicineId());
        editMedicine.setMedicineName(updateMedicineDto.getMedicineName());
        editMedicine.setMedicineDescription(updateMedicineDto.getMedicineDescription());
        editMedicine.setMedicinePrice(updateMedicineDto.getMedicinePrice());
        editMedicine.setMedicineStock(editMedicine.getMedicineStock());
        return convertService.convertEntityToMedicineDto(medicineRepository.save(editMedicine));
    }

    public MedicineDto reduceStock(int id, StockMedicineDto stockMedicineDto) throws MedicineNotFoundException, NegativeStockException, InputFormatException {
        Medicine editMedicine = medicineRepository.findById(id).orElse(null);

        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        int update=editMedicine.getMedicineStock()-stockMedicineDto.getMedicineStock();
        if (update < 0) {
            log.error(NEGATIVESTOCKERROR);
            throw new NegativeStockException(NEGATIVESTOCKERROR);
        }

        editMedicine.setMedicineId(editMedicine.getMedicineId());
        editMedicine.setMedicineName(editMedicine.getMedicineName());
        editMedicine.setMedicineDescription(editMedicine.getMedicineDescription());
        editMedicine.setMedicinePrice(editMedicine.getMedicinePrice());
        editMedicine.setMedicineStock(update);
        return convertService.convertEntityToMedicineDto(medicineRepository.save(editMedicine));
    }
    public MedicineDto addStock(int id, StockMedicineDto stockMedicineDto) throws MedicineNotFoundException, NegativeStockException {

        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
       int update=editMedicine.getMedicineStock()+stockMedicineDto.getMedicineStock();
        if (update < 0) {
            log.error(NEGATIVESTOCKERROR);
            throw new NegativeStockException(NEGATIVESTOCKERROR);
        }
        editMedicine.setMedicineId(editMedicine.getMedicineId());
        editMedicine.setMedicineName(editMedicine.getMedicineName());
        editMedicine.setMedicineDescription(editMedicine.getMedicineDescription());
        editMedicine.setMedicinePrice(editMedicine.getMedicinePrice());
        editMedicine.setMedicineStock(update);
        return convertService.convertEntityToMedicineDto(medicineRepository.save(editMedicine));
    }
    public StringBuilder deleteMedicine(int id) throws MedicineNotFoundException {
        Medicine deleteMedicine = medicineRepository.findById(id).orElse(null);
        if (deleteMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        medicineRepository.deleteById(id);
        return new StringBuilder("This medicine with the medicine id of ")
                .append(id).append(" has been removed from the inventory.");
    }



}