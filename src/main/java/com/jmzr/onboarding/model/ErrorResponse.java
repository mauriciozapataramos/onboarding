package com.jmzr.onboarding.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ErrorResponse {
	
	private String code;
	private String message; 
	private List<FieldErrorDetail> errors;
	private List<String> roles;
	private String sub;
	
}
