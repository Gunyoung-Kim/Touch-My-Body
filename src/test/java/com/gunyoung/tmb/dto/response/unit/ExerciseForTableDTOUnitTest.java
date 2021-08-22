package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.ExerciseForTableDTO;
import com.gunyoung.tmb.util.ExerciseTest;

/**
 * {@link ExerciseForTableDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExerciseForTableDTO only
 * @author kimgun-yeong
 *
 */
public class ExerciseForTableDTOUnitTest {
	
	/*
	 * public static ExerciseForTableDTO of(Exercise exercise) 
	 */
	
	@Test
	@DisplayName("Exercise 인스턴스를 통해 ExerciseForTableDTO 객체 생성 후 반환 -> 정상")
	public void ofTest() {
		//Given
		Long exerciseId = Long.valueOf(23);
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exercise.setId(exerciseId);
		
		//When
		ExerciseForTableDTO result = ExerciseForTableDTO.of(exercise);
		
		//Then
		assertEquals(exerciseId, result.getId());
	}
}
