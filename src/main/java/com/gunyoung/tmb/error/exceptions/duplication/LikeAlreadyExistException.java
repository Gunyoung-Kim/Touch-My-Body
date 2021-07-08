package com.gunyoung.tmb.error.exceptions.duplication;

@SuppressWarnings("serial")
public class LikeAlreadyExistException extends DuplicationException {

	public LikeAlreadyExistException(String msg) {
		super(msg);
	}

}
