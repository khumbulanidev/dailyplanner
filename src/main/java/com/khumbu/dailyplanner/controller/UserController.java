package com.khumbu.dailyplanner.controller;

import com.khumbu.dailyplanner.dto.ApiResponseDto;
import com.khumbu.dailyplanner.dto.UserDto;
import com.khumbu.dailyplanner.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
@Autowired
private UserService userService;
    @PostMapping("register")
    public ResponseEntity<ApiResponseDto<UserDto>> signUp(@RequestBody UserDto userDto){
        ApiResponseDto<UserDto> apiResponseDto = userService.signUp(userDto);
        return ResponseEntity.status(apiResponseDto.getHttpStatus()).body(apiResponseDto);
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResponseDto<String>> login(@RequestBody UserDto user){
        ApiResponseDto<String> apiResponseDto = new ApiResponseDto<>();
        apiResponseDto.setData("Login successful");
        return ResponseEntity.ok(apiResponseDto);
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponseDto<String>> tes(){
        ApiResponseDto<String> apiResponseDto = new ApiResponseDto<>();
        apiResponseDto.setData("Login successful");
        return ResponseEntity.ok(apiResponseDto);
    }
}
