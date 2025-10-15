package com.khumbu.dailyplanner.repository;

import com.khumbu.dailyplanner.models.UserOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOperationRepository extends JpaRepository<UserOperation, Long> {
}
