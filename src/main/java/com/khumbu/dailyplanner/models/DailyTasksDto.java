package com.khumbu.dailyplanner.models;

import java.time.LocalDate;
import java.util.List;

public class DailyTasksDto {

    private LocalDate startDate;
    private LocalDate endDate;

    private List<TaskDto> tasks;

    public DailyTasksDto() {
    }

    public DailyTasksDto(LocalDate startDate, LocalDate endDate, List<TaskDto> tasks) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = tasks;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }


    @Override
    public String toString() {
        return "DailyTasksDto{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", tasks=" + tasks +
                '}';
    }
}
