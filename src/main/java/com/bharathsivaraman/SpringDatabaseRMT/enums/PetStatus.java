package com.bharathsivaraman.SpringDatabaseRMT.enums;

public enum PetStatus
{
    AVAILABLE("available"),
    SOLD("sold");

    private final String status;

    PetStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public static PetStatus fromString(String status)
    {
        for (PetStatus petStatus : PetStatus.values())
        {
            if (petStatus.status.equalsIgnoreCase(status))
            {
                return petStatus;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }
}
