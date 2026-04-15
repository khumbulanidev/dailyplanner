package com.khumbu.dailyplanner.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    // @OneToMany is used to define the property in the task class used to map the mappedBy variable
    @OneToMany(mappedBy = "day")
    private List<Task> tasks;


}
