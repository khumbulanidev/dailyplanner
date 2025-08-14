package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.exceptions.DayException;
import com.khumbu.dailyplanner.models.Day;
import com.khumbu.dailyplanner.models.DayDto;
import com.khumbu.dailyplanner.models.TaskDto;
import com.khumbu.dailyplanner.repository.DayRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DayService {

    private  Logger logger= LoggerFactory.getLogger(DayService.class);

    @Autowired
    private DayRepository dayRepository;
    //todo add logging

    public DayDto save(DayDto dayDto){

        if(ObjectUtils.isEmpty(dayDto)){

            throw new DayException("DayDto cannot be empty");
        }
        Day day = dayRepository.findByDate(dayDto.getDate());
        if (day != null){
            logger.error(" inside exception if ");
            return null;
        }

        //find maximum id
        Long maxId=dayRepository.findMaxId();
         if(maxId == null)
         {
             maxId = 0L;
         }
        day = Day.builder().id(maxId + 1).date(dayDto.getDate()).build();
        return DayDto.create(dayRepository.save(day));
    }

    public List<DayDto> getAll(){
        List<Day> dayList= dayRepository.findAll();
      return   DayDto.createList(dayList);
    }

    public DayDto deleteById(Long id) {
        Optional<Day> dayOptional=dayRepository.findById(id);
        if(dayOptional.isPresent()){
            dayRepository.deleteById(id);
            return DayDto.create(dayOptional.get());
        }
        throw new DailyPlannerException("Day with id was not found : " +id);
    }

    public DayDto getDay(LocalDate date) {
        Day day = this.dayRepository.findByDate(date);

        if(day == null){
            Long maxId = this.dayRepository.findMaxId() + 1;
            Day day1 = new Day();
            day1.setDate(date);
            day = this.dayRepository.save(day1);
        }
        DayDto dayDto = DayDto.create(day);

        return dayDto;
    }

    //day contains a list of tasks
    public List<Day> getDaysOfTheMonth(Long month, Long year) {
        String yearMonth = formatDate( month,  year);

        //get the ids of all the days in month and year
        List<Day> dayList = dayRepository.findByMonthAndYear(yearMonth);
        return dayList;


    }




    private String formatDate(Long month, Long year){
        String monthString = month+"";
        if(month < 10){
            monthString = "0"+month;
        }
        return year+"-"+ monthString;
    }
}
