package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.exceptions.DayException;
import com.khumbu.dailyplanner.models.Day;
import com.khumbu.dailyplanner.models.DayDto;
import com.khumbu.dailyplanner.repository.DayRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
         if(maxId==null)
         {
             maxId=0L;
         }
        day = Day.builder().id(maxId+1).date(dayDto.getDate()).build();
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
}
