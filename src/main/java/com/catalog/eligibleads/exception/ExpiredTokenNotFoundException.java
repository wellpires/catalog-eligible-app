package com.catalog.eligibleads.exception;

public class ExpiredTokenNotFoundException extends Exception {

	private static final long serialVersionUID = 7659923105133764027L;

	public ExpiredTokenNotFoundException() {
		super("Expired Token not found!");
	}

}
