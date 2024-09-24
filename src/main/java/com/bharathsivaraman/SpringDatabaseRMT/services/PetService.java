package com.bharathsivaraman.SpringDatabaseRMT.services;

import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;

import java.util.List;

public interface PetService
{
    PetModel createPet(PetModel petModel);

    PetModel getPetById(Long id);

    List<PetModel> getAllPets();

    PetModel updatePet(Long id, PetModel petModel);

    void deletePet(Long id);

    PetDietModel createPetDiet(Long petId, PetDietModel petDietModel);

    PetDietModel getPetDietById(Long petId, Long dietId);

    PetDietModel updatePetDiet(Long petId, Long dietId, PetDietModel petDietModel);

    void deletePetDiet(Long petId, Long dietId);
}
