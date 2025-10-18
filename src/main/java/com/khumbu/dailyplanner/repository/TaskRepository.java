package com.khumbu.dailyplanner.repository;

import com.khumbu.dailyplanner.models.Day;
import com.khumbu.dailyplanner.models.Task;
import com.khumbu.dailyplanner.models.TaskDto;
import com.khumbu.dailyplanner.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository  extends JpaRepository<Task, Long> {

    @Query("SELECT MAX(id) FROM  Task")
    public Long findMaxId();
    @Query(value = "SELECT * FROM task WHERE day_id = :day_id ", nativeQuery = true)
    List<Task> getTasksByDayId(Long day_id);

    Optional<Task> findByName(String name);

    Optional<Task> findByNameAndDay(String name, Day day);
    Optional<Task> findByNameAndDayAndUserEmail(String name, Day day, String email);
    @Query(value = "SELECT * FROM task WHERE day_id = :day_id AND user_id = :email", nativeQuery = true)
    List<Task> getTasksByDayIdAndEmail(Long day_id, String email);

//    @Query(value = "SELECT * FROM task WHERE day_id = :day_id ", nativeQuery = true)
//    List<TaskDto> findByMonthAndYear(Long month, Long year);
}
