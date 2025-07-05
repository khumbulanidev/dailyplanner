package com.khumbu.dailyplanner.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
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
}
