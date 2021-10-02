package com.gunyoung.tmb.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Exercise, Muscle 들의 대분류
 * @author kimgun-yeong
 *
 */
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
	
	/**
	 * 인수와 일치하는 koreanName을 가지는 TargetType을 반환
	 * @param koreanName
	 * @return TargetType, null(해당 KoreanName의 TargetType 없을 때)
	 */
	public static TargetType getFromKoreanName(String koreanName) {
		for(TargetType tt: TargetType.values()) {
			if(tt.getKoreanName().equals(koreanName)) {
				return tt;
			}
		}
		
		return null;
	}
	
	/**
	 * 모든 TargetType의 koreanName들 반환 
	 * @author kimgun-yeong
	 */
	public static List<String> getKoreanNamesForAllTargetType() {
		List<String> koreanNamesForAllTargetType = new ArrayList<>();
		for(TargetType t: TargetType.values()) {
			koreanNamesForAllTargetType.add(t.getKoreanName());
		}
		return koreanNamesForAllTargetType;
	}
}
