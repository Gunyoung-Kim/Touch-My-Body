package com.gunyoung.tmb.error.exceptions;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException{
	public BusinessException(String msg) {
		super(msg);
	}
}
