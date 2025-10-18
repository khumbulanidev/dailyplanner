package com.khumbu.dailyplanner.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@ControllerAdvice()
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger= LoggerFactory.getLogger(AppExceptionHandler.class);
    @ExceptionHandler(value={DayException.class})
    public ResponseEntity<AppError> handleDayException( DayException exception){
        AppError error = AppError
                .builder()
                .date(LocalDateTime.now()).message(exception.getMessage())
                .statusCode(exception.getStatus() != null ? exception.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<AppError>(error,error.getStatusCode());
    }


    @ExceptionHandler(value={AuthenticationException.class})
    public ResponseEntity<AppError> handleAuthenticationException(AuthenticationException authenticationException){
        AppError error = AppError
                .builder()
                .date(LocalDateTime.now()).message(authenticationException.getMessage())
                .statusCode( HttpStatus.UNAUTHORIZED)
                .build();
        return new ResponseEntity<AppError>(error,error.getStatusCode());
    }


    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<AppError> handleAuthenticationException(Exception exception){
        AppError error = AppError
                .builder()
                .date(LocalDateTime.now()).message(exception.getMessage())
                .statusCode( HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<AppError>(error,error.getStatusCode());
    }

    @ExceptionHandler(value={TokenException.class})
    public ResponseEntity<TokenError> handleDalyPlannerException(TokenException exception){
        TokenError error = new TokenError();
        error.setStatusCode(HttpStatus.UNAUTHORIZED);
        error.setMessage(exception.getMessage());
        if(exception.getMessage().contains("expired")){
            error.setTokenExpired(true);
        }
        else{
            error.setTokenExpired(false);
        }
        error.setDate(LocalDateTime.now());
        error.setStatusCode(exception.getStatus());


        return new ResponseEntity<TokenError>(error,error.getStatusCode());
    }

    @ExceptionHandler(value={AccessDeniedException.class})
    public ResponseEntity<AppError> handleDalyPlannerException(AccessDeniedException exception){
        AppError error = new AppError();
        error.setStatusCode(HttpStatus.FORBIDDEN);
        error.setMessage(exception.getMessage());

        error.setDate(LocalDateTime.now());


        return new ResponseEntity<AppError>(error,error.getStatusCode());
    }
}
