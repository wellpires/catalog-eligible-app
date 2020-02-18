package com.catalog.eligibleads.exception;

public class InvalidAccessTokenException extends RuntimeException {

	private static final String ACCESS_TOKEN_INVALID = "Access Token for %s account invalid!";
	private static final long serialVersionUID = -8720099387077592429L;

	public InvalidAccessTokenException(String nameAccount) {
		super(String.format(ACCESS_TOKEN_INVALID, nameAccount));
	}

}
