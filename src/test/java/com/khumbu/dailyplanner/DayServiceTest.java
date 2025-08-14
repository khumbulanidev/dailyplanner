package com.khumbu.dailyplanner;

import com.khumbu.dailyplanner.exceptions.DayException;
import com.khumbu.dailyplanner.models.Day;
import com.khumbu.dailyplanner.models.DayDto;
import com.khumbu.dailyplanner.repository.DayRepository;
import com.khumbu.dailyplanner.service.DayService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DayServiceTest {
    @Mock
    DayRepository dayRepository;
    @InjectMocks
    private DayService dayService;
//    @BeforeEach
//    public void setup(){
//        //dayService= new DayService();
//    }
@Test
    public void testSaveThrowsDayException(){
    //DayRepository dayRepository= mock(DayRepository.class);
    DayDto dayDto = getDayDto();
//    DayService dayService= new DayService();

    //when(dayRepository.findMaxId()).thenReturn(0L);

    when(dayRepository.findByDate(any())).thenReturn(null);
    when(dayRepository.save(any())).thenReturn(dayDto.getDay());
    //act
    //returns daydto
    DayDto dto = dayService.save(dayDto);
    assertEquals(dayDto, dto);


    //assert
//    if(ObjectUtils.isEmpty(dayDto)){
//
//        throw new DayException("DayDto cannot be empty");
//    }
    //2
//    Day day = dayRepository.findByDate(dayDto.getDate());
//    if (day != null){
//        logger.error(" inside exception if ");
//        //throw new DayException("Day already exists");
//        return null;
//    }
//3
//    //find maximum id
//    Long maxId=dayRepository.findMaxId();
//    if(maxId==null)
//    {
//        maxId=0L;
//    }
//    day = Day.builder().id(maxId+1).date(dayDto.getDate()).build();
//    return DayDto.create(dayRepository.save(day));


}

private DayDto getDayDto(){
    Long id= 1L;
    LocalDate date = LocalDate.now();
    Day day = new Day();
    day.setId(id);
    day.setDate(date);
    return  DayDto.create(day);
}

}
