package com.khumbu.dailyplanner.repository;


import com.khumbu.dailyplanner.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


    Role findByName(String user);
}
