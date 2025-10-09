package com.khumbu.dailyplanner.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String operation;

    private boolean isActive;


    public UserOperation(Long id, String operation, boolean isActive) {
        this.id = id;
        this.operation = operation;
        this.isActive = isActive;
    }

    public UserOperation() {
    }

    public UserOperation(String operation) {
        this.operation = operation;
        this.isActive = true;
    }

    public UserOperation(String operation, boolean isActive) {
        this.operation = operation;
        this.isActive = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "UserOperation{" +
                "id=" + id +
                ", operation='" + operation + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
