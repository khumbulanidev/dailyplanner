package com.khumbu.dailyplanner.models;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.exceptions.DayException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DayDto {

    private Long id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    private List<Task> tasks;

    public static DayDto create(Day day){
        if(day == null){
            throw new DayException("Day cannot be null");
        }
        return DayDto.builder().id(day.getId()).date(day.getDate()).tasks(day.getTasks()).build();
    }

    public static List<DayDto> createList(List<Day> dayList){
        if(dayList == null){
            throw new DailyPlannerException("dayList cannot be null");
        }
        return dayList.stream().map(DayDto::create).toList();
    }

    public Day getDay(){
        Day day = new Day();
        day.setId(id);
        day.setDate(date);
        return day;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


}
