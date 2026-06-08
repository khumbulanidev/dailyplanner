package com.khumbu.dailyplanner;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.exceptions.DayException;
import com.khumbu.dailyplanner.models.Day;
import com.khumbu.dailyplanner.models.DayDto;
import com.khumbu.dailyplanner.repository.DayRepository;
import com.khumbu.dailyplanner.service.DayService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DayServiceTest {


    @Mock
    private DayRepository dayRepositoryMock;
    @InjectMocks
    private DayService dayService;

    //    public DayDto save(DayDto dayDto){
//
//        if(ObjectUtils.isEmpty(dayDto)){
//
//            throw new DayException("DayDto cannot be empty");
//        }
//        Day day = dayRepository.findByDate(dayDto.getDate());
//        if (day != null){
//            logger.error(" inside exception if "); //what is this for?
//            return null;
//        }
//
//        //find maximum id
//        Long maxId=dayRepository.findMaxId();
//        if(maxId == null)
//        {
//            maxId = 0L;
//        }
//        day = Day.builder().id(maxId + 1).date(dayDto.getDate()).build();
//        return DayDto.create(dayRepository.save(day));
//    }
    private static Day day;
    private static DayDto dayDto;

    @BeforeAll
    public static void setup() {
        day = new Day();
        day.setDate(LocalDate.now());
        day.setId(1L);
        dayDto = new DayDto(1L, day.getDate(), null);
    }

    @Test
    public void saveThrowsExceptionIfDayDtoIsEmpty() {
        DayDto dayDto = null;
        assertThrows(DayException.class, () -> dayService.save(dayDto));
    }

    @Test
    public void saveReturnsNullWhenDayAlreadyExists() {

        when(dayRepositoryMock.findByDate(any())).thenReturn(day);

        DayDto result = dayService.save(dayDto);
        verify(dayRepositoryMock).findByDate(any());
        assertNull(result);
    }


    @Test
    public void saveCreatesNewDayWhenDayIsNull() {
        Day savedDay = new Day(1L, dayDto.getDate(), null);
        when(dayRepositoryMock.findByDate(dayDto.getDate())).thenReturn(null);
        when(dayRepositoryMock.save(any())).thenReturn(day);
        DayDto savedDto = dayService.save(dayDto);
        assertEquals(savedDto, dayDto);

    }
    @Test
    public void getAllReturnsCorrectList() {
        List<Day> dayList = new ArrayList<>();
        dayList.add(day);
        when(dayRepositoryMock.findAll()).thenReturn(dayList);
        List<DayDto> result = dayService.getAll();
        assertEquals(result.get(0), dayDto);

    }

    @Test
    public void deleteByIdCallsRepositoryDeleteById() {
        when(dayRepositoryMock.findById(any())).thenReturn(Optional.of(day));
        DayDto result = dayService.deleteById(1L);
        verify(dayRepositoryMock).deleteById(1L);

    }

    @Test
    public void deleteByIdThrowsExceptionWhenIdIsNotFound() {
        when(dayRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DailyPlannerException.class,()->dayService.deleteById(1L));

    }

//WHAT DOES THIS ONE DO
    //    public DayDto getDay(LocalDate date) {
//        Day day = this.dayRepository.findByDate(date);
//
//        if(day == null){
//            Long maxId = this.dayRepository.findMaxId() + 1;
//            Day day1 = new Day();
//            day1.setDate(date);
//            day = this.dayRepository.save(day1);
//        }
//        DayDto dayDto = DayDto.create(day);
//
//        return dayDto;
//    }


}


//
//package com.khumbu.dailyplanner.service;

//
//@Service
//@Slf4j
//public class DayService {
//
//    private Logger logger= LoggerFactory.getLogger(com.khumbu.dailyplanner.service.DayService.class);
//
//    @Autowired
//    private DayRepository dayRepository;
//
//    @Autowired
//    private TaskRepository taskRepository;
//

//


//

//
//    public List<Day> getDaysOfTheMonth(Long month, Long year) {
//        String yearMonth = formatDate( month,  year);
//
//        //get the ids of all the days in month and year
//        List<Day> dayList = dayRepository.findByMonthAndYear(yearMonth);
//        return dayList;
//
//
//    }
//
//    private String formatDate(Long month, Long year){
//        String monthString = month+"";
//        if(month < 10){
//            monthString = "0"+month;
//        }
//        return year+"-"+ monthString;
//    }
//
//    public List<DayTaskDto> getDaysOfTheMonthForUser(Long month, Long year, String email) {
//        List<Day> days =  getDaysOfTheMonth(month, year);
//        List<DayTaskDto> taskDtos = new ArrayList<>();
//        for(Day day :days){
//            List<Task> tasksForUser = day.getTasks().stream().filter(t -> t.getUser().getEmail().equals(email)).toList();
//            DayTaskDto dayTaskDto = new DayTaskDto();
//            int dayValue = day.getDate().getDayOfMonth();
//            dayTaskDto.setDay(dayValue);
//            dayTaskDto.setNumberOfTasks(tasksForUser.size());
//            taskDtos.add(dayTaskDto);
//        }
//        return taskDtos;
//    }
//}
