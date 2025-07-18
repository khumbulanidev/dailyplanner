package com.khumbu.dailyplanner.config;

import com.khumbu.dailyplanner.filter.JwtFilter;
import com.khumbu.dailyplanner.service.DailyPlannerUserDetailsService;
import com.khumbu.dailyplanner.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private JwtFilter jwtFilter;
//    @Autowired
//    private DailyPlannerUserDetailsService dailyPlannerUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(cs ->cs.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/sign-up", "/api/v1/users/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration, DailyPlannerUserDetailsService dailyPlannerUserDetailsService) throws Exception {
        //DaoAuthenticationProvider used for database authentication. The Authentication provider will connect to a Database
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //NoOpPasswordEncoder is the default encoder meaning no encoder will be used

        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(dailyPlannerUserDetailsService);

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder(12);
    }

    //UserDetailsService is an interface you will need to find a suitable implementation or create your own
    //User implements userDetails
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails userDetails = User.withDefaultPasswordEncoder().username().password();
//        return new InMemoryUserDetailsManager();
//    }

}
