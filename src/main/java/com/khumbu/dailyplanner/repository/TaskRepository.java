package com.khumbu.dailyplanner.repository;

import com.khumbu.dailyplanner.models.Day;
import com.khumbu.dailyplanner.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    //List<Task> getTasksByMonthAndYear(Integer month, int year);
    //@Query(value = "SELECT * FROM task t JOIN Day d  ON t.day_id = d.id WHERE t.email = :email AND d.date LIKE CONCAT('%',:year,:month,'%')", nativeQuery = true)


//    @Query(value = "SELECT t.* FROM task t INNER JOIN Day d  ON t.day_id = d.id WHERE t.user_id = :email AND d.date LIKE CONCAT('%',:monthAndYear,'%')", nativeQuery = true)
//    List<Task> getTasksForChart(@Param("monthAndYear") String monthAndYear, @Param("email") String email);
    @Query(value = "SELECT t.* FROM task t INNER JOIN Day d  ON t.day_id = d.id WHERE t.user_id = :email AND YEAR(d.date) = :year AND MONTH(d.date) = :month", nativeQuery = true)
    List<Task> getTasksForChart(@Param("month") int month, @Param("email") String email, @Param("year") int year);
}
