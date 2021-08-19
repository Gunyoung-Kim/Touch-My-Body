package com.gunyoung.tmb.util;

import java.util.Calendar;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.user.UserExercise;

/**
 * Test 클래스 전용 UserExercise 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class UserExerciseTest {
	
	/**
	 * 테스트용 UserExercise 인스턴스 반환 <br>
	 * date 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static UserExercise getUserExerciseInstance(Calendar date) {
		UserExercise ue = UserExercise.builder()
				.date(date)
				.description("description")
				.laps(1)
				.sets(1)
				.weight(1)
				.build();
		return ue;
	}
	
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
