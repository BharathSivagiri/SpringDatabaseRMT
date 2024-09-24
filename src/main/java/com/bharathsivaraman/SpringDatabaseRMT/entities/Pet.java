package com.bharathsivaraman.SpringDatabaseRMT.entities;

import com.bharathsivaraman.SpringDatabaseRMT.enums.PetStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
@Data // This will generate getters and setters for all the fields
@Table(name = "pet") // This will create a table called pet
@NoArgsConstructor // This will generate a no-args constructor
@AllArgsConstructor // This will generate a constructor with all the fields
public class Pet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name_of_pet", length = 50)
    private String name;

    @Column(name = "type_of_pet")
    private String type;

    @Column(name = "name_of_owner")
    private String ownerName;

    @Column(name = "price_of_pet")
    private Double price;

    @Column(name = "birth_date_of_pet")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PetStatus status;

//    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PetDiet> petDiet;

    @Transient
    public Integer age;

    public int getAge()
    {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}