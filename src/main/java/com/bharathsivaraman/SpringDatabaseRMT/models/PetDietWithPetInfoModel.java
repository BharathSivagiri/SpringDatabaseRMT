package com.bharathsivaraman.SpringDatabaseRMT.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonPropertyOrder({"id", "name", "diet"})
@Data
public class PetDietWithPetInfoModel
{

    private Long id;
    private String name;
    private PetDietModel diet;

    public PetDietWithPetInfoModel(int id, String name, PetDietModel dModel)
    {
        this.id = (long) id;
        this.name = name;
        this.diet = dModel;
    }
}

