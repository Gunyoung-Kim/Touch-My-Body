package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum PrivacyPolicyErrorCode {
	
	PRIVACY_NOT_FOUNDED_ERROR("PP001","Such privacy policy is not founded")
	;
	private String code;
	private String description;
	private PrivacyPolicyErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
