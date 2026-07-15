package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.models.Role;
import com.khumbu.dailyplanner.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {


    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAll(){
        return roleRepository.findAll();
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public Role getById(Long id){

        Role role = roleRepository.findById(id).orElseThrow(()-> new DailyPlannerException(" Role with id " + id + " not found"));
        return role;
    }

    public Role deleteById(Long id){
        Role role = roleRepository.findById(id).orElseThrow(()-> new DailyPlannerException(" Role with id " + id + " not found"));
        roleRepository.deleteById(id);
        return role;
    }
}
