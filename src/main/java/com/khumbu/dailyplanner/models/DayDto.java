package com.khumbu.dailyplanner.models;

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

    public static DayDto create(Day day){
        if(day == null){
            throw new DayException("Day cannot be null");
        }
        return DayDto.builder().id(day.getId()).date(day.getDate()).build();
    }

    public static List<DayDto> createList(List<Day> dayList){

        List<DayDto> dayDtoList= dayList.stream().map(DayDto::create).toList();
        return dayDtoList;
    }

    public Day getDay(){
        Day day = new Day();
        day.setId(id);
        day.setDate(date);
        return day;
    }
}
