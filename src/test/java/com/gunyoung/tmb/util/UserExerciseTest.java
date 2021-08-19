package com.gunyoung.tmb.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Test 클래스 전용 UserExercise 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class UserExerciseTest {
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.SaveUserExerciseDTO} 객체에 바인딩 될 수 있는 MultiValueMap 반환 <br>
	 * exerciseName 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String, String> getSaveUserExerciseDTOMap(String exerciseName) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("laps", "1");
		map.add("sets", "1");
		map.add("weight", "1");
		map.add("description", "des");
		map.add("date", "2021-05-05");
		map.add("exerciseName", exerciseName);
		return map;
	}
}
