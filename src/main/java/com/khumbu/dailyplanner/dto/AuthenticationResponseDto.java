package com.khumbu.dailyplanner.dto;

public class AuthenticationResponseDto {

    private String email;
    private String token;
    private long tokenExpirationDate;

    public AuthenticationResponseDto(String email, String token, long tokenExpirationDate) {
        this.email = email;
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public AuthenticationResponseDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(long tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }


    @Override
    public String toString() {
        return "AuthenticationResponseDto{" +
                "email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", tokenExpirationDate=" + tokenExpirationDate +
                '}';
    }
}
