package com.gunyoung.tmb.error.exceptions.duplication;

@SuppressWarnings("serial")
public class EmailDuplicationFoundedException extends DuplicationException {

	public EmailDuplicationFoundedException(String msg) {
		super(msg);
	}

}
