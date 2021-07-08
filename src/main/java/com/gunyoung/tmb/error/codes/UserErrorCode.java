package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum UserErrorCode {
	
	USER_NOT_FOUNDED_ERROR("U001","Can't find user"),
	USER_NOT_MATCH_ERROR("U002", "Request user is not match to owner of source"),
	ROLE_NOT_FOUNDED_ERROR("U003", "Such role is not founded"),
	SESSION_ATTRIBUTES_NOT_FOUNDED_ERROR("U004", "Login ID Session nonexist"),
	ACESS_DENIED_ERROR("U005", "Cause of you authority, access denied")
	;
	private String code;
	private String description;
	private UserErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
