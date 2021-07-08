package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum MuscleErrorCode {
	
	MUSCLE_NOT_FOUNDED_ERROR("M001","Muscle is not founded"),
	MUSCLE_NAME_DUPLICATION_FOUNDED_ERROR("M002","Duplication for muscle name is founded")
	;
	private String code;
	private String description;
	private MuscleErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
