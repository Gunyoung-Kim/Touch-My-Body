package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum MuscleErrorCode {
	
	MuscleNotFoundedError("M001","Muscle is not founded"),
	MuscleNameDuplicationFoundedError("M002","Duplication for muscle name is founded")
	;
	private String code;
	private String description;
	private MuscleErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
