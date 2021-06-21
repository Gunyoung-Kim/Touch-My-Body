package com.gunyoung.tmb.error.exceptions.nonexist;

import com.gunyoung.tmb.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class EntityNotFoundExceptions extends BusinessException {

	public EntityNotFoundExceptions(String msg) {
		super(msg);
	}

}
