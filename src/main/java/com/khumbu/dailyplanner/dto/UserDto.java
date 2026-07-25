package com.khumbu.dailyplanner.dto;

import com.khumbu.dailyplanner.models.Role;
import lombok.Builder;

import java.util.List;
@Builder
public class UserDto {

    private String email;
    private String firstname;
    private String lastname;

    private String phone;
    private String password;

    private boolean isLocked;

    private boolean isDisabled;

    private List<Role> roles;

    public UserDto(String email, String firstname, String lastname, String phone, String password) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.password = password;
    }

    public UserDto(String email, String firstname, String lastname, String phone, String password, boolean isLocked, boolean isDisabled, List<Role> roles) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.password = password;
        this.isLocked = isLocked;
        this.isDisabled = isDisabled;
        this.roles = roles;
    }

    public UserDto() {
    }

    public UserDto(String email, String firstname, String lastname, String phone, String password, List<Role> roles) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.password = password;
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", isLocked=" + isLocked +
                ", isDisabled=" + isDisabled +
                ", roles=" + roles +
                '}';
    }
}
