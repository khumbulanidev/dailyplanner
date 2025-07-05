package com.khumbu.dailyplanner.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
public class Users {

    @Id
    private String email;
    private String firstname;
    private String lastname;

    private String phone;
    private String password;
    @Column(name = "account_expired")
    private boolean isAccountExpired;
    @Column(name = "locked")
    private boolean isLocked;
    @Column(name = "disabled")
    private boolean isDisabled;
    @Column(name = "credential_expired")
    private boolean isCredentialExpired;


    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "email"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<Role> roles = new ArrayList<>();

    public Users() {
    }

    public Users(String email, String firstname, String lastname, String phone, String password) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.password = password;
    }

    public Users(String email, String firstname, String lastname, String phone, String password,
                 boolean isAccountExpired, boolean isLocked, boolean isDisabled, boolean isCredentialExpired, List<Role> roles) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.password = password;
        this.isAccountExpired = isAccountExpired;
        this.isLocked = isLocked;
        this.isDisabled = isDisabled;
        this.isCredentialExpired = isCredentialExpired;
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

    public boolean isAccountExpired() {
        return isAccountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        isAccountExpired = accountExpired;
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

    public boolean isCredentialExpired() {
        return isCredentialExpired;
    }

    public void setCredentialExpired(boolean credentialExpired) {
        isCredentialExpired = credentialExpired;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Users{" +
                "email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", isAccountExpired=" + isAccountExpired +
                ", isLocked=" + isLocked +
                ", isDisabled=" + isDisabled +
                ", isCredentialExpired=" + isCredentialExpired +
                '}';
    }
}
