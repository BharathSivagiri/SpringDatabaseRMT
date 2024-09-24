package com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom;

public class DataNotFoundException extends RuntimeException
{
    public DataNotFoundException(String message)
    {
        super(message);
    }
}
