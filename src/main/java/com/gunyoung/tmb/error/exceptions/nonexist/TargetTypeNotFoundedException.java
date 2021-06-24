package com.gunyoung.tmb.error.exceptions.nonexist;

import com.gunyoung.tmb.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class TargetTypeNotFoundedException extends BusinessException {

	public TargetTypeNotFoundedException(String msg) {
		super(msg);
	}

}
