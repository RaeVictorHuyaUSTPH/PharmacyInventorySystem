package com.alpha.pharmacyinventorymanagementsystem.service;

import com.alpha.pharmacyinventorymanagementsystem.dto.CreateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.MedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.StockMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.dto.UpdateMedicineDto;
import com.alpha.pharmacyinventorymanagementsystem.entity.Medicine;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertService {
    @Autowired
    private ModelMapper modelMapper;
    public MedicineDto convertEntityToMedicineDto(Medicine medicine){
        MedicineDto medicineDto=modelMapper.map(medicine,MedicineDto.class);
        return medicineDto;
    }
    public CreateMedicineDto convertEntityToCreateMedicineDto(Medicine medicine){
        CreateMedicineDto createMedicineDto=modelMapper.map(medicine, CreateMedicineDto.class);
        return createMedicineDto;
    }
    public StockMedicineDto convertEntityToStockMedicineDto(Medicine medicine){
        StockMedicineDto stockMedicineDto=modelMapper.map(medicine, StockMedicineDto.class);
        return stockMedicineDto;
    }
    public UpdateMedicineDto convertEntityToUpdateMedicineDto(Medicine medicine){
        UpdateMedicineDto updateMedicineDto=modelMapper.map(medicine, UpdateMedicineDto.class);
        return updateMedicineDto;
    }

    public Medicine convertMedicineDtoToEntity(MedicineDto medicineDto){
        Medicine medicine=modelMapper.map(medicineDto,Medicine.class);
        return medicine;
    }
    public Medicine convertCreateMedicineDtoToEntity(CreateMedicineDto createMedicineDto){
        Medicine medicine=modelMapper.map(createMedicineDto,Medicine.class);
        return medicine;
    }
    public Medicine convertStockMedicineDtoToEntity(StockMedicineDto stockMedicineDto){
        Medicine medicine=modelMapper.map(stockMedicineDto,Medicine.class);
        return medicine;
    }
    public Medicine convertUpdateMedicineDtoToEntity(UpdateMedicineDto updateMedicineDto){
        Medicine medicine=modelMapper.map(updateMedicineDto,Medicine.class);
        return medicine;
    }
}
