package com.bharathsivaraman.SpringDatabaseRMT.services;

import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetWithDietModel;

import java.util.List;

public interface PetService
{
    PetModel createPet(PetModel petModel);

    PetModel getPetById(Long id);

    List<PetModel> getAllPets();

    PetModel updatePet(Long id, PetModel petModel);

    PetModel deletePet(Long id);

    PetDietModel createPetDiet(PetDietModel petDietModel);

    PetDietModel getPetDietById(Long dietId);

    List<PetDietModel> getAllPetDiets();

    PetDietModel updatePetDiet(Long dietId, PetDietModel petDietModel);

    PetDietModel deletePetDiet(Long dietId);

    PetWithDietModel getPetWithDiet(Long petId);

}
