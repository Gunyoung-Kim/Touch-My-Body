package com.gunyoung.tmb.error.exceptions.nonexist;

@SuppressWarnings("serial")
public class ExerciseNotFoundedException extends EntityNotFoundedException {

	public ExerciseNotFoundedException(String msg) {
		super(msg);
	}

}
