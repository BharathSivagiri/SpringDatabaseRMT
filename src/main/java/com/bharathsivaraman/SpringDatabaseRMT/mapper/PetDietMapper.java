//package com.bharathsivaraman.SpringDatabaseRMT.mapper;
//
//import com.bharathsivaraman.SpringDatabaseRMT.entities.PetDiet;
//import com.bharathsivaraman.SpringDatabaseRMT.enums.PetDietStatus;
//import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;
//
//import org.springframework.stereotype.Component;
//import java.time.LocalDate;
//
//@Component
//public class PetDietMapper
//{
//    public PetDiet toDEntity(PetDietModel petDietModel)
//    {
//        PetDiet petDiet = new PetDiet();
//
//        petDiet.setDietName(petDietModel.getDietName());
//        petDiet.setDescription(petDietModel.getDescription());
//        petDiet.setStartDate(LocalDate.parse(petDietModel.getStartDate()));
//        petDiet.setEndDate(LocalDate.parse(petDietModel.getEndDate()));
//        petDiet.setDiet(PetDietStatus.valueOf(petDietModel.getDiet().toUpperCase()));
//
//        return petDiet;
//    }
//
//    public PetDietModel toDModel(PetDiet petDiet)
//    {
//        PetDietModel petDietModel = new PetDietModel();
//
//        petDietModel.setDietName(petDiet.getDietName());
//        petDietModel.setDescription(petDiet.getDescription());
//        petDietModel.setStartDate(petDiet.getStartDate().toString());
//        petDietModel.setEndDate(petDiet.getEndDate().toString());
//        petDietModel.setDiet(petDiet.getDiet().name());
//
//        return petDietModel;
//    }
//}