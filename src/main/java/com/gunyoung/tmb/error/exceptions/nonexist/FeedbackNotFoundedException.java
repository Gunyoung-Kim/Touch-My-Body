package com.gunyoung.tmb.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class FeedbackNotFoundedException extends EntityNotFoundedException {

	public FeedbackNotFoundedException(String msg) {
		super(msg);
	}

}
