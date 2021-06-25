package com.gunyoung.tmb.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class ExercisePostNotFoundedException extends EntityNotFoundedException {

	public ExercisePostNotFoundedException(String msg) {
		super(msg);
	}

}
