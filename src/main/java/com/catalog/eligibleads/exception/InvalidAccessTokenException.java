package com.catalog.eligibleads.exception;

public class InvalidAccessTokenException extends RuntimeException {

	private static final long serialVersionUID = -8720099387077592429L;

	public InvalidAccessTokenException() {
		super("Access Token invalid!");
	}

}
