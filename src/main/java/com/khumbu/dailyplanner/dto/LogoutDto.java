package com.khumbu.dailyplanner.dto;

public class LogoutDto {

    private String username;
    private String message;

    public LogoutDto(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public LogoutDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LogoutDto{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
