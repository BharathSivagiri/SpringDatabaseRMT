package com.bharathsivaraman.SpringDatabaseRMT.mapper;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import com.bharathsivaraman.SpringDatabaseRMT.entities.PetDiet;
import com.bharathsivaraman.SpringDatabaseRMT.enums.DBRecordStatus;
import com.bharathsivaraman.SpringDatabaseRMT.enums.PetDietStatus;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;
import com.bharathsivaraman.SpringDatabaseRMT.utility.DateUtils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
public class PetDietMapper
{
    public PetDiet toDEntity(PetDietModel petDietModel, Pet pet)
    {
        PetDiet petDiet = new PetDiet();
        petDiet.setPet(pet);
        petDiet.setDietName(petDietModel.getDietName());
        petDiet.setDescription(petDietModel.getDescription());
        petDiet.setStartDate(DateUtils.convertToDate(petDietModel.getStartDate()));
        petDiet.setEndDate(DateUtils.convertToDate(petDietModel.getEndDate()));
        petDiet.setDiet(PetDietStatus.valueOf(petDietModel.getDiet().toUpperCase()));
        petDiet.setCreatedDietDate(DateUtils.convertToDate(petDietModel.getCreatedDietDate()));
        petDiet.setUpdatedDietDate(DateUtils.convertToDate(petDietModel.getUpdatedDietDate()));
        petDiet.setRecStatus(DBRecordStatus.valueOf(petDietModel.getRecStatus().toUpperCase()));
        return petDiet;
    }

    public PetDietModel toDModel(PetDiet petDiet)
    {
        PetDietModel petDietModel = new PetDietModel();
        Pet pet = new Pet();

        petDietModel.setId(String.valueOf(pet.getId()));
        petDietModel.setDietId(String.valueOf(petDiet.getDietId()));
        petDietModel.setDietName(petDiet.getDietName());
        petDietModel.setDescription(petDiet.getDescription());
        petDietModel.setStartDate(DateUtils.convertToString(petDiet.getStartDate()));
        petDietModel.setEndDate(DateUtils.convertToString(petDiet.getEndDate()));
        petDietModel.setDiet(petDiet.getDiet().name());
        petDietModel.setCreatedDietDate(DateUtils.convertToString(petDiet.getCreatedDietDate()));
        petDietModel.setUpdatedDietDate(DateUtils.convertToString(petDiet.getUpdatedDietDate()));
        petDietModel.setRecStatus(petDiet.getRecStatus().name());
        return petDietModel;
    }
}