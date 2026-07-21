package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.dto.ApiResponseDto;
import com.khumbu.dailyplanner.dto.AuthenticationResponseDto;
import com.khumbu.dailyplanner.dto.LoginDto;
import com.khumbu.dailyplanner.dto.UserDto;
import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.exceptions.DayException;
import com.khumbu.dailyplanner.models.RefreshToken;
import com.khumbu.dailyplanner.models.Role;
import com.khumbu.dailyplanner.models.Users;
import com.khumbu.dailyplanner.repository.RoleRepository;
import com.khumbu.dailyplanner.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.khumbu.dailyplanner.constants.DailyPlannerConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JWTService jwtService;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public ApiResponseDto<UserDto> signUp(UserDto userDto) {
        LOGGER.info("Inside signUp");
        Optional<Users> usersOptional = userRepository.findById(userDto.getEmail());
        if (usersOptional.isPresent()) {
            throw new DayException("User with " + " " + userDto.getEmail() + " already exists.");
        }
        List<Role> roles = userDto.getRoles();
        if (roles == null) {
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
                .roles(userDto.getRoles() != null ? userDto.getRoles() : roles)
                .build();
        List<Role> rolesToSave = new ArrayList<>();

        for(Role roleFromList : roles){
            Role role = roleRepository.findByName(roleFromList.getName());
            if(role != null){
                rolesToSave.add(role);

            }else{
                Role savedRole = roleRepository.save(roleFromList);
                rolesToSave.add(savedRole);
            }
        }
        user.setRoles(rolesToSave);
        Users savedUser = userRepository.save(user);

        userDto.setPassword(savedUser.getPassword());
        ApiResponseDto<UserDto> responseDto = new ApiResponseDto<>();
        responseDto.setHttpStatus(HttpStatus.OK);
        responseDto.setMessage("Registration successful");
        responseDto.setTime(LocalDateTime.now());
        UserDto savedDto = new UserDto(savedUser.getEmail(), savedUser.getFirstname(), savedUser.getLastname(), savedUser.getPhone(), null, savedUser.getRoles());
        responseDto.setData(savedDto);
        logger.info("User saved : {} ", savedUser);
        LOGGER.info("End signUp");
        return responseDto;
    }

    private void validateUserDto(UserDto userDto) {
        if (ObjectUtils.isEmpty(userDto.getEmail()) ||
                ObjectUtils.isEmpty(userDto.getFirstname()) ||
                ObjectUtils.isEmpty(userDto.getLastname()) ||
                ObjectUtils.isEmpty(userDto.getPassword())) {
            logger.error(DailyPlannerConstants.FIELDS_MISSING);
            throw new DayException(DailyPlannerConstants.FIELDS_MISSING);
        }
    }

    private void validateUsername(UserDto userDto) {
        Optional<Users> usersOptional = userRepository.findById(userDto.getEmail());
        if (!usersOptional.isEmpty()) {
            throw new DayException(DailyPlannerConstants.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
    }

    public Users generateUser(UserDto userDto) {
        Users user = new Users(userDto.getEmail(), userDto.getFirstname(), userDto.getLastname(), userDto.getPhone(), userDto.getPassword());
        return user;
    }

    public ApiResponseDto<AuthenticationResponseDto> authenticate(LoginDto userInfo) {
        LOGGER.info("Inside authenticate");
        ApiResponseDto<AuthenticationResponseDto> responseDto = new ApiResponseDto<>();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userInfo.getEmail(), userInfo.getPassword()));
        responseDto.setTime(LocalDateTime.now());

        if (authentication.isAuthenticated()) {
            AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
            Users user = userRepository.findById(userInfo.getEmail()).orElseThrow(() -> new DailyPlannerException("User with email " + userInfo.getEmail() + " not found"));

            //set refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfo.getEmail());
            authenticationResponseDto.setEmail(userInfo.getEmail());
            String string = jwtService.generateToken(userInfo.getEmail());
            Date expirationDate = jwtService.extractExpiration(string);
            authenticationResponseDto.setToken(string);
            authenticationResponseDto.setTokenExpirationDate(expirationDate.getTime());
            authenticationResponseDto.setRefreshToken(refreshToken);
            authenticationResponseDto.setFullName(user.getFirstname() + " " + user.getLastname());
            authenticationResponseDto.setRoles(refreshToken.getUser().getRoles());
            responseDto.setHttpStatus(HttpStatus.OK);
            responseDto.setMessage("Success");
            responseDto.setToken(string);
            responseDto.setData(authenticationResponseDto);


            return responseDto;
        }

        responseDto.setHttpStatus(HttpStatus.UNAUTHORIZED);
        responseDto.setMessage("Fail");
        responseDto.setData(null);
        LOGGER.info("End authenticate");
        return responseDto;

    }

    public String logout(String email) {

        Users user = userRepository.findById(email).orElseThrow(() -> new DailyPlannerException("User with username " + email + " was not found"));
        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.deleteByEmail(email);
        if (refreshTokenOptional.isPresent()) {
            return "DELETED";
        } else {
            return "NO TOKEN FOUND";
        }
    }

    public List<UserDto> getAll() {

        List<Users> users = userRepository.findAll();
        return users.stream().map(user -> UserDto
                .builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .isLocked(user.isLocked())
                .isDisabled(user.isDisabled())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .roles(user.getRoles()).build()).toList();
    }

    public UserDto getById(String email) {
        Users user = userRepository.findById(email).orElseThrow(() -> new DailyPlannerException("User with ID " + " not found"));

        return UserDto.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .isLocked(user.isLocked())
                .isDisabled(user.isDisabled())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .roles(user.getRoles()).build();
    }

    public UserDto deleteById(String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new DailyPlannerException("User with ID " + email + " not found"));

       UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .isLocked(user.isLocked())
                .isDisabled(user.isDisabled())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .roles(user.getRoles()).build();
       LOGGER.info("*************User dto {}", userDto);
        refreshTokenService.deleteByEmail(email);
        userRepository.delete(user);
        return userDto;
    }

    private void validatePassword(String password){
        if(ObjectUtils.isEmpty(password)){
            throw new DailyPlannerException("Password cannot be empty");
        }
    }

    @Transactional
    public UserDto update(UserDto userDto) {

        //validatePassword(userDto.getPassword());
        Role role = roleRepository.findByName("USER");
        Users userFromDb = userRepository.findById(userDto.getEmail()).orElseThrow(() -> new DailyPlannerException("User with id " + userDto.getEmail() + " not found"));

        userFromDb.setEmail(userDto.getEmail());
        userFromDb.setFirstname(userDto.getFirstname());
        userFromDb.setLastname(userDto.getLastname());
        userFromDb.setPhone(userDto.getPhone());
        userFromDb.setDisabled(userDto.isDisabled());
        userFromDb.setLocked(userDto.isLocked());


        if (!encoder.matches(userDto.getPassword(), userFromDb.getPassword()) && userDto.getPassword().length() > 6) {
            userFromDb.setPassword(encoder.encode(userDto.getPassword()));
        };

        if(userDto.getRoles() != null){
            userFromDb.setRoles(userDto.getRoles());
        }

        if(!userFromDb.getRoles().contains(role)){
            userFromDb.getRoles().add(role);
        }
        Users savedUser = userRepository.findById(userDto.getEmail()).orElseThrow(() -> new DailyPlannerException("User with id " + userDto.getEmail() + " not found"));

        return UserDto.builder().email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .isDisabled(savedUser.isDisabled())
                .firstname(savedUser.getFirstname())
                .lastname(savedUser.getLastname())
                .isLocked(savedUser.isLocked())
                .roles(savedUser.getRoles())
                .build();
    }
}
