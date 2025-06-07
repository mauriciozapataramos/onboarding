package com.jmzr.onboarding.controller.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.jmzr.onboarding.model.ErrorResponse;
import com.jmzr.onboarding.util.MessageKeys;
import com.jmzr.onboarding.util.MessageUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

	private final MessageUtil message;

	public GlobalExceptionController(MessageUtil message) {
		this.message = message;
	}

	// para anotacion de validacion
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		String errorMsg = ex.getBindingResult().getFieldErrors().stream()
				.map(field -> field.getField() + ": " + field.getDefaultMessage()).collect(Collectors.joining(", "));

		ErrorResponse errorResponse = ErrorResponse.builder().code(message.getCode(MessageKeys.AUTH_BAD_STRUCTURE_CODE))
				.message(errorMsg).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Throwable cause = ex.getMostSpecificCause();
		String messageToShow;

		if (cause instanceof UnrecognizedPropertyException unrecognizedEx) {
			messageToShow = String.format(
					"El campo \"%s\" no está definido en el modelo. Verifica los nombres de los campos enviados.",
					unrecognizedEx.getPropertyName());

		} else if (cause instanceof InvalidFormatException invalidFormatEx) {
			String fieldPath = StringUtils.substringBetween(invalidFormatEx.getPathReference(),"[\"", "\"]");
					invalidFormatEx.getPathReference();
			String invalidValue = invalidFormatEx.getValue() != null ? invalidFormatEx.getValue().toString()
					: "valor desconocido";
			String expectedType = invalidFormatEx.getTargetType().getSimpleName();

			messageToShow = String.format(
					"El campo '%s' recibió un valor inválido: '%s'. Se esperaba un dato de tipo '%s'.", fieldPath,
					invalidValue, expectedType);

		} else if (cause instanceof MismatchedInputException) {
			messageToShow = "Estructura del JSON incorrecta. Verifica que los tipos de datos y estructuras coincidan con lo esperado.";

		} else if (cause instanceof JsonParseException) {
			messageToShow = "El cuerpo del request contiene un JSON mal formado. Verifica la sintaxis.";

		} else {
			messageToShow = "Error al procesar el cuerpo del request: " + cause.getMessage();
		}

		ErrorResponse errorResponse = ErrorResponse.builder().code(message.getCode(MessageKeys.AUTH_BAD_STRUCTURE_CODE))
				.message(messageToShow).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// captura errores que no hayan sido manejado por otros metodos
	@ExceptionHandler(UnrecognizedPropertyException.class)
	public ResponseEntity<ErrorResponse> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex) {
		ErrorResponse errorResponse = ErrorResponse.builder().code(message.getCode(MessageKeys.AUTH_BAD_STRUCTURE_CODE))
				.message("JSON contiene campos no válidos: " + ex.getPropertyName()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// validacion manual o logica especifica
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ErrorResponse errorResponse = ErrorResponse.builder().code(message.getCode(MessageKeys.AUTH_BAD_STRUCTURE_CODE))
				.message("Argumento inválido: " + ex.getMessage()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		ErrorResponse errorResponse = ErrorResponse.builder().code(message.getCode(MessageKeys.AUTH_INTERNAL_ERROR))
				.message(message.getMessage(MessageKeys.AUTH_INTERNAL_ERROR)).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
