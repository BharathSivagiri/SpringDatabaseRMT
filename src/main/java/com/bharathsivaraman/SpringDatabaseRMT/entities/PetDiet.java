package com.bharathsivaraman.SpringDatabaseRMT.entities;

import com.bharathsivaraman.SpringDatabaseRMT.enums.PetDietStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "pet_diets")
@NoArgsConstructor
@AllArgsConstructor
public class PetDiet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "diet_name")
    private String dietName;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_status")
    private PetDietStatus diet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private Pet pet;
}
