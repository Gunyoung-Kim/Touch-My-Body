package com.gunyoung.tmb.dto.reqeust.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.util.ExerciseMuscleTest;
import com.gunyoung.tmb.util.ExerciseTest;

/**
 * {@link SaveExerciseDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SaveExerciseDTO only
 * @author kimgun-yeong
 *
 */
public class SaveExerciseDTOUnitTest {
	
	
	/*
	 * public static SaveExerciseDTO of(Exercise exercise) 
	 */
	
	@Test
	@DisplayName("Exercise 객체를 통해 SaveExerciseDTO 객체 생성 및 반환, mainMuscles 개수 확인")
	public void ofTestCheckMainMusclesNum() {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		
		int mainExerciseMuscleNum = 10;
		int subExerciseMuscleNum = 0;
		setExerciseMuscleToExercise(exercise, mainExerciseMuscleNum, subExerciseMuscleNum);
		
		//When
		SaveExerciseDTO result = SaveExerciseDTO.of(exercise);
		
		//Then
		assertEquals(mainExerciseMuscleNum, result.getMainMuscles().size());
	}
	
	@Test
	@DisplayName("Exercise 객체를 통해 SaveExerciseDTO 객체 생성 및 반환, subMuscles 개수 확인")
	public void ofTestCheckSubMusclesNum() {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		
		int mainExerciseMuscleNum = 0;
		int subExerciseMuscleNum = 10;
		setExerciseMuscleToExercise(exercise, mainExerciseMuscleNum, subExerciseMuscleNum);
		
		//When
		SaveExerciseDTO result = SaveExerciseDTO.of(exercise);
		
		//Then
		assertEquals(subExerciseMuscleNum, result.getSubMuscles().size());
	}
	
	private void setExerciseMuscleToExercise(Exercise exercise, int mainExerciseMuscleNum, int subExerciseMuscleNum) {
		for(int i=0;i < mainExerciseMuscleNum; i++) {
			ExerciseMuscle mainExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance();
			mainExerciseMuscle.setMain(true);
			exercise.getExerciseMuscles().add(mainExerciseMuscle);
		}
		
		for(int i=0; i < subExerciseMuscleNum; i++) {
			ExerciseMuscle subExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance();
			subExerciseMuscle.setMain(false);
			exercise.getExerciseMuscles().add(subExerciseMuscle);
		}
	}
}
