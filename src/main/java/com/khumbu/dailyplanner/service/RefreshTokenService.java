package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.exceptions.TokenException;
import com.khumbu.dailyplanner.models.RefreshToken;
import com.khumbu.dailyplanner.models.Users;
import com.khumbu.dailyplanner.repository.TokenRepository;
import com.khumbu.dailyplanner.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private Logger LOGGER = LoggerFactory.getLogger(RefreshTokenService.class);

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(java.lang.String email) throws DailyPlannerException {
        Users user = userRepository.findById(email).orElseThrow(()-> new DailyPlannerException("User with email "+ email + " was not found."));
        //check if token exists for user and if so delete it
        Optional<RefreshToken> optionalToken = tokenRepository.findByEmail(email);
        if(optionalToken.isPresent()){
            tokenRepository.delete(optionalToken.get());
        }
        LocalDateTime date = LocalDateTime.now();


        RefreshToken refactorToken = RefreshToken.builder().user(user).refreshToken(UUID.randomUUID().toString())
                .expiryDate(Date.from(date.plusMinutes(60).atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        return tokenRepository.save(refactorToken);
    }

    public Optional<RefreshToken> findByToken(String refactorToken){
        return tokenRepository.findByRefreshToken(refactorToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken){
        if(refreshToken.getExpiryDate().compareTo(new Date()) < 0){
            tokenRepository.delete(refreshToken);
            throw new TokenException(refreshToken.getRefreshToken() + " Refresh token expired, please sign in again.", HttpStatus.UNAUTHORIZED);
        }
    return refreshToken;
    }

    public Optional<RefreshToken> findByEmail(String email) {
        return tokenRepository.findByEmail(email);

    }

    public Optional<RefreshToken> deleteByEmail(String email){
        Optional<RefreshToken> refreshTokenOptional = tokenRepository.findByEmail(email);
        if(refreshTokenOptional.isPresent()){
            tokenRepository.delete(refreshTokenOptional.get());
        }
        return refreshTokenOptional;
    }
}
