package com.khumbu.dailyplanner.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseDto<T> {

   private  LocalDateTime time;
   private String message;
   private HttpStatus httpStatus;
   private String responseCode;
   private T t;
   private  Exception exception;

   private String token;

    public ApiResponseDto() {
    }

    public ApiResponseDto(T t, String message, HttpStatus httpStatus) {
        this.t = t;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ApiResponseDto(LocalDateTime time, String message, HttpStatus httpStatus, String responseCode, T t, Exception exception, String token) {
        this.time = time;
        this.message = message;
        this.httpStatus = httpStatus;
        this.responseCode = responseCode;
        this.t = t;
        this.exception = exception;
        this.token = token;
    }

    public ApiResponseDto(LocalDateTime time, String message, HttpStatus httpStatus, String responseCode, T t, Exception exception) {
        this.time = time;
        this.message = message;
        this.httpStatus = httpStatus;
        this.responseCode = responseCode;
        this.t = t;
        this.exception = exception;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public T getData() {
        return t;
    }

    public void setData(T t) {
        this.t = t;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "ApiResponseDto{" +
                "time=" + time +
                ", message='" + message + '\'' +
                ", httpStatus=" + httpStatus +
                ", responseCode='" + responseCode + '\'' +
                ", t=" + t +
                ", exception=" + exception +
                ", token='" + token + '\'' +
                '}';
    }
}
