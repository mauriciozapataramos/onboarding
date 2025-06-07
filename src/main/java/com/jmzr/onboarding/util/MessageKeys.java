package com.jmzr.onboarding.util;

import org.springframework.stereotype.Component;

@Component
public class MessageKeys {

	private MessageKeys() {}

    public static final String AUTH_SUCCESS = "error.auth.success";
    public static final String AUTH_BAD_CREDENTIALS = "error.auth.failed";
    public static final String AUTH_ACCESS_DENIED = "error.auth.denied";
    public static final String AUTH_USER_NOT_FOUND = "error.auth.notfound";
    public static final String AUTH_INTERNAL_ERROR = "error.auth.internal";
    public static final String AUTH_RUTA_NO_EXISTE = "error.auth.ruta";
    public static final String AUTH_BAD_STRUCTURE_CODE = "error.auth.bad.structure.body";
	public static final String AUTH_BODY_EMPTY_CODE = "error.auth.body.empy";
}
