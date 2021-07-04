package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum ExerciseErrorCode {
	
	ExerciseByNameNotFoundedError("E001","Can't find exercise with such name"),
	ExerciseByIdNotFoundedError("E002","Can't find exercise with such Id"),
	ExerciseNameDuplicatedError("E003", "Such exercise name already exist")
	;
	private String code;
	private String description;
	private ExerciseErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
