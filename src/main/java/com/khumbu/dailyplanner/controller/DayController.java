package com.khumbu.dailyplanner.controller;

import com.khumbu.dailyplanner.models.Day;
import com.khumbu.dailyplanner.models.DayDto;
import com.khumbu.dailyplanner.service.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/days")
public class DayController {

    @Autowired
    private DayService dayService;
@GetMapping
public List<DayDto> getAll(){
    return dayService.getAll();
}
    @PostMapping("/save")
    public DayDto saveDay(@RequestBody  DayDto dayDto){

        return dayService.save(dayDto);
    }

    @DeleteMapping("/delete/{id}")
    public DayDto deleteById(@PathVariable Long id){
    return dayService.deleteById(id);

    }
}
