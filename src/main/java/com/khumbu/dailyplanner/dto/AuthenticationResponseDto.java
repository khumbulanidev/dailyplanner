package com.khumbu.dailyplanner.dto;


import com.khumbu.dailyplanner.models.RefreshToken;

public class AuthenticationResponseDto {

    private java.lang.String email;
    private java.lang.String string;
    private RefreshToken refreshToken;
    private long tokenExpirationDate;

    public AuthenticationResponseDto(String email, String string, long tokenExpirationDate) {
        this.email = email;
        this.string = string;
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public AuthenticationResponseDto(java.lang.String email, java.lang.String string, long tokenExpirationDate, RefreshToken refreshToken) {
        this.email = email;
        this.string = string;
        this.refreshToken = refreshToken;
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public AuthenticationResponseDto() {
    }

    public java.lang.String getEmail() {
        return email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public java.lang.String getToken() {
        return string;
    }

    public void setToken(java.lang.String string) {
        this.string = string;
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

    @Override
    public java.lang.String toString() {
        return "AuthenticationResponseDto{" +
                "email='" + email + '\'' +
                ", token='" + string + '\'' +
                ", tokenExpirationDate=" + tokenExpirationDate +
                '}';
    }
}
