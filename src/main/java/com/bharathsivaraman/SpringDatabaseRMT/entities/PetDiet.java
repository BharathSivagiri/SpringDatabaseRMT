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
    @Column(name = "diet_id", nullable = false)
    private int dietId;

    @Column(name = "diet_name", nullable = false)
    private String dietName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_status", nullable = false)
    private PetDietStatus diet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
