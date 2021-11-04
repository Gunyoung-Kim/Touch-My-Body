package com.gunyoung.tmb.precondition;

public class PreconditionViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PreconditionViolationException(String message) {
		super(message);
	}
}
