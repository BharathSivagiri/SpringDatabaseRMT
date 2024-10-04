package com.bharathsivaraman.SpringDatabaseRMT.services;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietWithPetInfoModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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

    List<PetDietWithPetInfoModel> getPetsWithDiet(Long id, LocalDate startDate, LocalDate endDate);

    List<PetDietModel> getAllPetDiets(String status);

    Page<Pet> getPetsWithPagingAndSorting(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String startingLetter);

}
