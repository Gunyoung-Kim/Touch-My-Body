package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum CommentErrorCode {
	
	COMMENT_NOT_FOUNDED_ERROR("C001","Can't find such comment")
	
	;
	private String code;
	private String description;
	private CommentErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
