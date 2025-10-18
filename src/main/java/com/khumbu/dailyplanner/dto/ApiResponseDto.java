package com.khumbu.dailyplanner.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseDto<T> {

   private  LocalDateTime time;
   private String message;
   private HttpStatus httpStatus;
   private String responseCode;
   private T data;
   private  Exception exception;

   private String token;

    public ApiResponseDto() {
    }

    public ApiResponseDto(T data, String message, HttpStatus httpStatus) {
        this.data = data;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ApiResponseDto(LocalDateTime time, String message, HttpStatus httpStatus, String responseCode, T data, Exception exception, String token) {
        this.time = time;
        this.message = message;
        this.httpStatus = httpStatus;
        this.responseCode = responseCode;
        this.data = data;
        this.exception = exception;
        this.token = token;
    }

    public ApiResponseDto(LocalDateTime time, String message, HttpStatus httpStatus, String responseCode, T data, Exception exception) {
        this.time = time;
        this.message = message;
        this.httpStatus = httpStatus;
        this.responseCode = responseCode;
        this.data = data;
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



    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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
                ", data=" + data +
                ", exception=" + exception +
                ", token='" + token + '\'' +
                '}';
    }
}
