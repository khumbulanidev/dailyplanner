package com.khumbu.dailyplanner.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Day {
    @Id
    private Long id;
    private LocalDate date;


}
