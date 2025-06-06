package com.jmzr.onboarding.service;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import com.jmzr.onboarding.security.JwtService;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager am, JwtService js) {
        this.authenticationManager = am;
        this.jwtService = js;
    }

    public String login(String username, String password) {
       
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
		);
        
        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user.getUsername(), user.getAuthorities());
     
        return token;
    }

}
