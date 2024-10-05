package com.bharathsivaraman.SpringDatabaseRMT.controller;

import com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom.BasicValidationException;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetDietWithPetInfoModel;
import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
import com.bharathsivaraman.SpringDatabaseRMT.services.PetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @ResponseStatus(HttpStatus.OK)
    public List<PetDietModel> getAllPetDiets()
    {
        return petService.getAllPetDiets();
    }

    @GetMapping("/diets/{dietId}") //GET
    @ResponseStatus(HttpStatus.OK)
    public PetDietModel getPetDietById(@PathVariable Long dietId)
    {
        return petService.getPetDietById(dietId);
    }

    @PutMapping("/diets/update/{dietId}") //PUT
    @ResponseStatus(HttpStatus.OK)
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

    @GetMapping("/pets-with-diet") //GET
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PetDietWithPetInfoModel>> getPetsWithDiet(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
    {
        List<PetDietWithPetInfoModel> petsWithDiet = petService.getPetsWithDiet(id, startDate, endDate);
        return ResponseEntity.ok(petsWithDiet);
    }

    //Pet Diet data record status

    @GetMapping("/diets/record-status") //GET
    public ResponseEntity<List<PetDietModel>> getAllPetDiets(@RequestParam(required = false) String status)
    {
        List<PetDietModel> petDiets = petService.getAllPetDiets(status);
        return ResponseEntity.ok(petDiets);
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<PetModel>> getPetsWithPagingAndSorting(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir,
            @RequestParam(required = false) String startingLetter,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String searchTermOwner)
    {

        pageNo = (pageNo != null) ? pageNo : 0;
        pageSize = (pageSize != null) ? pageSize : 10;
        sortBy = (sortBy != null) ? sortBy : "id";
        sortDir = (sortDir != null) ? sortDir : "asc";

        Page<PetModel> pets = petService.getPetsWithPagingAndSorting(pageNo, pageSize, sortBy, sortDir, startingLetter, searchTerm, searchTermOwner);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PetModel>> searchPets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String ownerName)
    {
        List<PetModel> pets = petService.searchPets(name, type, ownerName);
        return ResponseEntity.ok(pets);
    }
}