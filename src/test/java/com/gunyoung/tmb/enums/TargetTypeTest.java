package com.gunyoung.tmb.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
	
	/*
	 * public static List<String> getKoreanNamesForAllTargetType()
	 */
	
	@Test
	@DisplayName("모든 TargetType의 koreanName들 반환 -> 정상")
	public void getKoreanNamesForAllTargetTypeTest() {
		//Given
		
		//When
		List<String> result = TargetType.getKoreanNamesForAllTargetType();
		
		//Then
		verifyResultFor_getKoreanNamesForAllTargetTypeTest(result);
	}
	
	private void verifyResultFor_getKoreanNamesForAllTargetTypeTest(List<String> result) {
		String[] koreanNamesForAllTargetType = new String[] {"가슴", "어깨", "등", "팔", "코어", "하체"};
		assertEquals(koreanNamesForAllTargetType.length, result.size());
		for(String koreanName: koreanNamesForAllTargetType) {
			assertTrue(isListOfStringContainsGivenString(result, koreanName));
		}
	}
	
	private boolean isListOfStringContainsGivenString(List<String> strings, String givenString) {
		for(String string: strings) {
			if(string.equals(givenString)) 
				return true;
		}
		return false;
	}
}
