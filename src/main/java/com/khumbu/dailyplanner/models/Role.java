package com.khumbu.dailyplanner.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Role{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(name = "role")
    private  String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    List<Users> users = new ArrayList<>();

    private boolean active;

    public Role(Long roleId, String role, List<Users> users) {
        this.roleId = roleId;
        this.name = role;
        this.users = users;
    }

    public Role(Long roleId, String name, List<Users> users, boolean active) {
        this.roleId = roleId;
        this.name = name;
        this.users = users;
        this.active = active;
    }

    public Role() {
    }

    public Role(String role) {
        this.name = role;
    }

    public void setName(String name) {
        this.name = name;
    }




    public String getName() {
        return name;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + roleId
                +
                ", authority='" + name + '\'' +
                '}';
    }
}
