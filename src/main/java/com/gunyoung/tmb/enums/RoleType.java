package com.gunyoung.tmb.enums;

import lombok.Getter;

@Getter
public enum RoleType {
	ADMIN("관리자","Administrator"),  // 관리자
	MANAGER("매니저","Manager"), // 매니저
	USER("유저","User")  // 일반 유저
	
	;
	private String koreanName;
	private String englishName;
	
	private RoleType(String koreanName, String englishName) {
		this.koreanName = koreanName;
		this.englishName = englishName;
	}
	
	
}
