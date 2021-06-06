package com.exercise.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LowFundsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LowFundsException(){
        super();
    }

}
