package com.bharathsivaraman.SpringDatabaseRMT.controller;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import com.bharathsivaraman.SpringDatabaseRMT.services.petService;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/pets")
public class petController
{
    private petService petService;

    @GetMapping("/all") //@GetMapping is used to get a record from the database
    public ResponseEntity<List<Pet>> getPets()
    {
        return new ResponseEntity<>(petService.getPets(), OK);
    }

    @PostMapping("/add") //@PostMapping is used to add a record to the database
    public ResponseEntity<Pet> add(@RequestBody Pet pet) //@RequestBody is used to get the data from the request body
    {
        return new ResponseEntity<>(petService.add(pet), CREATED);
    }

    @PutMapping("/update") //@PutMapping is used to update a record in the database
    public ResponseEntity<Pet> update(@RequestBody Pet pet) //@RequestBody is used to get the data from the request body
    {
        return new ResponseEntity<>(petService.update(pet), CREATED);
    }

    @DeleteMapping("/pet/{id}") //@DeleteMapping is used to delete a record from the database
    public void delete(@PathVariable("id")Integer id) //@PathVariable is used to get the id from the url
    {
        petService.delete(id);
    }

}
