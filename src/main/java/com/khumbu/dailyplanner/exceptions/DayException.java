package com.khumbu.dailyplanner.exceptions;


import org.springframework.http.HttpStatus;

public class DayException extends RuntimeException{
    private HttpStatus status;
    public DayException() {
    }

    public DayException(String message) {
        super(message);
    }

    public DayException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
