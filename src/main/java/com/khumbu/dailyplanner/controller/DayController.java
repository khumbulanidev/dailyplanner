package com.khumbu.dailyplanner.controller;

import com.khumbu.dailyplanner.dto.ApiResponseDto;
import com.khumbu.dailyplanner.models.Day;
import com.khumbu.dailyplanner.models.DayDto;
import com.khumbu.dailyplanner.models.TaskDto;
import com.khumbu.dailyplanner.service.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/days")
@CrossOrigin(origins = "http://localhost:4200")
public class DayController {

    @Autowired
    private DayService dayService;
    @GetMapping
    public List<DayDto> getAll(){
        return dayService.getAll();
    }

    @GetMapping("/:date")
    public DayDto getDayInfo(@PathVariable LocalDate date){
        return this.dayService.getDay(date);
    }


    @PostMapping("/save")
    public ResponseEntity<Object> saveDay(@RequestBody  DayDto dayDto){
        DayDto dayDto1= dayService.save(dayDto);
        if (dayDto1 != null){
            return ResponseEntity.status(HttpStatus.OK).body(dayDto1);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto<>(null, "Day already exists", HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @DeleteMapping("/delete/{id}")
    public DayDto deleteById(@PathVariable Long id){
    return dayService.deleteById(id);
    }

    @GetMapping("/{month}/{year}")
    public ResponseEntity<List<Day>> getDaysOfMonth(@PathVariable Long month, @PathVariable Long year){
        return ResponseEntity.ok(dayService.getDaysOfTheMonth(month, year));
    }

}
