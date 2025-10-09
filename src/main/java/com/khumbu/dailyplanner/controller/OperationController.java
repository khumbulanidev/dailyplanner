package com.khumbu.dailyplanner.controller;

import com.khumbu.dailyplanner.models.UserOperation;
import com.khumbu.dailyplanner.service.UserOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operations")
@CrossOrigin(origins = "http://localhost:4200")
public class OperationController {

    @Autowired
    private UserOperationService userOperationService;


    @GetMapping
    public ResponseEntity<List<UserOperation>> getOperations(){
        return ResponseEntity.ok(userOperationService.getOperations());
    }

    @PostMapping("/save")
    public ResponseEntity<UserOperation> save(@RequestBody UserOperation operation){

        return ResponseEntity.ok(userOperationService.save(operation));
    }

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<UserOperation> deleteById(@PathVariable Long id)
    {
        return ResponseEntity.ok(userOperationService.deleteById(id));
    }
}
