package com.andrej911.bankcardsystem.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
	
	Instant timestamp,
	int status,
	String error,
	String message,
	String path,
	Map <String, String> fieldErrors) {
		
	public static ApiError of(HttpStatusCode status, String message, String path) {
		return new ApiError(Instant.now(), status.value(), reasonPhrase(status), message, path, null);
	}
	
	public static ApiError validation(String path, Map<String, String> fieldErrors) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), 
				"Validation failed", path, fieldErrors);
	}
	private static String reasonPhrase(HttpStatusCode status) {
		HttpStatus resolved = HttpStatus.resolve(status.value());
		return resolved != null ? resolved.getReasonPhrase() : "Unknown";
	}

}
