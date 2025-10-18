package com.khumbu.dailyplanner.filter;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.exceptions.DayException;
import com.khumbu.dailyplanner.service.DailyPlannerUserDetailsService;
import com.khumbu.dailyplanner.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.logging.LogManager;

@Component

public class JwtFilter  extends OncePerRequestFilter {

    private Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ApplicationContext applicationContext;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        //client will send bearer token
        //extract it and get the token
        //Bearer TOKENGOESHERE
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        try{
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                token = authHeader.substring(7); //substring from  7 becuase Bearer and space is 6 characters
                if(token.contains("null")){
                    throw new AuthenticationException("Request missing token ");
                }
                username = jwtService.extractUsername(token);
            }
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                // use a custom details service as using a default one will result in null
                UserDetails userDetails = applicationContext.getBean(DailyPlannerUserDetailsService.class).loadUserByUsername(username);
                if(jwtService.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //?
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                else{
                    //create a new response
                    //throw an excepction

                    throw new DailyPlannerException("Token expired ", HttpStatus.UNAUTHORIZED);
                }
            }
            filterChain.doFilter(request,response);
        }
        catch (AuthenticationException authenticationException){
            //set the status code
            LOGGER.error(" Exception thrown {} " , authenticationException.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        catch (Exception exception){
            LOGGER.error(" Exception thrown {} " , exception.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().println(exception.getMessage());
            //throw an exception that will be handled
            if(exception.getMessage().contains("JWT expired")){
                throw new DayException(exception.getMessage() , HttpStatus.UNAUTHORIZED);
            }

        }


    }
}
