package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum TargetTypeErrorCode {
	
	TARGET_TYPE_NOT_FOUNDED_ERROR("T001","There is no such Target Type")
	
	;
	private String code;
	private String description;
	private TargetTypeErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}

