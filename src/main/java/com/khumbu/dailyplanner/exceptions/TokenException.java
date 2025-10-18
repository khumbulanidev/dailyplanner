package com.khumbu.dailyplanner.exceptions;

import org.springframework.http.HttpStatus;

public class TokenException extends RuntimeException{

    private HttpStatus status;

    public TokenException(String message, HttpStatus status) {
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
