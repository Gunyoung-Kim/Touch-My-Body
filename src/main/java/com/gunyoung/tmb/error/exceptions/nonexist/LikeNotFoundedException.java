package com.gunyoung.tmb.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class LikeNotFoundedException extends EntityNotFoundedException {

	public LikeNotFoundedException(String msg) {
		super(msg);
	}

}
