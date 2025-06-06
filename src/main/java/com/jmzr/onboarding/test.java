package com.jmzr.onboarding;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method 
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "admin123";  // <- cambia esto por la contraseña que quieras
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
		System.out.println(encoder.matches("admin123", encodedPassword));


	}

}
