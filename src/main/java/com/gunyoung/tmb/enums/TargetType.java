package com.gunyoung.tmb.enums;

import lombok.Getter;

@Getter
public enum TargetType {
	CHEST("가슴","Chest"),
	SHOULDER("어깨","Shoulder"),
	BACK("등","Back"),
	ARM("팔","Arm"),
	CORE("코어","Core"),
	LOWER_BODY("하체","Lower Body")
	;
	private String koreanName;
	private String englishName;
	
	private TargetType(String koreanName, String englishName) {
		this.koreanName = koreanName;
		this.englishName = englishName;
	}
	
	
}
