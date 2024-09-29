package com.bharathsivaraman.SpringDatabaseRMT.models;

import lombok.Data;

import java.util.List;

@Data
public class PetWithDietModel
{
    private PetModel pet;
    private List<PetDietModel> diets;

    public PetWithDietModel(PetModel pet, List<PetDietModel> diets)
    {
        this.pet = pet;
        this.diets = diets;
    }
}
