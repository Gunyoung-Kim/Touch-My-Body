package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum FeedbackErrorCode {
	
	FEEDBACK_NOT_FOUNDED_ERROR("F001","Feedback is not founded")
	
	;
	private String code;
	private String description;
	private FeedbackErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
