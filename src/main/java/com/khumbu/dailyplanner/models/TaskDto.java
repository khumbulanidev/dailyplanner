package com.khumbu.dailyplanner.models;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TaskDto {


    private Long id;
    private int duration;
    private  String name;
    private String comments;
    private int quantity;
    private boolean isDone;
    private LocalDate date;
    private Long dayId;

}
