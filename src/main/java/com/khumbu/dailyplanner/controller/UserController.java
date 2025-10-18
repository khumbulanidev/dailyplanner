package com.khumbu.dailyplanner.controller;

import com.khumbu.dailyplanner.dto.*;
import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.models.RefreshToken;
import com.khumbu.dailyplanner.service.JWTService;
import com.khumbu.dailyplanner.service.RefreshTokenService;
import com.khumbu.dailyplanner.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/api/v1/users")
public class UserController {
@Autowired
private UserService userService;

    Logger LOGGER = LoggerFactory.getLogger(UserService.class);
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
        LOGGER.info(apiResponseDto.toString());
        return ResponseEntity.ok(apiResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutDto> logout(@RequestBody String username){
        String response = userService.logout(username);
        LogoutDto logoutDto = new LogoutDto(username, response);
        return ResponseEntity.ok(logoutDto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto){
        AuthenticationResponseDto authenticationResponse= refreshTokenService.findByToken(refreshTokenDto.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    java.lang.String accessString = jwtService.generateToken(user.getEmail());
                    RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenDto.getRefreshToken()).get();
                    AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
                    authenticationResponseDto.setToken(accessString);
                    authenticationResponseDto.setEmail(user.getEmail());
                    authenticationResponseDto.setTokenExpirationDate(refreshToken.getExpiryDate().getTime());
                    authenticationResponseDto.setRefreshToken(refreshToken);
                    return authenticationResponseDto;

                }).orElseThrow(()-> new DailyPlannerException("Refresh token not found in database"));

      return  ResponseEntity.ok(authenticationResponse);

    }

}
