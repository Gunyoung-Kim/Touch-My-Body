package com.gunyoung.tmb.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class UserNotFoundedException extends EntityNotFoundedException{

	public UserNotFoundedException(String msg) {
		super(msg);
	}

}
