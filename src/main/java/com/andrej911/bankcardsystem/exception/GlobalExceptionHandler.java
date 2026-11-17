package com.andrej911.bankcardsystem.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleAlreadyExists(
			ResourceAlreadyExistsException ex, WebRequest request) {
		log.warn("Conflict at {}: {}", path(request), ex.getMessage());
		return build(HttpStatus.CONFLICT, ex.getMessage(), request);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> handleBadCredentials(
			BadCredentialsException ex, WebRequest request) {
		return build(HttpStatus.UNAUTHORIZED, "Invalid username or password", request);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> handleAccessDenied(
			AccessDeniedException ex, WebRequest request) {
		return build(HttpStatus.FORBIDDEN, "Access denied", request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleDataIntegrity(
			DataIntegrityViolationException ex, WebRequest request) {
		log.warn("Data integrity violation at {}", path(request), ex);
		return build(HttpStatus.CONFLICT, "Operation conflicts with existing data", request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleUnexpected(
			Exception ex, WebRequest request) {
		log.error("Unexpected error at {}", path(request), ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
	}

	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		
		Map<String, String> fieldErrors = new LinkedHashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {

			fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ApiError.validation(path(request), fieldErrors));
	}

	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		log.warn("Unreadable request body at {}: {}", path(request), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ApiError.of(HttpStatus.BAD_REQUEST, "Malformed JSON request", path(request)));
	}

	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {

		String message = "Request failed";
		
		if (ex instanceof org.springframework.web.ErrorResponse springError
				&& springError.getBody().getDetail() != null) {
			message = springError.getBody().getDetail();
		}

		ApiError apiError = ApiError.of(statusCode, message, path(request));
		return super.handleExceptionInternal(ex, apiError, headers, statusCode, request);
	}

	
	private ResponseEntity<ApiError> build(HttpStatus status, String message, WebRequest request) {
		return ResponseEntity.status(status).body(ApiError.of(status, message, path(request)));
	}

	
	private static String path(WebRequest request) {
		if (request instanceof ServletWebRequest servletWebRequest) {
			return servletWebRequest.getRequest().getRequestURI();
		}
		return null; 
	}
	
	@Override
	protected ResponseEntity<Object> handleNoResourceFoundException(
	        NoResourceFoundException ex, HttpHeaders headers,
	        HttpStatusCode status, WebRequest request) {

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(ApiError.of(HttpStatus.NOT_FOUND, "Resource not found", path(request)));
	}
}
