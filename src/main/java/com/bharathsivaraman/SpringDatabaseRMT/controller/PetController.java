package com.bharathsivaraman.SpringDatabaseRMT.controller;

import com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom.BasicValidationException;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
import com.bharathsivaraman.SpringDatabaseRMT.services.PetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController
{
    private final PetService petService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody PetModel createPet(@Valid @RequestBody PetModel petModel) throws BasicValidationException
    {
        return petService.createPet(petModel);
    }

    @GetMapping("/petId/{id}")
    public PetModel getPetById(@PathVariable Long id)
    {
        return petService.getPetById(id);
    }

    @GetMapping("/all")
    public List<PetModel> getAllPets()
    {
        return petService.getAllPets();
    }

    @PutMapping("/put/{id}")
    public PetModel updatePet(@PathVariable Long id, @RequestBody PetModel petModel)
    {
        return petService.updatePet(id, petModel);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable Long id)
    {
        petService.deletePet(id);
    }
}