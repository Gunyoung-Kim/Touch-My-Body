package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum ExercisePostErrorCode {
	
	ExercisePostNotFoundedError("EP001","Exercise's post is not founded");
	
	private String code;
	private String description;
	private ExercisePostErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
	
}
