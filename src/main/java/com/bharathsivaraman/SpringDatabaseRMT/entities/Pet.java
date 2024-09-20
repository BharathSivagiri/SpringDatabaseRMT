package com.bharathsivaraman.SpringDatabaseRMT.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //(From lombok package)Used to generate getters and setters
@Entity //Used to represent a table in the database
@Table(name = "pets") //Create and represent a table in the database
@NoArgsConstructor //Generates a no-args constructor
public class Pet
{
    @Id //Used to represent a primary key in the database
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Generates a unique id for each record in the database
    private Integer id;
    private String name;
    private String color;

    public Pet(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
