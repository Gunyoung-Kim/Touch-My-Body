package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum ProgramErrorCode {
	
	PRECONDITION_VIOLATION_ERROR("P001", "precondition violation occurred");
	
	private String code;
	private String description;
	private ProgramErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
