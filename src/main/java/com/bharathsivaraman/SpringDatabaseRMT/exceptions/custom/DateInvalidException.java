package com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom;

public class DateInvalidException extends RuntimeException
{
    public DateInvalidException(String message) {
        super(message);
    }
}
