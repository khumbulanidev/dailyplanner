package com.khumbu.dailyplanner.controller;


import com.khumbu.dailyplanner.models.DailyTasksDto;
import com.khumbu.dailyplanner.models.TaskDto;
import com.khumbu.dailyplanner.models.TaskMapDto;
import com.khumbu.dailyplanner.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/v1/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;
    @GetMapping("{id}")
    public List<TaskDto> getTasksById(@PathVariable Long id){
        return taskService.getTasksById(id);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id){
       TaskDto task=taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/today/{date}")
    public List<TaskDto> getTasksForToday(@PathVariable String date, @RequestHeader("Authorization") String authHeader){
        LOGGER.info("request {} ", authHeader);
        LOGGER.info("Inside getTasksForToday {}", date);
        return taskService.getTasksByDate(date);
    }

    @GetMapping("/today/{email}/{date}")
    public List<TaskDto> getTasksByEmail(@PathVariable String date, @PathVariable String email, @RequestHeader("Authorization") String authHeader){
        LOGGER.info("request {} ", authHeader);
        LOGGER.info("Inside getTasksByEmail {}", date);
        return taskService.getTasksByDateEmail(date, email);
    }

    @GetMapping("/date/{date}")
    public List<TaskDto> getTasksByDate(@PathVariable String date, HttpServletRequest request){
        LOGGER.info("request {} ", request);
        LOGGER.info("Inside getTasksByDate {}", date);
        return taskService.getTasksByDate(date);
    }

    @GetMapping("/week/{email}/{date}")
    public ResponseEntity<Map<LocalDate, List<TaskDto>>> getTasksForWeekGroupedByDay(@PathVariable String date , @PathVariable String email){
       return  ResponseEntity.ok(taskService.getTaskForWeek(date, email));
    }

    @PostMapping("/save")
    public ResponseEntity<TaskDto> saveTask(@RequestBody TaskDto taskDto){
        LOGGER.info("Inside saveTask");
        return  ResponseEntity.ok(taskService.saveTask(taskDto));

    }

    @PostMapping("/save-all")
    public ResponseEntity<DailyTasksDto> saveAll(@RequestBody DailyTasksDto dailyTasksDto){
        LOGGER.info("Inside saveAll");
        return  ResponseEntity.ok(taskService.saveAll(dailyTasksDto));

    }

    @PutMapping("/update")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto){
        LOGGER.info("Inside updateTask");
        return  ResponseEntity.ok(taskService.updateTask(taskDto));

    }
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable Long taskId){
        return ResponseEntity.ok(this.taskService.deleteTaskById(taskId));
    }

    @DeleteMapping("/delete-tasks")
    public ResponseEntity<List<TaskDto>> deleteTasks(@RequestBody  List<Long> taskIds){
        return ResponseEntity.ok(this.taskService.deleteTasksByIds(taskIds));
    }

    @GetMapping("/month/{month}/{year}")
    public ResponseEntity<List<TaskDto>> getTasksForTheMonth(@PathVariable Long month, @PathVariable Long year){
        return ResponseEntity.ok(taskService.getTasksForTheMonth(month, year));
    }


    @PostMapping("/complete")
    public ResponseEntity<Map<Integer, List<TaskDto>>> getCompleteTasksForTheMonth(@RequestBody TaskMapDto taskMapDto){
        return ResponseEntity.ok(taskService.filterTasksOnCompletion(taskService.getTasksForMonth(taskMapDto.getMonth(), taskMapDto.getYear(), taskMapDto.getEmail()),true));
    }
    @PostMapping("/incomplete")
    public ResponseEntity<Map<Integer, List<TaskDto>>> getIncompleteTasksForTheMonth(@RequestBody TaskMapDto taskMapDto){
        return ResponseEntity.ok(taskService.filterTasksOnCompletion(taskService.getTasksForMonth(taskMapDto.getMonth(), taskMapDto.getYear(), taskMapDto.getEmail()),false));
    }


}
