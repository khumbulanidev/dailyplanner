package com.khumbu.dailyplanner;


import com.khumbu.dailyplanner.models.*;
import com.khumbu.dailyplanner.repository.DayRepository;
import com.khumbu.dailyplanner.repository.UserRepository;
import com.khumbu.dailyplanner.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private DayRepository dayRepositoryMock;
    @InjectMocks
    TaskService taskService;
    @Test
    void testSaveAll(){
        //initialize
        DailyTasksDto dailyTasksDto = new DailyTasksDto();
        //act
        List tasks = new ArrayList();
        Day day = new Day(1l, null, new ArrayList<Task>());
        Users user = new  Users("test@mail.com", "John", "Doe", "7787787878", "password");
        Task task = new Task(1l,  user,  day, 0, "Test", null, 0 , false, null	, null);

        tasks.add(task);
        dailyTasksDto.setTasks(tasks);
        dailyTasksDto.setStartDate(LocalDate.now());
        dailyTasksDto.setEndDate(LocalDate.of(2026,5,7));
        //mock user repoepository

        when(userRepositoryMock.findByEmail("")).thenReturn(Optional.of(user));
        when(dayRepositoryMock.findByDate(any())).thenReturn(day);
        when(dayRepositoryMock.save(any())).thenReturn(day);


        DailyTasksDto savedDailyTask = taskService.saveAll(dailyTasksDto);

        //assert
        assertEquals(dailyTasksDto, savedDailyTask);

    }


//    public DailyTasksDto saveAll(DailyTasksDto dailyTasksDto) {
//
//        if(dailyTasksDto.getTasks().size() == 0){
//            throw new DailyPlannerException("No tasks to save");
//        }
//
//        List<Task> tasks = new ArrayList<>();
//        List<Task> savedTasks = new ArrayList<>();
//        List<TaskDto> savedDtos = new ArrayList<>();
//        TaskDto firstTask = dailyTasksDto.getTasks().get(0);
//        Optional<Users> optionalUser = userRepository.findByEmail(firstTask.getEmail());
//        if(optionalUser.isPresent())
//        {
//            Users user =  optionalUser.get();
//            LocalDate startDate = dailyTasksDto.getStartDate();
//            LocalDate endDate = dailyTasksDto.getEndDate();
//            startDate.datesUntil(endDate.plusDays(1)).forEach( date->{
//                        Day startDay = dayRepository.findByDate(date);
//                        Day newDay = new Day();
//                        if(startDay == null){
//                            newDay.setDate(date);
//                            startDay = dayRepository.save(newDay);
//                        }
//                        for(TaskDto taskDto : dailyTasksDto.getTasks()){
//                            taskDto.setDate(date);
//                            taskDto.setDayId(startDay.getId());
//                            savedDtos.add(saveTask(taskDto));
//                        }
//                    }
//            );
//            List<TaskDto> savedTaskDtos = constructTaskDtos(savedTasks);
//            DailyTasksDto savedDailyTasksDto = new DailyTasksDto();
//            savedDailyTasksDto.setStartDate(dailyTasksDto.getStartDate());
//            savedDailyTasksDto.setEndDate(dailyTasksDto.getEndDate());
//            savedDailyTasksDto.setTasks(savedDtos);
//
//            return savedDailyTasksDto;
//        }
//        else{
//            throw new DailyPlannerException("User not found in system " + firstTask.getEmail());
//        }
//
//    }
}
