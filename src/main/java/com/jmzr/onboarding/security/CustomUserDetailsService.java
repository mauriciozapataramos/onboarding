package com.jmzr.onboarding.security;


import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.jmzr.onboarding.model.UserEntity;
import com.jmzr.onboarding.repository.UserRepository;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                user.getRoles().stream()
                    .map(r -> new SimpleGrantedAuthority(r.getRole()))
                    .collect(Collectors.toSet())
        );
       
    }
}
