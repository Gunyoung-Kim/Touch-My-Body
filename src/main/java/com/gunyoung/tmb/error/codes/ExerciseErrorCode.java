package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum ExerciseErrorCode {
	
	EXERCISE_BY_NAME_NOT_FOUNDED_ERROR("E001","Can't find exercise with such name"),
	EXERCISE_BY_ID_NOT_FOUNDED_ERROR("E002","Can't find exercise with such Id"),
	EXERCISE_NAME_DUPLICATION_ERROR("E003", "Such exercise name already exist")
	;
	private String code;
	private String description;
	private ExerciseErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
