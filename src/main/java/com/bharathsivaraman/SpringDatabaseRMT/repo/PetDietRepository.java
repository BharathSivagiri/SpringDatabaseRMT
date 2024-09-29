package com.bharathsivaraman.SpringDatabaseRMT.repo;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import com.bharathsivaraman.SpringDatabaseRMT.entities.PetDiet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetDietRepository extends JpaRepository<PetDiet, Long>
{
    List<PetDiet> findByPet(Pet pet);


    // This interface extends JpaRepository, which provides CRUD operations for the PetDiet entity.
    // The JpaRepository interface provides methods for creating, reading, updating, and deleting entities.
    // The JpaRepository interface also provides methods for finding entities by their primary key.
}
