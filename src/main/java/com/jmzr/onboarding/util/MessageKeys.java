package com.jmzr.onboarding.util;

import org.springframework.stereotype.Component;

@Component
public class MessageKeys {

	private MessageKeys() {}

    public static final String AUTH_SUCCESS = "error.auth.success";
    public static final String AUTH_BAD_CREDENTIALS = "error.auth.failed";
    public static final String AUTH_ACCESS_DENIED = "error.auth.denied";
    public static final String AUTH_USER_NOT_FOUND = "error.auth.failed";
    public static final String AUTH_INTERNAL_ERROR = "error.auth.internal";
}
