package com.khumbu.dailyplanner.models;

import lombok.Builder;

@Builder
public class TaskMapDto {
    private String email;
    private int month;
    private int year;


    public TaskMapDto() {
    }

    public TaskMapDto(String email, int month, int year) {
        this.email = email;
        this.month = month;
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "TaskMapDto{" +
                "email='" + email + '\'' +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
