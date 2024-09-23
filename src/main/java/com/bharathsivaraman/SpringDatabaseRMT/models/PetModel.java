package com.bharathsivaraman.SpringDatabaseRMT.models;

import lombok.*;

@Data
public class PetModel
{
    private String name;
    private String type;
    private String ownerName;
    private String price;
    private String birthDate;
}
