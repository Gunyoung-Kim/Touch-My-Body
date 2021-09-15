package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.response.UserExerciseWithDateDTO;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.UserExerciseTest;

/**
 * {@link UserExerciseWithDateDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserExerciseWithDateDTO only
 * @author kimgun-yeong
 *
 */
public class UserExerciseWithDateDTOUnitTest {
	
	/*
	 * public static List<UserExerciseWithDateDTO> of(List<UserExercise> list)
	 */
	
	@Test
	@DisplayName("UserExercise List 객체를 UserExerciseWithDateDTO 로 변환 -> 정상")
	public void ofTestList() {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		
		List<UserExercise> userExerciseList = new ArrayList<>();
		int givenUserExerciseNum = 15;
		for(int i=0;i<givenUserExerciseNum; i++) {
			UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
			userExercise.setExercise(exercise);
			userExerciseList.add(userExercise);
		}
		
		//When
		List<UserExerciseWithDateDTO> result = UserExerciseWithDateDTO.of(userExerciseList);
		
		//Then
		assertEquals(givenUserExerciseNum, result.size());
	}
	
	/*
	 * public static UserExerciseWithDateDTO of(UserExercise userExercise)
	 */
	
	@Test
	@DisplayName("UserExercise 객체를 UserExerciseWithDateDTO로 변환 -> 정상")
	public void ofTest() {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
		userExercise.setExercise(exercise);
		
		//When
		UserExerciseWithDateDTO result = UserExerciseWithDateDTO.of(userExercise);
		
		//Then
		assertEquals(exercise.getName(), result.getExerciseName());
	}
}
