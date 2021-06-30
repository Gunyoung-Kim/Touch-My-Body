package com.gunyoung.tmb.error.exceptions.request;

import com.gunyoung.tmb.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class RequestException extends BusinessException {

	public RequestException(String msg) {
		super(msg);
	}

}
