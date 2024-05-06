package com.khumbu.dailyplanner.repository;

import com.khumbu.dailyplanner.models.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    public Day findByDate(LocalDate date);
}
