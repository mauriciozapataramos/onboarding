package com.jmzr.onboarding.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.jmzr.onboarding.model.ErrorResponse;
import com.jmzr.onboarding.util.MessageKeys;
import com.jmzr.onboarding.util.MessageUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class FallbackController {
	private final MessageUtil message;

	public FallbackController(MessageUtil message) {
		this.message = message;
	}

	@RequestMapping("/**")
	public ResponseEntity<ErrorResponse> fallback(HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ErrorResponse.builder().code(message.getCode(MessageKeys.AUTH_RUTA_NO_EXISTE))
						.message(message.getMessage(MessageKeys.AUTH_RUTA_NO_EXISTE)).build());
	}
}
