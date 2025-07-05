package com.khumbu.dailyplanner.controller;

import com.khumbu.dailyplanner.models.Task;
import com.khumbu.dailyplanner.models.TaskDto;
import com.khumbu.dailyplanner.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @GetMapping("{id}")
    public List<TaskDto> getTasksById(@PathVariable Long id){
     List<Task> tasks=taskService.getTasksById(id);
     List<TaskDto> taskDtos=tasks.stream().map(
             task->TaskDto.builder().id(task.getId())
                     .date(task.getDay().getDate())
                     .dayId(task.getDay().getId())
                     .name(task.getName())
                     .isDone(task.isDone())
                     .duration(task.getDuration())
                     .quantity(task.getQuantity())
                     .duration(task.getDuration())
                     .build()).toList();
        return taskDtos;
    }

    @PostMapping("/save")
    public TaskDto saveTask(@RequestBody TaskDto taskDto){
        taskService.saveTask(taskDto);
        return null;

    }

}
