package com.exercise.bank.exception;

import java.util.Date;

public class ErrorInfo {
    private Date timestamp;
    private String message;

    public ErrorInfo(Date timestamp, String message) {
        super();
        this.timestamp = timestamp;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
