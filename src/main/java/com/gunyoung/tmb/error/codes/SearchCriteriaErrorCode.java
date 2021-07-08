package com.gunyoung.tmb.error.codes;

import lombok.Getter;

@Getter
public enum SearchCriteriaErrorCode {
	
	ORDER_BY_CRITERIA_ERROR("S001","Criteria for order by is unvalid")
	;
	private String code;
	private String description;
	private SearchCriteriaErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
