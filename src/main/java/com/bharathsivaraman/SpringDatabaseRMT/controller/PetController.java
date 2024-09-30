package com.bharathsivaraman.SpringDatabaseRMT.controller;

import com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom.BasicValidationException;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietWithPetInfoModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
import com.bharathsivaraman.SpringDatabaseRMT.services.PetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController
{
    private final PetService petService;

    //Pet API

    @PostMapping("/add") //POST
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody PetModel createPet(@Valid @RequestBody PetModel petModel) throws BasicValidationException
    {
        return petService.createPet(petModel);
    }

    @GetMapping("/petId/{id}") //GET
    public PetModel getPetById(@PathVariable Long id)
    {
        return petService.getPetById(id);
    }

    @GetMapping("/all") //GET
    public List<PetModel> getAllPets()
    {
        return petService.getAllPets();
    }

    @PutMapping("/put/{id}") //PUT
    public PetModel updatePet(@PathVariable Long id, @RequestBody PetModel petModel)
    {
        return petService.updatePet(id, petModel);
    }

    @DeleteMapping("/delete/{id}") //DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public PetModel deletePet(@PathVariable Long id)
    {
        return petService.deletePet(id);
    }

    //Pet Diet API

    @PostMapping("/diets/add") //POST
    @ResponseStatus(HttpStatus.CREATED)
    public PetDietModel createPetDiet(@Valid @RequestBody PetDietModel petDietModel) throws BasicValidationException
    {
        return petService.createPetDiet(petDietModel);
    }

    @GetMapping("/diets/all") //GET
    public List<PetDietModel> getAllPetDiets()
    {
        return petService.getAllPetDiets();
    }

    @GetMapping("/diets/{dietId}") //GET
    public PetDietModel getPetDietById(@PathVariable Long dietId)
    {
        return petService.getPetDietById(dietId);
    }

    @PutMapping("/diets/update/{dietId}") //PUT
    public PetDietModel updatePetDiet(@PathVariable Long dietId, @RequestBody PetDietModel petDietModel)
    {
        return petService.updatePetDiet(dietId, petDietModel);
    }

    @DeleteMapping("/diets/delete/{dietId}") //DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public PetDietModel deletePetDiet(@PathVariable Long dietId)
    {
        return petService.deletePetDiet(dietId);
    }

    //Get All Pets and its Diet by Pet ID

    @GetMapping("/with-diet")
    public ResponseEntity<List<PetDietWithPetInfoModel>> getPetsWithDiet(@RequestParam(required = false) Long id) {
        List<PetDietWithPetInfoModel> petsWithDiet = petService.getPetsWithDiet(id);
        return ResponseEntity.ok(petsWithDiet);
    }



}