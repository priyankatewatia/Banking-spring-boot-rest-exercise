package com.exercise.bank.exception;

import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LowFundsException.class)
    public ResponseEntity<?> lowFundsException() {
        ErrorInfo errorDetails = new ErrorInfo(new Date(), "Amount Withdrawal cannot be processed due to low funds");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException() {
        ErrorInfo errorDetails = new ErrorInfo(new Date(), "Requested Resource does not exist");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<?> internalServerException(String message) {
        ErrorInfo errorDetails = new ErrorInfo(new Date(), message);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
