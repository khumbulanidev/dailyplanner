package com.khumbu.dailyplanner.repository;

import com.khumbu.dailyplanner.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {


}
