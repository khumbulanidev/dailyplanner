package com.khumbu.dailyplanner.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class DayDto {
    private Long id;
    private LocalDate date;


    public static DayDto create(Day day){
        return DayDto.builder().id(day.getId()).date(day.getDate()).build();
    }

    public static List<DayDto> createList(List<Day> dayList){

       List<DayDto> dayDtoList= dayList.stream().map(day->DayDto.create(day)).toList();
        return dayDtoList;
    }
}
