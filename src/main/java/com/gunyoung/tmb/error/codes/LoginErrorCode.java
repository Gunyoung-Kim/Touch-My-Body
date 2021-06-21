package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum LoginErrorCode {
	
	UnrecognizedRole("L001","Role is Unrecognized");
	
	private String code;
	private String description;
	
	private LoginErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
