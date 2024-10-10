package com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom;

import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
import com.bharathsivaraman.SpringDatabaseRMT.services.implementation.PetServiceImpl;
import com.bharathsivaraman.SpringDatabaseRMT.utility.constants.ErrorResponse;
import com.bharathsivaraman.SpringDatabaseRMT.services.LogService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @Autowired
    private LogService logService;

    @Autowired
    private PetServiceImpl petService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request)
    {
        PetModel petModel = new PetModel();

        petModel.setOwnerName((String) ex.getFieldValue("ownerName"));

        StringBuilder errorMessages = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessages.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(";\n ")
        );

        String finalErrorMessage = errorMessages.toString();
        String apiName = getApiNameFromUri(request.getRequestURI());// Use the request URI as the API name

        logService.logApiCall(
                petModel.getOwnerName(),
                apiName,
                finalErrorMessage,
                ZonedDateTime.now()
        );

        ErrorResponse errorResponse = new ErrorResponse() {
            @Override
            public String getMessage() {
                return finalErrorMessage;
            }

            @Override
            public int getStatus() {
                return HttpStatus.BAD_REQUEST.value();
            }
        };

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BasicValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBasicValidationException(BasicValidationException ex)
    {
        ErrorResponse errorResponse = new ErrorResponse() {
            @Override
            public String getMessage()
            {
                return ex.getMessage();
            }

            @Override
            public int getStatus() {
                return HttpStatus.BAD_REQUEST.value();
            }
        };

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String getApiNameFromUri(String uri)
    {
        switch (uri)
        {
            //API Pet Entity

            case "/api/pets/add":
                return "Pet Creation";
            case "/api//petId/{id}":
                return "Pet by ID";
            case "/api/pets/all":
                return "All Pets";
            case "/api/pets/put/{id}":
                return "Pet Update";
            case "/api/pets/delete/{}":
                return "Pet Deletion";

            //API Pet Diet Entity

            case "/api/pets/diets/add":
                return "Pet Diet Creation";
            case "/api/pets/diets/all":
                return "All Pet Diets";
            case "/api/pets/diets/{dietId}":
                return "Pet Diet by ID";
            case "/api/pets/diets/update/{dietId}":
                return "Pet Diet Update";
            case "/api/pets/diets/delete/{dietId}":
                return "Pet Diet Deletion";
            case "/api/pets/pets-with-diet":
                return "Pet with Pet diet";

            //Pet diet data record status

            case "/api/pets/diets/record-status":
                return "Pet Diet Record Status";
            case "/api/pets/paging":
                return "Paging and Sorting";
            case "/api/search":
                return "Search Pet with Pet Info";
            case "/api/pets/send-email/{id}":
                return "Email Service";


            default:
                return "Unknown API";
        }
    }

}



