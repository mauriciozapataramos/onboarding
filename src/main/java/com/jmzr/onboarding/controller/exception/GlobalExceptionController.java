package com.jmzr.onboarding.controller.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.jmzr.onboarding.model.ErrorResponse;
import com.jmzr.onboarding.model.FieldErrorDetail;
import com.jmzr.onboarding.util.MessageKeys;
import com.jmzr.onboarding.util.MessageUtil;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
		log.warn("Validation error: {}", ex.getMessage());
		List<FieldErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> FieldErrorDetail.builder().field(fieldError.getField())
						.rejectedValue(
								fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : null)
						.message(fieldError.getDefaultMessage()).build())
				.collect(Collectors.toList());

		ErrorResponse errorResponse = ErrorResponse.builder().code(message.getCode(MessageKeys.ERROR_ARGUMENT_NO_VALID))
				.message(message.getMessage(MessageKeys.ERROR_ARGUMENT_NO_VALID)).errors(errors).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn("JSON parse error: {}", ex.getMessage());
		Throwable cause = ex.getMostSpecificCause();
		String messageToShow;
		String codigo = "";

		if (cause instanceof UnrecognizedPropertyException unrecognizedEx) {
			messageToShow = String.format(message.getMessage(MessageKeys.ERROR_CAMPO_NO_RECONOCIDO),
					unrecognizedEx.getPropertyName());
			codigo = message.getCode(MessageKeys.ERROR_CAMPO_NO_RECONOCIDO);

		} else if (cause instanceof InvalidFormatException invalidFormatEx) {
			String fieldPath = invalidFormatEx.getPath().get(invalidFormatEx.getPath().size() - 1).getFieldName();

			String invalidValue = invalidFormatEx.getValue() != null ? invalidFormatEx.getValue().toString()
					: "valor desconocido";
			String expectedType = invalidFormatEx.getTargetType().getSimpleName();

			messageToShow = String.format(message.getMessage(MessageKeys.ERROR_CAMPO_INVALIDO), fieldPath, invalidValue,
					expectedType);
			codigo = message.getCode(MessageKeys.ERROR_CAMPO_INVALIDO);

		} else if (cause instanceof MismatchedInputException) {
			messageToShow = message.getMessage(MessageKeys.ERROR_JSON_MAL_FORMADO);
			codigo = message.getCode(MessageKeys.ERROR_JSON_MAL_FORMADO);

		} else if (cause instanceof JsonParseException) {
			messageToShow = message.getMessage(MessageKeys.ERROR_JSON_INVALIDO);
			codigo = message.getCode(MessageKeys.ERROR_JSON_INVALIDO);

		} else {
			messageToShow = "Error al procesar el JSON: " + cause.getMessage();
		}

		ErrorResponse errorResponse = ErrorResponse.builder().code(codigo).message(messageToShow).errors(List.of()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// validacion manual o logica especifica
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		log.warn("Illegal argument: {}", ex.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder().code(message.getCode(MessageKeys.ERROR_BAD_STRUCTURE_CODE))
				.message(ex.getMessage()).errors(List.of()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
		log.warn("Constraint violation: {}", ex.getMessage());

		// CAMBIO: Usamos AUTH_BAD_STRUCTURE_CODE para errores de validación de
		// parámetros
		ErrorResponse errorResponse = ErrorResponse.builder().code(message.getCode(MessageKeys.ERROR_CONSTRAINT_VIOLATION))
				.message(message.getMessage(MessageKeys.ERROR_CONSTRAINT_VIOLATION))
				.errors(ex.getConstraintViolations().stream()
						.map(v -> FieldErrorDetail.builder().field(v.getPropertyPath().toString())
								.rejectedValue(v.getInvalidValue() != null ? v.getInvalidValue().toString() : null)
								.message(v.getMessage()).build())
						.collect(Collectors.toList()))
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		log.error("Unexpected error: ", ex);
		ErrorResponse errorResponse = ErrorResponse.builder().code(message.getCode(MessageKeys.ERROR_INTERNAL_ERROR))
				.message(message.getMessage(MessageKeys.ERROR_INTERNAL_ERROR)).errors(List.of()).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
