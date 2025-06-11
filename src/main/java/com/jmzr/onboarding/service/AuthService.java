package com.jmzr.onboarding.service;

import com.jmzr.onboarding.model.UserEntity;
import com.jmzr.onboarding.repository.UserRepository;
import com.jmzr.onboarding.security.JwtService;
import com.jmzr.onboarding.util.MessageKeys;
import com.jmzr.onboarding.util.MessageUtil;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final MessageUtil message;

	public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService,
			MessageUtil message) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.message = message;
	}

	public String login(String username, String password) {

		UserEntity userEntity = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(message.getMessage(MessageKeys.AUTH_USER_NOT_FOUND)));

		if (!passwordEncoder.matches(password, userEntity.getPassword())) {
			throw new BadCredentialsException(message.getMessage(MessageKeys.AUTH_PASSWORD_INCORRECT));
		}

		Collection<GrantedAuthority> authorities = userEntity.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());

		String token = jwtService.generateToken(userEntity.getUsername(), authorities);

		return token;
	}

	public List<String> getRoles(String token) {
		return jwtService.extractRoles(token);
	}
	
	public String username(String token) {
		return jwtService.extractUsername(token);
	}
}
