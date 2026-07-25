package com.khumbu.dailyplanner.repository;

import com.khumbu.dailyplanner.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {


    Optional<Users> findByEmail(String email);
}
