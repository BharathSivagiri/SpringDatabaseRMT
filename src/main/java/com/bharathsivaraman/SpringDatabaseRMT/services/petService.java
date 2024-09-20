package com.bharathsivaraman.SpringDatabaseRMT.services;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;

import java.util.List;
import java.util.Optional;

public interface petService
{
    Pet add(Pet pet); //Add a pet to the database

    List<Pet> getPets(); //Get all pets from the database

    Pet update(Pet pet); //Update a pet in the database

    void delete(Integer id); //Delete a pet from the database

    Optional<Pet> getPetById(Integer id); //Get a pet from the database

}
