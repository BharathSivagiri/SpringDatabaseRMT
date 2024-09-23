package com.bharathsivaraman.SpringDatabaseRMT.models;

import com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom.BasicValidationException;

import lombok.*;
import java.util.regex.Pattern;

@Data
public class PetModel
{
    private String id;
    private String name;
    private String type;
    private String ownerName;
    private String price;
    private String birthDate;
    private String status;
    private String age;

    public void validate() throws BasicValidationException
    {
        if (!Pattern.matches("^[a-zA-Z\\s]{2,30}$", name))
        {
            throw new BasicValidationException("Name must be 2-30 characters long and contain only letters and spaces");
        }
        if (!Pattern.matches("^(Dog|Cat|Bird|Fish|Reptile)$", type))
        {
            throw new BasicValidationException("Type must be one of: Dog, Cat, Bird, Fish, Reptile");
        }
        if (!Pattern.matches("^[a-zA-Z\\s]{2,50}$", ownerName))
        {
            throw new BasicValidationException("Owner name must be 2-50 characters long and contain only letters and spaces");
        }
        if (!Pattern.matches("^\\d+(\\.\\d{1,2})?$", price))
        {
            throw new BasicValidationException("Price must be a valid number with up to 2 decimal places");
        }
        if (!Pattern.matches("^\\d{4}\\d{2}\\d{2}$", birthDate))
        {
            throw new BasicValidationException("Birth date must be in the format YYYY-MM-DD");
        }
    }
}
