package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.util.ExerciseMuscleTest;
import com.gunyoung.tmb.util.ExerciseTest;

/**
 * {@link ExerciseForInfoViewDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExerciseForInfoViewDTO only
 * @author kimgun-yeong
 *
 */
public class ExerciseForInfoViewDTOUnitTest {
	
	/*
	 * public static ExerciseForInfoViewDTO of(Exercise exercise) 
	 */
	
	@Test
	@DisplayName("Exercise를 통해 ExerciseForInfoViewDTO 객체 생성 및 반환 -> 정상, Exercise 필드와 확인")
	public void ofExerciseTestCheckWithExercise() {
		//Given
		Long exerciseId = Long.valueOf(24);
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exercise.setId(exerciseId);
		
		//When
		ExerciseForInfoViewDTO result = ExerciseForInfoViewDTO.of(exercise);
		
		//Then
		verifyExerciseForInfoViewDTO_for_ofExerciseTest(exercise, result);
	}
	
	private void verifyExerciseForInfoViewDTO_for_ofExerciseTest(Exercise exercise, ExerciseForInfoViewDTO result) {
		assertEquals(exercise.getId(), result.getId());
		assertEquals(exercise.getName(), result.getName());
		assertEquals(exercise.getDescription(), result.getDescription());
		assertEquals(exercise.getCaution(), result.getCaution());
		assertEquals(exercise.getMovement(), result.getMovement());
		assertEquals(exercise.getTarget().getKoreanName(), result.getTarget());
	}
	
	@Test
	@DisplayName("Exercise를 통해 ExerciseForInfoViewDTO 객체 생성 및 반환 -> 정상, ExerciseMuscle과 확인")
	public void ofExerciseTestCheckWithExerciseMuscle() {
		//Given
		Long exerciseId = Long.valueOf(24);
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exercise.setId(exerciseId);
		
		String mainExerciseMuscleName = "mainMuscle";
		ExerciseMuscle mainExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance(mainExerciseMuscleName, true);
		exercise.getExerciseMuscles().add(mainExerciseMuscle);
		
		String subExerciseMuscleName = "subMuscle";
		ExerciseMuscle subExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance(subExerciseMuscleName, false);
		exercise.getExerciseMuscles().add(subExerciseMuscle);
		
		//When
		ExerciseForInfoViewDTO result = ExerciseForInfoViewDTO.of(exercise);
		
		//Then
		verfiyExerciseForInfoViewDTO_for_ofExerciseTestCheckWithExerciseMuscle(mainExerciseMuscleName, subExerciseMuscleName, result);
	}
	
	private void verfiyExerciseForInfoViewDTO_for_ofExerciseTestCheckWithExerciseMuscle(String mainExerciseMuscleName, String subExerciseMuscle, ExerciseForInfoViewDTO result) {
		assertEquals(mainExerciseMuscleName, result.getMainMuscle());
		assertEquals(subExerciseMuscle, result.getSubMuscle());
	}
}
