package com.gunyoung.tmb.error.exceptions.nonexist;

import com.gunyoung.tmb.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class SessionAttributesNotFoundedException extends BusinessException {

	public SessionAttributesNotFoundedException(String msg) {
		super(msg);
	}
	
}
