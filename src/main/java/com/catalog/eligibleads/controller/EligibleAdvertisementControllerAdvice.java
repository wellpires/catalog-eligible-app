package com.catalog.eligibleads.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.catalog.eligibleads.builder.ErrorResponseBuilder;
import com.catalog.eligibleads.response.ErrorResponse;

@RestControllerAdvice
public class EligibleAdvertisementControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(EligibleAdvertisementControllerAdvice.class);

	@Value("${exception.general.error-message}")
	private String errorMessage;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {

		logger.error(errorMessage, ex);

		ErrorResponse errorResponse = new ErrorResponseBuilder().message(errorMessage).build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
			MissingServletRequestParameterException ex) {
		logger.error(ex.getMessage(), ex);
		ErrorResponse errorResponse = new ErrorResponseBuilder().message(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

}
