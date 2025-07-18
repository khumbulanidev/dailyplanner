package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.models.DailyPlannerUserDetails;
import com.khumbu.dailyplanner.models.Users;
import com.khumbu.dailyplanner.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DailyPlannerUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyPlannerUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Inside loadUserByUsername method.");
       Users user =  userRepository.findById(username).orElseThrow(()-> {
           LOGGER.error("User with username {} not found", username);
         return  new DailyPlannerException("User with username "+ username+ " not found");
       }
       );
       LOGGER.info(user.getEmail()+" was loaded successfully.");
       UserDetails userDetails = new DailyPlannerUserDetails(user);
        LOGGER.info("End  loadUserByUsername method.");
        return userDetails;
    }
}
