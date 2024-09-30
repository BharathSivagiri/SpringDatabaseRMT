package com.bharathsivaraman.SpringDatabaseRMT.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PetDietModel
{
    @JsonIgnore //This annotation is used to ignore the field when serializing the object to JSON
    private String id;

    private String dietId;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Must contain only letters and spaces")
    @Size(min=2,max = 30, message = "Name must be 10-30 characters long")
    private String dietName;

    private String description;

    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "Date must be in the format YYYYMMDD")
    private String startDate;

    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "Date must be in the format YYYYMMDD")
    private String endDate;

    private String diet;

    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "Date must be in the format YYYYMMDD")
    private String createdDietDate;

    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "Date must be in the format YYYYMMDD")
    private String updatedDietDate;

    private String recStatus;
}
