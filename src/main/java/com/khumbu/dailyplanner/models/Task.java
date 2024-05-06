package com.khumbu.dailyplanner.models;

import jakarta.persistence.Id;

import java.time.LocalDate;

public class Task {

    @Id
    private Long id;
    private LocalDate date;
    private int duration;
    private  String name;
    private String comments;
    private int quantity;
    private boolean isDone;
}
