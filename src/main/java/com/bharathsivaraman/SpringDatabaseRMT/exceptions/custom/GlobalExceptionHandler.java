package com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom;

import com.bharathsivaraman.SpringDatabaseRMT.models.PetModel;
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
        String apiName = request.getRequestURI(); // Use the request URI as the API name

        logService.logApiCall(
                "Name : " + petModel.getOwnerName(),
                "API : " + apiName,
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
    public ResponseEntity<ErrorResponse> handleBasicValidationException(BasicValidationException ex) {
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
}



