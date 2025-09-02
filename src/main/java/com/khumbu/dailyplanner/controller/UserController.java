package com.khumbu.dailyplanner.controller;

import com.khumbu.dailyplanner.dto.ApiResponseDto;
import com.khumbu.dailyplanner.dto.AuthenticationResponseDto;
import com.khumbu.dailyplanner.dto.LoginDto;
import com.khumbu.dailyplanner.dto.UserDto;
import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.dto.RefreshTokenDto;
import com.khumbu.dailyplanner.models.RefreshToken;
import com.khumbu.dailyplanner.models.Users;
import com.khumbu.dailyplanner.service.JWTService;
import com.khumbu.dailyplanner.service.RefreshTokenService;
import com.khumbu.dailyplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/api/v1/users")
public class UserController {
@Autowired
private UserService userService;
    @Autowired
    private JWTService jwtService;
@Autowired
private RefreshTokenService refreshTokenService;
    @PostMapping("sign-up")
    public ResponseEntity<ApiResponseDto<UserDto>> signUp(@RequestBody UserDto userDto){
        ApiResponseDto<UserDto> apiResponseDto = userService.signUp(userDto);
        return ResponseEntity.status(apiResponseDto.getHttpStatus()).body(apiResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthenticationResponseDto>> login(@RequestBody LoginDto loginDto){
        ApiResponseDto<AuthenticationResponseDto> apiResponseDto = userService.authenticate(loginDto);
        return ResponseEntity.ok(apiResponseDto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto){
      return  ResponseEntity.ok(
        refreshTokenService.findByToken(refreshTokenDto.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    java.lang.String accessString = jwtService.generateToken(user.getEmail());
                    RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenDto.getToken()).get();
                    AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
                    authenticationResponseDto.setToken(accessString);
                    authenticationResponseDto.setEmail(user.getEmail());
                    authenticationResponseDto.setTokenExpirationDate(refreshToken.getExpiryDate().getTime());
                    authenticationResponseDto.setRefreshToken(refreshToken);
                    return authenticationResponseDto;

                }).orElseThrow(()-> new DailyPlannerException("Refresh token not found in database"))
      );
    }

}
