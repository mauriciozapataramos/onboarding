package com.jmzr.onboarding.controller;

import com.jmzr.onboarding.dto.AuthRequest;
import com.jmzr.onboarding.dto.AuthResponse;
import com.jmzr.onboarding.model.ErrorResponse;
import com.jmzr.onboarding.service.AuthService;
import com.jmzr.onboarding.util.MessageKeys;
import com.jmzr.onboarding.util.MessageUtil;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final MessageUtil message;

	public AuthController(AuthService authService, MessageUtil message) {
		this.authService = authService;
		this.message = message;
	}

	@PostMapping("/login")
	public ResponseEntity<? extends ErrorResponse> login(@Valid @RequestBody AuthRequest request){
		try {
			String token = authService.login(request.getUsername(), request.getPassword());

			return ResponseEntity.ok(AuthResponse.builder().token(token).code(message.getCode(MessageKeys.AUTH_SUCCESS))
					.message(message.getMessage(MessageKeys.AUTH_SUCCESS)).build());

		} 
		catch(UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(ErrorResponse.builder().code(message.getCode(MessageKeys.AUTH_USER_NOT_FOUND))
							.message(e.getMessage()).build());
		}
		catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ErrorResponse.builder().code(message.getCode(MessageKeys.AUTH_PASSWORD_INCORRECT))
							.message(e.getMessage()).build());

		}
	}
}
