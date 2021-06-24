package com.gunyoung.tmb.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class MuscleNotFoundedException extends EntityNotFoundedException {

	public MuscleNotFoundedException(String msg) {
		super(msg);
	}

}
