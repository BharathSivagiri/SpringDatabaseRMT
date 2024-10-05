package com.bharathsivaraman.SpringDatabaseRMT.repo;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //This annotation is used to indicate that this interface is a repository
public interface PetRepository extends JpaRepository<Pet, Long> //Pet is the entity and Long is the primary key type
{
    Optional<Pet> findById(int id);

    //This interface extends JpaRepository, which provides CRUD operations for the Pet entity.
    //The JpaRepository interface provides methods for creating, reading, updating, and deleting entities.
    //The JpaRepository interface also provides methods for finding entities by their primary key.
}
