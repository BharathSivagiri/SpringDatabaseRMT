package com.bharathsivaraman.SpringDatabaseRMT.enums;

import lombok.Getter;

@Getter
public enum PetDietStatus
{
    MORNING("morning"),
    AFTERNOON("afternoon"),
    EVENING("evening"),
    NIGHT("night");

    private final String petDietStatus;

    PetDietStatus(String petDietStatus)
    {
        this.petDietStatus = petDietStatus;
    }

    public static PetDietStatus fromString(String petDietStatus)
    {
        for (PetDietStatus status : PetDietStatus.values())
        {
            if (status.petDietStatus.equalsIgnoreCase(petDietStatus))
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid pet diet status: " + petDietStatus);
    }
}
