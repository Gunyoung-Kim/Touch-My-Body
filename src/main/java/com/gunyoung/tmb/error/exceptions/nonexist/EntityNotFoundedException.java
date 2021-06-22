package com.gunyoung.tmb.error.exceptions.nonexist;

import com.gunyoung.tmb.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class EntityNotFoundedException extends BusinessException {

	public EntityNotFoundedException(String msg) {
		super(msg);
	}

}
