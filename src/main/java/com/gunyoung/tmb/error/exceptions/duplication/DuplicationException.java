package com.gunyoung.tmb.error.exceptions.duplication;

import com.gunyoung.tmb.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class DuplicationException extends BusinessException{

	public DuplicationException(String msg) {
		super(msg);
	}

}
