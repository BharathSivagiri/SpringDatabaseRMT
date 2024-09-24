//package com.bharathsivaraman.SpringDatabaseRMT.enums;
//
//public enum PetDietStatus
//{
//    MORNING("morning"),
//    AFTERNOON("afternoon"),
//    EVENING("evening"),
//    NIGHT("night");
//
//    private final String petDietStatus;
//
//    PetDietStatus(String petDietStatus)
//    {
//        this.petDietStatus = petDietStatus;
//    }
//
//    public String getPetDietStatus()
//    {
//        return petDietStatus;
//    }
//
//    public static PetDietStatus fromString(String petDietStatus)
//    {
//        for (PetDietStatus status : PetDietStatus.values())
//        {
//            if (status.petDietStatus.equalsIgnoreCase(petDietStatus))
//            {
//                return status;
//            }
//        }
//
//        throw new IllegalArgumentException("Invalid pet diet status: " + petDietStatus);
//    }
//}
