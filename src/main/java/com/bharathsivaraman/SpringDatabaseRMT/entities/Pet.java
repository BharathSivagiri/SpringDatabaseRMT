package com.bharathsivaraman.SpringDatabaseRMT.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity // This tells Hibernate to make a table out of this class
@Data // This will generate getters and setters for all the fields
@Table(name = "pet") // This will create a table called pet
@NoArgsConstructor // This will generate a no-args constructor
@AllArgsConstructor // This will generate a constructor with all the fields
public class Pet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This will generate a primary key for the table
    @Column(name = "ID") // This will create a column called ID
    private Long id;

    @Column(name = "name_of_pet")
    private String name;

    @Column(name = "type_of_pet")
    private String type;

    @Column(name = "name_of_owner")
    private String ownerName;

    @Column(name = "price_of_pet")
    private Double price;

    @Column(name = "birth_date_of_pet")
    private LocalDate birthDate;
}