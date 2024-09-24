package com.bharathsivaraman.SpringDatabaseRMT.exceptions.custom;

import com.bharathsivaraman.SpringDatabaseRMT.utility.constants.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex)
    {
        StringBuilder errorMessages = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessages.append(error.getDefaultMessage()).append("; ")
        );

        ErrorResponse errorResponse = new ErrorResponse() {
            @Override
            public String getMessage() {
                return errorMessages.toString();
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

