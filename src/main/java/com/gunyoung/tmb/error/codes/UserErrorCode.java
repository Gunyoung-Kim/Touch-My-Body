package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum UserErrorCode {
	
	UserNotFoundedError("U001","Can't find user"),
	UserNotMatchError("U002", "Request user is not match to owner of source"),
	RoleNotFoundedError("U003", "Such role is not founded")
	;
	private String code;
	private String description;
	private UserErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
