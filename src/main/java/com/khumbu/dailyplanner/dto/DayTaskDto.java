package com.khumbu.dailyplanner.dto;

public class DayTaskDto {

    private int day;
    private int numberOfTasks;

    public DayTaskDto(int day, int numberOfTasks) {
        this.day = day;
        this.numberOfTasks = numberOfTasks;
    }

    public DayTaskDto() {
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getNumberOfTasks() {
        return numberOfTasks;
    }

    public void setNumberOfTasks(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
    }

    @Override
    public String toString() {
        return "DayTaskDto{" +
                "day=" + day +
                ", numberOfTasks=" + numberOfTasks +
                '}';
    }
}
