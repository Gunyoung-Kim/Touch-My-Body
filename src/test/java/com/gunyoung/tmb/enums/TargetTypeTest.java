package com.gunyoung.tmb.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * TargetType 클래스에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) TargetType.class only 
 * @author kimgun-yeong
 *
 */
public class TargetTypeTest {
	
	/*
	 * public static TargetType getFromKoreanName(String koreanName)
	 */
	@Test
	@DisplayName("한국이름으로 TargetType 찾기 -> 해당하는 TargetType 없음")
	public void getFromKoreanNameNonExist() {
		//Given
		String nonExistKoreanName = "아무개";
		
		//When
		
		TargetType result = TargetType.getFromKoreanName(nonExistKoreanName);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("한국이름으로 TargetType 찾기 -> 해당하는 TargetType 없음")
	public void getFromKoreanNameTest() {
		//Given
		TargetType targetType = TargetType.ARM;
		String existKoreanName = targetType.getKoreanName();
		
		//When
		
		TargetType result = TargetType.getFromKoreanName(existKoreanName);
		
		//Then
		assertEquals(targetType, result);
	}
}
