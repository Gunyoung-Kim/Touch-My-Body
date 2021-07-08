package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum LikeErrorCode {
	
	LIKE_NOT_FOUNDED_ERROR("LK001","Like is not founded!"),
	LIKE_ALREADY_EXIST_ERROR("LK002","Like for user and target already exist"),
	;
	private String code;
	private String description;
	private LikeErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
