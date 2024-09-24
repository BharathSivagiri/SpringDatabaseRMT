package com.bharathsivaraman.SpringDatabaseRMT.models;

import lombok.Data;

@Data
public class PetDietModel
{
    private String id;
    private String dietName;
    private String description;
    private String startDate;
    private String endDate;
    private String diet;
}
