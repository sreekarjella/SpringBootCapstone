package com.eatz.reviewservice.exception;

public class InvalidReviewException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidReviewException(String msg) {
		super(msg);
	}

}
