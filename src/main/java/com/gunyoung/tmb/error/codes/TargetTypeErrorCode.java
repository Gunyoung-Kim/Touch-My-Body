package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum TargetTypeErrorCode {
	
	TargetTypeNotFoundedError("T001","There is no such Target Type")
	
	;
	private String code;
	private String description;
	private TargetTypeErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}

