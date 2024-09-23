package com.bharathsivaraman.SpringDatabaseRMT.mapper;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import com.bharathsivaraman.SpringDatabaseRMT.enums.PetStatus;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;

import com.bharathsivaraman.SpringDatabaseRMT.utility.DateUtils;
import org.springframework.stereotype.Component;


@Component // This annotation is used to indicate that this class is a Spring component
public class PetMapper {
    public Pet toEntity(PetModel petModel) // This method is used to convert a PetModel object to a Pet object
    {
        Pet pet = new Pet();

        pet.setName(petModel.getName());
        pet.setType(petModel.getType());
        pet.setOwnerName(petModel.getOwnerName());
        pet.setPrice(Double.valueOf(petModel.getPrice()));
        pet.setBirthDate(DateUtils.convertToDate(petModel.getBirthDate()));
        pet.setStatus(PetStatus.valueOf(petModel.getStatus().toUpperCase()));
        return pet;
    }

    public PetModel toModel(Pet pet) // This method is used to convert a Pet object to a PetModel object
    {
        PetModel petModel = new PetModel();

        petModel.setId(String.valueOf(pet.getId()));
        petModel.setName(pet.getName());
        petModel.setType(pet.getType());
        petModel.setOwnerName(pet.getOwnerName());
        petModel.setPrice(String.valueOf(pet.getPrice()));
        petModel.setBirthDate(DateUtils.convertToString(pet.getBirthDate()));
        petModel.setStatus(pet.getStatus().name());
        petModel.setAge(String.valueOf(pet.getAge()));
        return petModel;
    }
}

