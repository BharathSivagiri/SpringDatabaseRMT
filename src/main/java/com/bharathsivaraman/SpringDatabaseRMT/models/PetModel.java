package com.bharathsivaraman.SpringDatabaseRMT.models;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class PetModel
{
    private String id;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must contain only letters and spaces")
    @Size(min=2,max = 30, message = "Name must be 10-30 characters long")
    private String name;

    @Pattern(regexp = "^(Dog|Cat|Bird|Fish|Reptile)$", message = "Type must be one of the following: Dog, Cat, Bird, Fish, Reptile")
    private String type;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Owner name must contain only letters and spaces")
    @Size(min=2,max = 30, message = "Owner name must be 10-30 characters")
    private String ownerId;
    private String ownerName;

    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Price must be a valid number with up to 2 decimal places")
    private String price;

    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "Birth date must be in the format YYYYMMDD")
    private String birthDate;

    private String status;
    private String age;
}


