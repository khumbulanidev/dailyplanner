package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.models.Role;
import com.khumbu.dailyplanner.models.Users;
import com.khumbu.dailyplanner.repository.RoleRepository;
import com.khumbu.dailyplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {


    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role save(Role role) {

        verifyRole(role);

        if (role.getRoleId() != 0) {

            Role roleFromDb = roleRepository.findById(role.getRoleId())
                                            .orElseThrow(() -> new DailyPlannerException("Role with id "+ role.getRoleId() + " not found."));
            //update the name but it has to be updated everywhered
            if (!roleFromDb.getName().equals(role.getName())) {
                Role roleToCheck = roleRepository.findByName(role.getName());
                if (roleToCheck != null) {
                    throw new DailyPlannerException(" Role with name " + roleToCheck.getName() + " already exists");
                }
            }
            roleFromDb.setActive(role.isActive());
            roleFromDb.setName(role.getName());
            return roleFromDb;
        } else {
            return roleRepository.save(role);
        }
    }

    private void verifyRole(Role role) {
        String roleName = role.getName();
        if (role == null || role.getName().isEmpty()) {
            throw new DailyPlannerException(" Role  name cannot be null or empty");
        }
        if (role.getName().length() < 3) {
            throw new DailyPlannerException(" Role  name cannot be less than 3 characters");
        }


    }

    public Role getById(Long id) {

        Role role = roleRepository.findById(id).orElseThrow(() -> new DailyPlannerException(" Role with id " + id + " not found"));
        return role;
    }

    public Role deleteById(Long id) {

        Role roleFromDb = roleRepository.findById(id).orElseThrow(()-> new DailyPlannerException(" Role with id " + id + " not found"));
        List<Users> users = roleFromDb.getUsers();
        if(users.size() > 0){
            throw new DailyPlannerException("There are users using this role. Delete or change role for the users before deleting this Role.");
        }
        roleRepository.deleteById(id);
        return roleFromDb;
    }
}
