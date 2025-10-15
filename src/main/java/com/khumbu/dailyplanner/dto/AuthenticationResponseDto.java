package com.khumbu.dailyplanner.dto;


import com.khumbu.dailyplanner.models.RefreshToken;

public class AuthenticationResponseDto {
    private String fullName;

    private String email;
    private String token;
    private RefreshToken refreshToken;
    private long tokenExpirationDate;

    public AuthenticationResponseDto(String email, String token, long tokenExpirationDate) {
        this.email = email;
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public AuthenticationResponseDto(String fullName, String email, String token, RefreshToken refreshToken, long tokenExpirationDate) {
        this.fullName = fullName;
        this.email = email;
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public AuthenticationResponseDto(String email, String token, long tokenExpirationDate, RefreshToken refreshToken) {
        this.email = email;
        this.token = token;
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

    @Override
    public String toString() {
        return "AuthenticationResponseDto{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", refreshToken=" + refreshToken +
                ", tokenExpirationDate=" + tokenExpirationDate +
                '}';
    }
}
