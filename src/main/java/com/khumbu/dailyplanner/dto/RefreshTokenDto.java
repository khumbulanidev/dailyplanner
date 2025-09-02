package com.khumbu.dailyplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RefreshTokenDto {

    //used to request a refresh token
    //this is just a UUID a random string
    private String token;

}
