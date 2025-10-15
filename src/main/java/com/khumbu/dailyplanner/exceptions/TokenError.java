package com.khumbu.dailyplanner.exceptions;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TokenError extends AppError{

    private boolean isTokenExpired;
}
