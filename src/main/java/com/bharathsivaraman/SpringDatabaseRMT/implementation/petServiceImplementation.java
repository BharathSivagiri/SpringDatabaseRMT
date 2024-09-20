package com.bharathsivaraman.SpringDatabaseRMT.implementation;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import com.bharathsivaraman.SpringDatabaseRMT.services.petService;
import com.bharathsivaraman.SpringDatabaseRMT.repo.petRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //@Service is a specialization of the @Component annotation.
@AllArgsConstructor //@AllArgsConstructor is a Lombok annotation that generates a constructor implicitly with arguments for all fields in the class.
public class petServiceImplementation implements petService
{

    private petRepository petRepo;

    @Override
    public Pet add(Pet pet) {
        return petRepo.save(pet);//save() method is used to save the pet object to the database
    }

    @Override
    public List<Pet> getPets() {
        return petRepo.findAll();//findAll() method is used to get all the pets from the database
    }

    @Override
    public Pet update(Pet pet) {
        return petRepo.save(pet);//save() method is used to save the pet object to the database
    }

    @Override
    public void delete(Integer id) {
        petRepo.deleteById(id);//deleteById() method is used to delete the pet object from the database
    }

    @Override
    public Optional<Pet> getPetById(Integer id) {
        return Optional.ofNullable(petRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found!")));//findById() method is used to get the pet object from the database
    }
}
