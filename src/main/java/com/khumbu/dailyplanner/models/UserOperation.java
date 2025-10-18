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

    private String link;

    private int position;


    public UserOperation(Long id, String operation, boolean isActive, String link) {
        this.id = id;
        this.operation = operation;
        this.isActive = isActive;
        this.link = link;
    }

    public UserOperation(Long id, String operation, boolean isActive, String link, int position) {
        this.id = id;
        this.operation = operation;
        this.isActive = isActive;
        this.link = link;
        this.position = position;
    }

    public UserOperation() {
    }

    public UserOperation(String operation) {
        this.operation = operation;
        this.isActive = true;
    }

    public UserOperation(String operation, boolean isActive, String link) {
        this.operation = operation;
        this.isActive = true;
        this.link = link;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "UserOperation{" +
                "id=" + id +
                ", operation='" + operation + '\'' +
                ", isActive=" + isActive +
                ", link='" + link + '\'' +
                ", position=" + position +
                '}';
    }
}
