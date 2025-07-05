package com.khumbu.dailyplanner.repository;

import com.khumbu.dailyplanner.models.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {

    @Query("SELECT MAX(ID) FROM  Day")
    public Long findMaxId();
    public Day findByDate(LocalDate date);
}
