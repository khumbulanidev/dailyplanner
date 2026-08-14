package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.models.*;
import com.khumbu.dailyplanner.repository.DayRepository;
import com.khumbu.dailyplanner.repository.TaskRepository;
import com.khumbu.dailyplanner.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static com.khumbu.dailyplanner.constants.DailyPlannerConstants.*;

@Service
@Slf4j
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    private final DayRepository dayRepository;

    private final DayService dayService;

    private UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, DayRepository dayRepository, DayService dayService, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.dayRepository = dayRepository;
        this.dayService = dayService;
        this.userRepository = userRepository;
    }

    public List<TaskDto> getTasksById(Long day_id) {
        LOGGER.info("Inside getTasksById");

        List<Task> tasks = taskRepository.getTasksByDayId(day_id);
        List<TaskDto> taskDtos = tasks.stream().map(
                task -> TaskDto.builder().id(task.getId())
                        .date(task.getDay().getDate())
                        .dayId(task.getDay().getId())
                        .name(task.getName())
                        .isDone(task.isDone())
                        .duration(task.getDuration())
                        .quantity(task.getQuantity())
                        .comments(task.getComments())
                        .startTime(setTime(task.getStartTime()))
                        .endTime(setTime(task.getEndTime()))
                        .build()).toList();
        LOGGER.info("End getTasksById");
        return taskDtos;

    }

    public TaskDto saveTask(TaskDto taskDto) {
        LOGGER.info("Inside saveTask");
        Day day = dayRepository.findByDate(taskDto.getDate());
        Optional<Task> existingTask = taskRepository.findByNameAndDayAndUserEmail(taskDto.getName(), day, taskDto.getEmail());

        if (existingTask.isPresent() && existingTask.get().getDay().getDate().equals(taskDto.getDate())) {
            throw new DailyPlannerException("Task with name " + taskDto.getName() + " already exists for this date.");
        }

        if (day == null) {
            day = new Day();
            day.setDate(taskDto.getDate());
            dayRepository.save(day);
        }

        Long maxId = taskRepository.findMaxId();
        if (maxId == null) {
            maxId = 1L;
        }
        Users user = userRepository.findById(taskDto.getEmail()).orElseThrow(() -> new DailyPlannerException("User with email " + taskDto.getEmail() + " not found in the system."));
        Task task = Task.builder().id(maxId + 1).day(day).name(taskDto.getName()).isDone(taskDto.isDone()).duration(taskDto.getDuration()).quantity(taskDto.getQuantity()).comments(taskDto.getComments()).startTime(constructTime(taskDto.getStartTime())).endTime(constructTime(taskDto.getEndTime())).user(user).build();

        Task savedTask = taskRepository.save(task);
        LOGGER.info("End saveTask");
        return TaskDto.builder().dayId(savedTask.getId()).date(savedTask.getDay().getDate()).name(savedTask.getName()).id(savedTask.getId()).isDone(savedTask.isDone()).comments(savedTask.getComments()).duration(savedTask.getDuration()).startTime(setTime(savedTask.getStartTime())).endTime(setTime(savedTask.getEndTime())).build();
    }

    public List<TaskDto> getTasksForToday() {
        LOGGER.info("Inside getTasksForToday");

        Day today = dayRepository.findByDate(LocalDate.now());
        List<TaskDto> taskDtos = new ArrayList<>();

        if (today == null) {
            return taskDtos;
        }
        LOGGER.info(END_TASK_FOR_TODAY);
        return getTasksById(today.getId());
    }

    public List<TaskDto> getTasksByDate(String date) {
        LOGGER.info("Inside getTasksByDate");

        Day today = dayRepository.findByDate(formatDate(date));
        List<TaskDto> taskDtos = new ArrayList<>();

        if (today == null) {
            return taskDtos;
        }
        LOGGER.info(END_TASK_FOR_TODAY);
        return getTasksById(today.getId());

    }

    private LocalDate formatDate(String date) {
        //validate date before formating
        String dateArray[] = date.split("-");

        if (dateArray.length != 3) {
            LOGGER.error("Invalid date {}", date);
            throw new DailyPlannerException("Invalid date " + date);
        }

        return LocalDate.of(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
    }

    public TaskDto deleteTaskById(Long taskId) {

        Task task = this.taskRepository.findById(taskId).orElseThrow(() -> new DailyPlannerException("Task with ID " + taskId + " not found."));

        this.taskRepository.deleteById(taskId);

        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .isDone(task.isDone())
                .quantity(task.getQuantity())
                .date(task.getDay().getDate())
                .duration(task.getDuration())
                .comments(task.getComments())
                .startTime(setTime(task.getStartTime()))
                .endTime(setTime(task.getEndTime()))
                .build();
    }

    public TaskDto getTaskById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new DailyPlannerException("Task with id " + id + " was not found."));

        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .isDone(task.isDone())
                .startTime(setTime(task.getStartTime()))
                .endTime(setTime(task.getEndTime()))
                .quantity(task.getQuantity())
                .date(task.getDay().getDate())
                .duration(task.getDuration())
                .comments(task.getComments())
                .startTime(setTime(task.getStartTime()))
                .endTime(setTime(task.getEndTime()))
                .build();
    }

    private String setTime(LocalTime time) {
        return time == null ? UNSET_TIME : time.toString();
    }

    public TaskDto updateTask(TaskDto taskDto) {
        //check if task exists
        Task task = taskRepository.findById(taskDto.getId()).orElseThrow(
                () -> new DailyPlannerException("Task with id " + taskDto.getId() + " not found.")
        );
        Day day = dayRepository.findByDate(taskDto.getDate());

        if (day == null) {
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
                .startTime(setTime(savedTask.getStartTime()))
                .endTime(setTime(savedTask.getEndTime()))
                .build();
    }

    public List<TaskDto> getTasksForTheMonth(Long month, Long year) {
        String yearMonth = formatDate(month, year);
        List<Day> dayList = dayRepository.findByMonthAndYear(month, year);
        return new ArrayList<TaskDto>();

    }

    private String formatDate(Long month, Long year) {
        String monthString = month + "";
        if (month < 10) {
            monthString = "0" + month;
        }
        return year + "-" + monthString;
    }

    public List<TaskDto> getTasksByDateEmail(String date, String email) {
        LOGGER.info("Inside getTasksByDateEmail");
        Day today = dayRepository.findByDate(formatDate(date));
        List<TaskDto> taskDtos = new ArrayList<>();
        validateEmail(email);
        if (today == null) {
            return taskDtos;
        }
        LOGGER.info("End getTasksForToday");
        List<Task> tasks = taskRepository.getTasksByDayIdAndEmail(today.getId(), email);
        taskDtos = tasks.stream().map(
                task -> TaskDto.builder().id(task.getId())
                        .date(task.getDay().getDate())
                        .dayId(task.getDay().getId())
                        .name(task.getName())
                        .isDone(task.isDone())
                        .duration(task.getDuration())
                        .quantity(task.getQuantity())
                        .comments(task.getComments())
                        .email(task.getUser().getEmail())
                        .startTime(setTime(task.getStartTime()))
                        .endTime(setTime(task.getEndTime()))
                        .build()).toList();
        return taskDtos;
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new DailyPlannerException(NO_EMAIL);
        }
    }

    public List<TaskDto> deleteTasksByIds(List<Long> taskIds) {
        List<Task> tasksToDelete = taskRepository.findAllById(taskIds);
        taskRepository.deleteAllById(taskIds);
        return constructTaskDtos(tasksToDelete);
    }

    private List<TaskDto> constructTaskDtos(List<Task> tasks) {
        return tasks.stream().map(
                task -> TaskDto.builder().id(task.getId())
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

    private List<Task> constructTasksFromDto(List<TaskDto> taskDtos) {

        return taskDtos.stream().map(dto -> {
                    Day day = dayRepository.findById(dto.getDayId()).get();
                    Users user = userRepository.findByEmail(dto.getEmail()).get();

                    if (day == null) {
                        throw new DailyPlannerException("Day not found");
                    }

                    if (user == null) {
                        throw new DailyPlannerException("User not found");
                    }
                    return Task.builder()
                            .id(null)
                            .day(day)
                            .name(dto.getName())
                            .isDone(dto.isDone())
                            .duration(dto.getDuration())
                            .quantity(dto.getQuantity())
                            .comments(dto.getComments())
                            .user(user)
                            .startTime(constructTime(dto.getStartTime()))
                            .endTime(constructTime(dto.getEndTime()))
                            .build();

                }
        ).toList();

    }

    private LocalTime constructTime(String time) {

        if (time == null || time.equals("0") || time.equals(UNSET_TIME)) {
            return null;
        } else {
            return LocalTime.parse(time);
        }
    }

    @Transactional
    public DailyTasksDto saveAll(DailyTasksDto dailyTasksDto) {

        if (dailyTasksDto.getTasks().size() == 0) {
            throw new DailyPlannerException("No tasks to save");
        }

        List<Task> savedTasks = new ArrayList<>();
        List<TaskDto> savedDtos = new ArrayList<>();
        TaskDto firstTask = dailyTasksDto.getTasks().get(0);
        Optional<Users> optionalUser = userRepository.findByEmail(firstTask.getEmail());
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            LocalDate startDate = dailyTasksDto.getStartDate();
            LocalDate endDate = dailyTasksDto.getEndDate();
            startDate.datesUntil(endDate.plusDays(1)).forEach(date -> {
                        Day startDay = dayRepository.findByDate(date);
                        Day newDay = new Day();
                        if (startDay == null) {
                            newDay.setDate(date);
                            startDay = dayRepository.save(newDay);
                        }

                        //build task from task dto
                        //taskRepository.saveAll(constructTasksFromDto(dailyTasksDto.getTasks()));
                        for (TaskDto taskDto : dailyTasksDto.getTasks()) {
                            taskDto.setDate(date);
                            taskDto.setDayId(startDay.getId());
                            savedDtos.add(saveTask(taskDto)); //task is being saved here
                        }
                    }
            );
            List<TaskDto> savedTaskDtos = constructTaskDtos(savedTasks);
            DailyTasksDto savedDailyTasksDto = new DailyTasksDto();
            savedDailyTasksDto.setStartDate(dailyTasksDto.getStartDate());
            savedDailyTasksDto.setEndDate(dailyTasksDto.getEndDate());
            savedDailyTasksDto.setTasks(savedDtos);

            return savedDailyTasksDto;
        } else {
            throw new DailyPlannerException("User not found in system " + firstTask.getEmail());
        }

    }

    public Map<LocalDate, List<TaskDto>> getTaskForWeek(String date1, String email) {
        LocalDate date = formatDate(date1);

        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<LocalDate> fullWeek = monday.datesUntil(monday.plusWeeks(1)).toList();
        List<Task> tasksForWeek = new ArrayList<>();
        //find all tasks in this date range
        for (LocalDate localDate : fullWeek) {
            Day day = dayRepository.findByDate(localDate);
            if (day != null) {
                tasksForWeek.addAll(taskRepository.getTasksByDayIdAndEmail(day.getId(), email));
            }
        }
        List<TaskDto> taskDtos = tasksForWeek.stream().map(ta -> {
            return TaskDto.builder().id(ta.getId())
                    .date(ta.getDay().getDate())
                    .dayId(ta.getDay().getId())
                    .name(ta.getName())
                    .isDone(ta.isDone())
                    .duration(ta.getDuration())
                    .quantity(ta.getQuantity())
                    .comments(ta.getComments())
                    .email(ta.getUser().getEmail())
                    .startTime(setTime(ta.getStartTime()))
                    .endTime(setTime(ta.getEndTime()))
                    .build();
        }).toList();

        Map<LocalDate, List<TaskDto>> map = taskDtos.stream().collect(Collectors.groupingBy(TaskDto::getDate));

        return map;
    }

    private List<TaskDto> convertTaskListToTaskDtoList(List<Task> taskList) {

        return taskList.stream().map(ta -> {
            return TaskDto.builder().id(ta.getId())
                    .date(ta.getDay().getDate())
                    .dayId(ta.getDay().getId())
                    .name(ta.getName())
                    .isDone(ta.isDone())
                    .duration(ta.getDuration())
                    .quantity(ta.getQuantity())
                    .comments(ta.getComments())
                    .email(ta.getUser().getEmail())
                    .startTime(setTime(ta.getStartTime()))
                    .endTime(setTime(ta.getEndTime()))
                    .build();
        }).toList();
    }


    public Map<Integer, List<TaskDto>> getTasksForMonth(int month, int year, String email) {

        Map<Integer, List<TaskDto>> tasksForMonth = new HashMap<Integer, List<TaskDto>>();
        List<Task> tasksForTheMonth = taskRepository.getTasksForChart(month, email, year);

        List<TaskDto> taskDtoList = convertTaskListToTaskDtoList(tasksForTheMonth);

        Map<Integer, List<LocalDate>> weeksDateRangesForMonth = getWeeksDateRange(LocalDate.of(year, month, 1));

        List<TaskDto> week1Tasks = new ArrayList<>();
        tasksForMonth.put(1, week1Tasks);
        List<TaskDto> week2Tasks = new ArrayList<>();
        tasksForMonth.put(2, week2Tasks);
        List<TaskDto> week3Tasks = new ArrayList<>();
        tasksForMonth.put(3, week3Tasks);
        List<TaskDto> week4Tasks = new ArrayList<>();
        tasksForMonth.put(4, week4Tasks);
        List<TaskDto> week5Tasks = new ArrayList<>();
        tasksForMonth.put(5, week5Tasks);
        List<TaskDto> week6Tasks = new ArrayList<>();
        tasksForMonth.put(6, week6Tasks);

        for (TaskDto taskDto : taskDtoList) {

            boolean isInWeek1 = checkWeek(taskDto.getDate(), weeksDateRangesForMonth.get(1).get(0), weeksDateRangesForMonth.get(1).get(1));
            boolean isInWeek2 = checkWeek(taskDto.getDate(), weeksDateRangesForMonth.get(2).get(0), weeksDateRangesForMonth.get(2).get(1));
            boolean isInWeek3 = checkWeek(taskDto.getDate(), weeksDateRangesForMonth.get(3).get(0), weeksDateRangesForMonth.get(3).get(1));
            boolean isInWeek4 = checkWeek(taskDto.getDate(), weeksDateRangesForMonth.get(4).get(0), weeksDateRangesForMonth.get(4).get(1));
            boolean isInWeek5 = weeksDateRangesForMonth.get(6).size() == 2 ? checkWeek(taskDto.getDate(), weeksDateRangesForMonth.get(5).get(0), weeksDateRangesForMonth.get(5).get(1)) : false;
            boolean isInWeek6 = weeksDateRangesForMonth.get(6).size() == 2 ? checkWeek(taskDto.getDate(), weeksDateRangesForMonth.get(6).get(0), weeksDateRangesForMonth.get(6).get(1)) : false;

            if (isInWeek1) {
                tasksForMonth.merge(1, new ArrayList<>(Arrays.asList(taskDto)), (oldList, newList) -> {
                    oldList.addAll(newList);
                    return oldList;
                });
            } else if (isInWeek2) {
                tasksForMonth.merge(2, new ArrayList<>(Arrays.asList(taskDto)), (oldList, newList) -> {
                    oldList.addAll(newList);
                    return oldList;
                });
            } else if (isInWeek3) {
                tasksForMonth.merge(3, new ArrayList<>(Arrays.asList(taskDto)), (oldList, newList) -> {
                    oldList.addAll(newList);
                    return oldList;
                });
            } else if (isInWeek4) {
                tasksForMonth.merge(4, new ArrayList<>(Arrays.asList(taskDto)), (oldList, newList) -> {
                    oldList.addAll(newList);
                    return oldList;
                });
            } else if (isInWeek5) {
                tasksForMonth.merge(5, new ArrayList<>(Arrays.asList(taskDto)), (oldList, newList) -> {
                    oldList.addAll(newList);
                    return oldList;
                });
            } else if (isInWeek6) {
                tasksForMonth.merge(6, new ArrayList<>(Arrays.asList(taskDto)), (oldList, newList) -> {
                    oldList.addAll(newList);
                    return oldList;
                });
            }
        }


        return tasksForMonth;
    }

    /***
     * Filters tasks based on boolean value true for complete and false for incomplete
     * @param allTasks
     * @param isComplete
     * @return
     */
    public Map<Integer, List<TaskDto>> filterTasksOnCompletion(Map<Integer, List<TaskDto>> allTasks, boolean isComplete) {

        for (int key : allTasks.keySet()) {
            allTasks.merge(key, allTasks.get(key).stream().filter(task -> task.isDone() == isComplete).toList(), (oldList, newList) -> {
                return newList;
            });
        }

        return allTasks;
    }


    //check if date is in the range
    private boolean checkWeek(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return date.isAfter(startDate.minusDays(1)) && date.isBefore(endDate.plusDays(1));
    }

    /***
     * Gets all the weeks in a given month and associated date ranges for each week
     * Key represents the week from 1 to N
     * Value is a list with start date and end date for that week
     * @param date
     * @return Map<Integer, List < LocalDate>>
     */
    public Map<Integer, List<LocalDate>> getWeeksDateRange(LocalDate date) {

        Map<Integer, List<LocalDate>> weeksDateRanges = new HashMap<>();

        LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfFirstWeek = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        List<LocalDate> dateRange = new ArrayList<>();
        dateRange.add(firstDayOfMonth);
        dateRange.add(endOfFirstWeek);
        weeksDateRanges.put(1, dateRange);
        LocalDate endOfWeek = endOfFirstWeek;

        int daysInMonth = date.lengthOfMonth();
        for (int i = 2; i < 7; i++) {


            LocalDate startOfWeek = endOfWeek.plusDays(1);
            if (startOfWeek.getMonth() != date.getMonth()) {
                dateRange = new ArrayList<>();
                weeksDateRanges.put(i, dateRange);
                continue;
            }
            endOfWeek = startOfWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            dateRange = new ArrayList<>();
            dateRange.add(startOfWeek);
            dateRange.add(endOfWeek);
            weeksDateRanges.put(i, dateRange);
        }

        return weeksDateRanges;
    }
}
