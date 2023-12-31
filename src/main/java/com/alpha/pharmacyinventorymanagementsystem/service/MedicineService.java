package com.alpha.pharmacyinventorymanagementsystem.service;

import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineAlreadyExistsException;
import com.alpha.pharmacyinventorymanagementsystem.exception.MedicineNotFoundException;
import com.alpha.pharmacyinventorymanagementsystem.exception.NegativeStockException;
import com.alpha.pharmacyinventorymanagementsystem.repository.MedicineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineDTOMapper medicineDTOMapper;
    private static final String NEGATIVESTOCKERROR = "Stock should not be a negative value";
    private static final String MEDICINENOTFOUNDERROR = "Medicine Not Found in the Database";

    public Medicine addMedicine(Medicine medicine) throws MedicineAlreadyExistsException {
        if (medicineRepository.existsByMedicineName(medicine.getMedicineName())) {
            String medicineExistsError = "Medicine already exists in the database";
            log.error(medicineExistsError);
            throw new MedicineAlreadyExistsException(medicineExistsError);
        }

        return medicineRepository.save(medicine);
    }

    public List<MedicineDto> findAllMedicine() {
        List<Medicine> medicines = medicineRepository.findAll();
        return medicines.stream().map(medicineDTOMapper).toList();

    }

    public MedicineDto findMedicine(Integer id) throws MedicineNotFoundException {

        MedicineDto medicine = medicineRepository.findById(id).map(medicineDTOMapper).orElse(null);
        if (medicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        return medicine;
    }

    public Medicine updateMedicine(int id) throws MedicineNotFoundException {
        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        editMedicine.setMedicineName(editMedicine.getMedicineName());
        editMedicine.setMedicineDescription(editMedicine.getMedicineDescription());
        editMedicine.setMedicinePrice(editMedicine.getMedicinePrice());

        return medicineRepository.save(editMedicine);
    }

    public Medicine reduceStock(int id, int updateStock) throws MedicineNotFoundException, NegativeStockException {

        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        int update = editMedicine.getMedicineStock() - updateStock;
        if (update < 0) {
            log.error(NEGATIVESTOCKERROR);
            throw new NegativeStockException(NEGATIVESTOCKERROR);
        }
        editMedicine.setMedicineStock(update);
        return medicineRepository.save(editMedicine);
    }

    public Medicine addStock(int id, Integer updateStock) throws MedicineNotFoundException, NegativeStockException {

        Medicine editMedicine = medicineRepository.findById(id).orElse(null);
        if (editMedicine == null) {
            log.error(MEDICINENOTFOUNDERROR);
            throw new MedicineNotFoundException(MEDICINENOTFOUNDERROR);
        }
        int update = editMedicine.getMedicineStock() + updateStock;
        if (update < 0) {
            log.error(NEGATIVESTOCKERROR);
            throw new NegativeStockException(NEGATIVESTOCKERROR);
        }
        editMedicine.setMedicineStock(update);
        return medicineRepository.save(editMedicine);
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
