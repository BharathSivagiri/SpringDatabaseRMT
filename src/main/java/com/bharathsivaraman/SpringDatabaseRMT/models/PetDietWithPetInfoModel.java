package com.bharathsivaraman.SpringDatabaseRMT.models;

import lombok.Data;


@Data
public class PetDietWithPetInfoModel
{
    private PetModel pet;
    private PetDietModel diet;

    public PetDietWithPetInfoModel(PetModel pet, PetDietModel diet)
    {
        this.pet = pet;
        this.diet = diet;
    }
}

