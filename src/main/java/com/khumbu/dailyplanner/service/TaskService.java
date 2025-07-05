package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DayException;
import com.khumbu.dailyplanner.models.Day;
import com.khumbu.dailyplanner.models.Task;
import com.khumbu.dailyplanner.models.TaskDto;
import com.khumbu.dailyplanner.repository.DayRepository;
import com.khumbu.dailyplanner.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private DayRepository dayRepository;

    public List<Task> getTasksById(Long day_id){

       List<Task> tasks= taskRepository.getTasksById(day_id);
        return tasks;

    }


    public TaskDto saveTask(TaskDto taskDto) {
        //convert dto to entity

        Day day = dayRepository.getById(taskDto.getDayId());
        if(day == null)
        {
            throw new DayException(" Day not found " + taskDto.getId());
        }

        Long maxId= taskRepository.findMaxId();
        if(maxId==null){
            maxId=1L;
        }
        Task task= Task.builder().id(maxId+1).day(day).name(taskDto.getName()).isDone(taskDto.isDone()).duration(taskDto.getDuration()).quantity(taskDto.getQuantity()).comments(taskDto.getComments()).build();


       Task savedTask= taskRepository.save(task);

       return TaskDto.builder().dayId(savedTask.getId()).date(savedTask.getDay().getDate()).name(savedTask.getName()).id(savedTask.getId()).isDone(savedTask.isDone()).comments(savedTask.getComments()).duration(savedTask.getDuration()).build();
    }
}
