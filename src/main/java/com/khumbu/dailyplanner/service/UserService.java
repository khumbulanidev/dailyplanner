package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.dto.ApiResponseDto;
import com.khumbu.dailyplanner.dto.UserDto;
import com.khumbu.dailyplanner.exceptions.DayException;
import com.khumbu.dailyplanner.models.Role;
import com.khumbu.dailyplanner.models.Users;
import com.khumbu.dailyplanner.repository.RoleRepository;
import com.khumbu.dailyplanner.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import  com.khumbu.dailyplanner.constants.DailyPlannerConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  BCryptPasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public ApiResponseDto<UserDto> signUp(UserDto userDto){

        Optional<Users> usersOptional = userRepository.findById(userDto.getEmail());
        if(usersOptional.isPresent()){
           throw  new DayException("User with "+" "+ userDto.getEmail()+" already exists.");
        }
        List<Role> roles = userDto.getRoles();
        if(roles == null){
            roles = new ArrayList<>();
            Role userRole = new Role("USER");
            roles.add(userRole);
        }

        validateUserDto(userDto);
        validateUsername(userDto);
        Users user = Users.builder()
                .email(userDto.getEmail())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .phone(userDto.getPhone())
                .password(encoder.encode(userDto.getPassword()))
                .isAccountExpired(false)
                .isCredentialExpired(false)
                .isDisabled(false)
                .isLocked(false)
                .roles(roles)
                .build();
        List<Role> savedRoles = roleRepository.saveAll(roles);
        logger.info("Roles saved {}", savedRoles);
        Users savedUser = userRepository.save(user);

        userDto.setPassword(savedUser.getPassword());
        ApiResponseDto<UserDto> responseDto = new ApiResponseDto<>();
        responseDto.setHttpStatus(HttpStatus.OK);
        responseDto.setMessage("Registration successful");
        responseDto.setTime(LocalDateTime.now());
        UserDto savedDto = new UserDto(savedUser.getEmail(), savedUser.getFirstname(), savedUser.getLastname(),savedUser.getPhone(),null, savedUser.getRoles());
        responseDto.setData(savedDto);
        logger.info("User saved : {} ", savedUser);
        return responseDto;
    }

    private void validateUserDto(UserDto userDto){
        if(ObjectUtils.isEmpty(userDto.getEmail()) ||
                ObjectUtils.isEmpty(userDto.getFirstname()) ||
                ObjectUtils.isEmpty(userDto.getLastname()) ||
                ObjectUtils.isEmpty(userDto.getPassword()) ){

            logger.error(DailyPlannerConstants.FIELDS_MISSING);
            throw new DayException(DailyPlannerConstants.FIELDS_MISSING);
        }
    }

    private void validateUsername(UserDto userDto){
        Optional<Users> usersOptional = userRepository.findById(userDto.getEmail());
        if(!usersOptional.isEmpty()){
            throw new DayException(DailyPlannerConstants.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
    }

//    public String authenticate(UserDto userDto) {
//
//        //convert userdto to user
//        Users user = generateUser(userDto);
//        //authenticate the user
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
//
//        return authentication.isAuthenticated() ? "Success" : "Fail";
//
//    }

    public Users generateUser(UserDto userDto){
        Users user = new Users(userDto.getEmail(), userDto.getFirstname(), userDto.getLastname(), userDto.getPhone(), userDto.getPassword());
        return  user;
    }
}
