package com.gunyoung.tmb.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class CommentNotFoundedException extends EntityNotFoundedException {

	public CommentNotFoundedException(String msg) {
		super(msg);
	}

}
