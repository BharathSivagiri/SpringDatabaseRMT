package com.bharathsivaraman.SpringDatabaseRMT.repo;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface petRepository extends JpaRepository<Pet, Integer>
{

}
