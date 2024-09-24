package com.bharathsivaraman.SpringDatabaseRMT.mapper;

import com.bharathsivaraman.SpringDatabaseRMT.entities.PetDiet;
import com.bharathsivaraman.SpringDatabaseRMT.enums.PetDietStatus;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;

import com.bharathsivaraman.SpringDatabaseRMT.utility.DateUtils;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class PetDietMapper
{
    public PetDiet toDEntity(PetDietModel petDietModel)
    {
        PetDiet petDiet = new PetDiet();

        petDiet.setDietName(petDietModel.getDietName());
        petDiet.setDescription(petDietModel.getDescription());
        petDiet.setStartDate(DateUtils.convertToDate(petDietModel.getStartDate()));
        petDiet.setEndDate(DateUtils.convertToDate(petDietModel.getEndDate()));
        petDiet.setDiet(PetDietStatus.valueOf(petDietModel.getDiet().toUpperCase()));

        return petDiet;
    }

    public PetDietModel toDModel(PetDiet petDiet)
    {
        PetDietModel petDietModel = new PetDietModel();

        petDietModel.setDietName(petDiet.getDietName());
        petDietModel.setDescription(petDiet.getDescription());
        petDietModel.setStartDate(DateUtils.convertToString(petDiet.getStartDate()));
        petDietModel.setEndDate(DateUtils.convertToString(petDiet.getEndDate()));
        petDietModel.setDiet(petDiet.getDiet().name());

        return petDietModel;
    }
}