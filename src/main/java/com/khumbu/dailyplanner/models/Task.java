package com.khumbu.dailyplanner.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @Id
    private Long id;

    //@JoinColumn points to the column used to map in the Day class
    @ManyToOne
    @JoinColumn(name = "day_id", nullable=false)
    private Day day;
    private int duration;
    private  String name;
    private String comments;
    private int quantity;
    private boolean isDone;
}
