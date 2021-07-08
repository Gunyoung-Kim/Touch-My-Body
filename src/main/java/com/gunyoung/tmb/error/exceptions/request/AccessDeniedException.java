package com.gunyoung.tmb.error.exceptions.request;

@SuppressWarnings("serial")
public class AccessDeniedException extends RequestException {

	public AccessDeniedException(String msg) {
		super(msg);
	}

}
