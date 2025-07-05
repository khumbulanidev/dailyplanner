package com.khumbu.dailyplanner.exceptions;

import org.springframework.http.HttpStatus;

public class DailyPlannerException extends RuntimeException{
    private HttpStatus status;
    public DailyPlannerException() {
    }

    public DailyPlannerException(String message) {
        super(message);
    }

    public DailyPlannerException(String message, HttpStatus status) {
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