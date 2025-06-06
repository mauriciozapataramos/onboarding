package com.jmzr.onboarding.dto;

import com.jmzr.onboarding.model.ErrorResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AuthResponse extends ErrorResponse {
	private String token;

}
