package com.khumbu.dailyplanner.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    //@JoinColumn points to the column used to map in the Day class
    @ManyToOne()
    @JoinColumn(name = "day_id", nullable=false)
    @JsonIgnore
    private Day day;
    private int duration;
    private String name;
    private String comments;
    private int quantity;
    private boolean isDone;
}
