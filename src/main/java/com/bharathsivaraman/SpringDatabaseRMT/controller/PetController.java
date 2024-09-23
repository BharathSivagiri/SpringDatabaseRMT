package com.bharathsivaraman.SpringDatabaseRMT.controller;

import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
import com.bharathsivaraman.SpringDatabaseRMT.services.PetService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //@RestController is used to create a RESTful web service
@RequestMapping("/api/pets") //@RequestMapping is used to map the request to the controller
@RequiredArgsConstructor //@RequiredArgsConstructor is used to create a constructor with required arguments
public class PetController
{
    private final PetService petService;

    @PostMapping("/add") //@PostMapping[POST] is used to add a record to the database
    @ResponseStatus(HttpStatus.CREATED) //@ResponseStatus is used to set the status code of the response
    public PetModel createPet(@RequestBody PetModel petModel)
    {
        return petService.createPet(petModel);
    }

    @GetMapping("/petId/{id}") //@GetMapping[GET] is used to get a record from the database
    public PetModel getPetById(@PathVariable Long id)
    {
        return petService.getPetById(id);
    }

    @GetMapping("/all") //@GetMapping[GET] is used to get all the records from the database
    public List<PetModel> getAllPets()
    {
        return petService.getAllPets();
    }

    @PutMapping("/put/{id}") //@PutMapping[PUT] is used to update a record in the database
    public PetModel updatePet(@PathVariable Long id, @RequestBody PetModel petModel)  //@RequestBody is used to get the data from the request body
    {
        return petService.updatePet(id, petModel);
    }

    @DeleteMapping("/delete/{id}") //@DeleteMapping[DELETE] is used to delete a record from the database
    @ResponseStatus(HttpStatus.NO_CONTENT) //@ResponseStatus is used to set the status code of the response
    public void deletePet(@PathVariable Long id)  //@PathVariable is used to get the id from the url
    {
        petService.deletePet(id);
    }
}
