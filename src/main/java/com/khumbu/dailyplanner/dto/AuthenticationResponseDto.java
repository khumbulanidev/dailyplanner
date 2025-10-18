package com.khumbu.dailyplanner.dto;


import com.khumbu.dailyplanner.models.RefreshToken;
import com.khumbu.dailyplanner.models.Role;

import java.util.List;

public class AuthenticationResponseDto {
    private String fullName;

    private String email;
    private String token;
    private RefreshToken refreshToken;
    private long tokenExpirationDate;


    private List<Role> roles;

    public AuthenticationResponseDto(String email, String token, long tokenExpirationDate, List<Role> roles) {
        this.email = email;
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
        this.roles = roles;
    }

    public AuthenticationResponseDto(String fullName, String email, String token, RefreshToken refreshToken, long tokenExpirationDate, List<Role> roles) {
        this.fullName = fullName;
        this.email = email;
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenExpirationDate = tokenExpirationDate;
        this.roles = roles;
    }

    public AuthenticationResponseDto(String email, String token, long tokenExpirationDate, RefreshToken refreshToken, List<Role> roles) {
        this.email = email;
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenExpirationDate = tokenExpirationDate;
        this.roles = roles;
    }

    public AuthenticationResponseDto() {
    }

    public java.lang.String getEmail() {
        return email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public  String getToken() {
        return token;
    }

    public void setToken( String string) {
        this.token= string;
    }

    public long getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(long tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AuthenticationResponseDto{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", refreshToken=" + refreshToken +
                ", tokenExpirationDate=" + tokenExpirationDate +
                ", roles=" + roles +
                '}';
    }
}
