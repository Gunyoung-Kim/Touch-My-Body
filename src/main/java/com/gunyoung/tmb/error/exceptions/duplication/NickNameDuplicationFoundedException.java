package com.gunyoung.tmb.error.exceptions.duplication;

@SuppressWarnings("serial")
public class NickNameDuplicationFoundedException extends DuplicationException {

	public NickNameDuplicationFoundedException(String msg) {
		super(msg);
	}

}
