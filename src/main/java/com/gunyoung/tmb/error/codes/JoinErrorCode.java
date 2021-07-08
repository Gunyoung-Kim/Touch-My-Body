package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum JoinErrorCode {
	
	EMAIL_DUPLICATION_FOUNDED_ERROR("J001","Email duplication is founded"),
	NICKNAME_DUPLICATION_FOUNDED_ERROR("J002","Nickname duplication is founded")
	
	;
	private String code;
	private String description;
	
	
	private JoinErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
