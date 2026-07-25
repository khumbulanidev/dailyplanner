package com.khumbu.dailyplanner.controller;

import com.khumbu.dailyplanner.models.Role;
import com.khumbu.dailyplanner.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAll(){
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Role> get(@PathVariable Long id){
        return ResponseEntity.ok(roleService.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Role> delete(@PathVariable Long id){
        return ResponseEntity.ok(roleService.deleteById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<Role> save(@RequestBody Role role){
        return ResponseEntity.ok(roleService.save(role));
    }




}
