package com.gunyoung.tmb.error.exceptions.nonexist;

import com.gunyoung.tmb.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class RoleNotFoundedException extends BusinessException {

	public RoleNotFoundedException(String msg) {
		super(msg);
	}

}
