package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.models.*;
import com.khumbu.dailyplanner.repository.DayRepository;
import com.khumbu.dailyplanner.repository.TaskRepository;
import com.khumbu.dailyplanner.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.khumbu.dailyplanner.constants.DailyPlannerConstants.*;

@Service
@Slf4j
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);


    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private DayService dayService;

    @Autowired
    private UserRepository userRepository;

    public List<TaskDto> getTasksById(Long day_id){
        LOGGER.info("Inside getTasksById");

       List<Task> tasks = taskRepository.getTasksByDayId(day_id);
        List<TaskDto> taskDtos = tasks.stream().map(
                task->TaskDto.builder().id(task.getId())
                        .date(task.getDay().getDate())
                        .dayId(task.getDay().getId())
                        .name(task.getName())
                        .isDone(task.isDone())
                        .duration(task.getDuration())
                        .quantity(task.getQuantity())
                        .comments(task.getComments())
                        .startTime(task.getStartTime() == null ? UNSET_TIME: task.getStartTime().toString())
                        .endTime(task.getEndTime() == null ? UNSET_TIME : task.getEndTime().toString())
                        .build()).toList();
        LOGGER.info("End getTasksById");
        return taskDtos;

    }


    public TaskDto saveTask(TaskDto taskDto) {
        LOGGER.info("Inside saveTask");
        Day day = dayRepository.findByDate(taskDto.getDate());
        Optional<Task> existingTask = taskRepository.findByNameAndDayAndUserEmail(taskDto.getName(),day, taskDto.getEmail()) ;

        if(existingTask.isPresent() && existingTask.get().getDay().getDate().equals(taskDto.getDate()) ){
         throw    new DailyPlannerException("Task with name "+ taskDto.getName() +" already exists for this date.");
        }

        if(day == null){
            day = new Day();
            day.setDate(taskDto.getDate());
            Long nextId = dayRepository.findMaxId() + 1;
            day.setId(nextId);
            dayRepository.save(day);
        }

        Long maxId = taskRepository.findMaxId();
        if(maxId == null){
            maxId=1L;
        }
        Users user = userRepository.findById(taskDto.getEmail()).orElseThrow(()-> new DailyPlannerException("User with email "+ taskDto.getEmail() + " not found in the system."));
        validateTime(taskDto);
        Task task= Task.builder().id(maxId + 1).day(day).name(taskDto.getName()).isDone(taskDto.isDone()).duration(taskDto.getDuration()).quantity(taskDto.getQuantity()).comments(taskDto.getComments()).user(user).startTime(constructTime(taskDto.getStartTime())).endTime(constructTime(taskDto.getEndTime())).build();


       Task savedTask = taskRepository.save(task);
        LOGGER.info("End saveTask");
       return TaskDto.builder().dayId(savedTask.getId()).date(savedTask.getDay().getDate()).name(savedTask.getName()).id(savedTask.getId()).isDone(savedTask.isDone()).comments(savedTask.getComments()).duration(savedTask.getDuration()).startTime(savedTask.getStartTime() == null ? "--:--" : savedTask.getStartTime().toString()).endTime(savedTask.getEndTime() == null ? "--:--" : savedTask.getEndTime().toString()).build();
    }

    private LocalTime constructTime(String time){

        if(time == null || time.equals("0") || time.equals(UNSET_TIME)){
            return null;
        }else{
            return LocalTime.parse(time);
        }
    }

    /***
     * Checks if time is valid
     * Checks for time overlaps
     * Exception thrown if time is not valid
     * @param taskDto
     */
    private void validateTime(TaskDto taskDto){
        if( !taskDto.getStartTime().equals("0") && taskDto.getStartTime().equals(taskDto.getEndTime())){
            throw new DailyPlannerException(SAME_START_END_TIME);
        }
        if(!taskDto.getStartTime().equals("0") && taskDto.getStartTime() != null && taskDto.getEndTime() != null){
            LocalTime sTime = LocalTime.parse(taskDto.getStartTime());
            LocalTime eTime = LocalTime.parse(taskDto.getEndTime());
            if(eTime.isBefore(sTime)){
                throw new DailyPlannerException(START_TIME_AFTER_END_TIME);
            }
            validateTimeOverlap(sTime, eTime, taskDto);
        }
        if(taskDto.getStartTime() != null && taskDto.getEndTime() == null || (taskDto.getStartTime() == null && taskDto.getEndTime() != null)){
            throw new DailyPlannerException(BOTH_START_END_TIME_REQUIRED);
        }
    }

    /***
     *
     * @param sTime Start time
     * @param eTime End time
     * @param taskDto
     */
    private void validateTimeOverlap(LocalTime sTime, LocalTime eTime, TaskDto taskDto){
        Day day = dayRepository.findByDate(taskDto.getDate());
        List<Task> userTasksForToday = taskRepository.getTasksByDayIdAndEmail(day.getId(), taskDto.getEmail());
        List<Task> overlappingTasks = userTasksForToday.stream().filter(a->{
            if(a.getStartTime() == null || a.getEndTime() == null){
                return false;
            }
            if( (sTime.isAfter(a.getStartTime()) && sTime.isBefore(a.getEndTime())) || (eTime.isAfter(a.getStartTime()) && eTime.isBefore(a.getEndTime()))){
                return true;
            }
            return  false;
        }).toList();

        if(overlappingTasks.size() > 0){
            throw new DailyPlannerException(TIME_OVERLAPS);
        }
    }

    public List<TaskDto> getTasksForToday() {
        LOGGER.info("Inside getTasksForToday");

        Day today = dayRepository.findByDate(LocalDate.now());
        List<TaskDto> taskDtos = new ArrayList<>();

        if(today == null){
            return taskDtos;
        }
        LOGGER.info("End getTasksForToday");
        return getTasksById(today.getId());
    }

    public List<TaskDto> getTasksByDate(String date) {
        LOGGER.info("Inside getTasksByDate");

        Day today = dayRepository.findByDate(formatDate(date));
        List<TaskDto> taskDtos = new ArrayList<>();

        if(today == null){
            return taskDtos;
        }
        LOGGER.info("End getTasksForToday");
        return getTasksById(today.getId());

    }

    private LocalDate formatDate(String date){
        //validate date before formating
        String dateArray[] = date.split("-");

        if(dateArray.length != 3){
            LOGGER.error("Invalid date {}",date);
            throw  new DailyPlannerException("Invalid date "+ date);
        }

        return LocalDate.of(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
    }

    public TaskDto deleteTaskById(Long taskId) {

        Task task = this.taskRepository.findById(taskId).orElseThrow(()-> new DailyPlannerException("Task with ID " + taskId +" not found."));

        this.taskRepository.deleteById(taskId);

        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .isDone(task.isDone())
                .quantity(task.getQuantity())
                .date(task.getDay().getDate())
                .duration(task.getDuration())
                .comments(task.getComments())
                .startTime(task.getStartTime() == null ? UNSET_TIME : task.getStartTime().toString())
                .endTime(task.getEndTime() == null ? UNSET_TIME : task.getEndTime().toString())
                .build();
    }

    public TaskDto getTaskById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(()->  new DailyPlannerException("Task with id "+ id + " was not found."));

        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .isDone(task.isDone())
                .startTime(task.getStartTime().toString())
                .endTime(task.getEndTime().toString())
                .quantity(task.getQuantity())
                .date(task.getDay().getDate())
                .duration(task.getDuration())
                .comments(task.getComments())
                .startTime(task.getStartTime() == null ? UNSET_TIME : task.getStartTime().toString())
                .endTime(task.getEndTime() == null ? UNSET_TIME : task.getEndTime().toString())
                .build();
    }

    public TaskDto updateTask(TaskDto taskDto) {
        //check if task exists
        Task task = taskRepository.findById(taskDto.getId()).orElseThrow(
          ()-> new DailyPlannerException("Task with id "+ taskDto.getId() + " not found.")
        );
      Day day = dayRepository.findByDate(taskDto.getDate());


       if(day == null){
           day = new Day();
           day.setDate(taskDto.getDate());
          DayDto savedDay = dayService.save(DayDto.create(day));
          task.setDay(savedDay.getDay());

       }

        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setDone(taskDto.isDone());
        task.setComments(taskDto.getComments());
        task.setDuration(taskDto.getDuration());
        task.setStartTime(constructTime(taskDto.getStartTime()));
        task.setEndTime(constructTime(taskDto.getEndTime()));


        Task savedTask = taskRepository.save(task);
        return TaskDto.builder()
                .id(savedTask.getId())
                .name(savedTask.getName())
                .isDone(savedTask.isDone())
                .quantity(savedTask.getQuantity())
                .date(savedTask.getDay().getDate())
                .duration(savedTask.getDuration())
                .comments(savedTask.getComments())
                .startTime(task.getStartTime() == null ? UNSET_TIME : task.getStartTime().toString())
                .endTime(task.getEndTime() == null ? UNSET_TIME : task.getEndTime().toString())
                .build();
    }

    public List<TaskDto> getTasksForTheMonth(Long month, Long year) {
        String yearMonth = formatDate( month,  year);
        List<Day> dayList = dayRepository.findByMonthAndYear(yearMonth);
        return new ArrayList<TaskDto>();

    }

    private String formatDate(Long month, Long year){
        String monthString = month+"";
        if(month < 10){
            monthString = "0"+month;
        }
        return year+"-"+ monthString;
    }

    public List<TaskDto> getTasksByDateEmail(String date, String email) {
        LOGGER.info("Inside getTasksByDateEmail");
        Day today = dayRepository.findByDate(formatDate(date));
        List<TaskDto> taskDtos = new ArrayList<>();
        validateEmail(email);
        if(today == null){
            return taskDtos;
        }
        LOGGER.info("End getTasksForToday");
        List<Task> tasks = taskRepository.getTasksByDayIdAndEmail(today.getId(), email);
      taskDtos = tasks.stream().map(
                task->TaskDto.builder().id(task.getId())
                        .date(task.getDay().getDate())
                        .dayId(task.getDay().getId())
                        .name(task.getName())
                        .isDone(task.isDone())
                        .duration(task.getDuration())
                        .quantity(task.getQuantity())
                        .comments(task.getComments())
                        .email(task.getUser().getEmail())
                        .startTime(task.getStartTime() == null ? UNSET_TIME : task.getStartTime().toString())
                        .endTime(task.getEndTime() == null ? UNSET_TIME : task.getEndTime().toString())
                        .build()).toList();
        return taskDtos;
    }
    private void validateEmail(String email){
        if(email == null || email.isEmpty()){
            throw new DailyPlannerException(NO_EMAIL);
        }
    }

    public List<TaskDto> deleteTasksByIds(List<Long> taskIds) {
        List<Task> tasksToDelete = taskRepository.findAllById(taskIds);
        taskRepository.deleteAllById(taskIds);
        return  constructTaskDtos(tasksToDelete);
    }

    private List<TaskDto> constructTaskDtos(List<Task> tasks){
      return  tasks.stream().map(
                task->TaskDto.builder().id(task.getId())
                        .date(task.getDay().getDate())
                        .dayId(task.getDay().getId())
                        .name(task.getName())
                        .isDone(task.isDone())
                        .duration(task.getDuration())
                        .quantity(task.getQuantity())
                        .comments(task.getComments())
                        .email(task.getUser().getEmail())
                        .startTime(task.getStartTime() == null ? UNSET_TIME : task.getStartTime().toString())
                        .endTime(task.getEndTime() == null ? UNSET_TIME : task.getEndTime().toString())
                        .build()).toList();
    }
}
