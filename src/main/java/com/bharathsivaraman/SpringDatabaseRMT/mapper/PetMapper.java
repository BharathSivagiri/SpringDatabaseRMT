package com.bharathsivaraman.SpringDatabaseRMT.mapper;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component // This annotation is used to indicate that this class is a Spring component
public class PetMapper
{
    public Pet toEntity(PetModel petModel) // This method is used to convert a PetModel object to a Pet object
    {
        Pet pet = new Pet();

        pet.setName(petModel.getName());
        pet.setType(petModel.getType());
        pet.setOwnerName(petModel.getOwnerName());
        pet.setPrice(Double.valueOf(petModel.getPrice()));
        pet.setBirthDate(LocalDate.parse((petModel.getBirthDate())));
        return pet;
    }

    public PetModel toModel(Pet pet) // This method is used to convert a Pet object to a PetModel object
    {
        PetModel petModel = new PetModel();

        petModel.setName(pet.getName());
        petModel.setType(pet.getType());
        petModel.setOwnerName(pet.getOwnerName());
        petModel.setPrice(String.valueOf(pet.getPrice()));
        petModel.setBirthDate(String.valueOf(pet.getBirthDate()));
        return petModel;
    }
}
