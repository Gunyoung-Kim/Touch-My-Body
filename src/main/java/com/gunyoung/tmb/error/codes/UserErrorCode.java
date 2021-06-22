package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum UserErrorCode {
	
	UserNotFoundedError("U001","Can't find user")
	;
	private String code;
	private String description;
	private UserErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
