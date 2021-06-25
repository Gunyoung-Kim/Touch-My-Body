package com.gunyoung.tmb.error.exceptions.notmatch;

import com.gunyoung.tmb.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class UserNotMatchException extends BusinessException {

	public UserNotMatchException(String msg) {
		super(msg);
	}

}
