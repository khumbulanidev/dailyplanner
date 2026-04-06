package com.khumbu.dailyplanner.models;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TaskDto {


    private Long id;
    private int duration;
    private String name;
    private String comments;
    private int quantity;
    private boolean isDone;
    private LocalDate date;
    private Long dayId;
    private String email;
    private LocalTime startTime;
    private LocalTime endTime;

}
