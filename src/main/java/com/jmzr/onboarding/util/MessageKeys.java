package com.jmzr.onboarding.util;

import org.springframework.stereotype.Component;

@Component
public class MessageKeys {

	private MessageKeys() {}

    public static final String AUTH_SUCCESS = "error.auth.success";
    public static final String AUTH_PASSWORD_INCORRECT = "error.auth.password";
    public static final String AUTH_USER_NOT_FOUND = "error.auth.notfound";
    public static final String ERROR_INTERNAL_ERROR = "error.internal";
    public static final String ERROR_RUTA_NO_EXISTE = "error.ruta";
    public static final String ERROR_BAD_STRUCTURE_CODE = "error.bad.structure";
	public static final String ERROR_ARGUMENT_NO_VALID = "error_arg_no_valid";
	public static final String ERROR_CAMPO_NO_RECONOCIDO = "error_campo_no_reconocido";
	public static final String ERROR_CAMPO_INVALIDO = "error_campo_invalido";
	public static final String ERROR_JSON_MAL_FORMADO = "error_json_mal_formado";
	public static final String ERROR_JSON_INVALIDO = "error_campo_invalido";
	public static final String ERROR_CONSTRAINT_VIOLATION ="error_constraint_violation";
}
