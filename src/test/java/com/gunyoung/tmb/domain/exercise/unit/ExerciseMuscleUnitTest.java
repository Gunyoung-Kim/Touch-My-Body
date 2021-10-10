package com.gunyoung.tmb.domain.exercise.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.testutil.ExerciseMuscleTest;

/**
* {@link ExerciseMuscle} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) ExerciseMuscle only
* @author kimgun-yeong
*
*/
class ExerciseMuscleUnitTest {
	/*
	 *  String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상") 
	void toStringTest() {
		//Given
		Long exerciseMuscleId = Long.valueOf(75);
		ExerciseMuscle exerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance();
		exerciseMuscle.setId(exerciseMuscleId);
		
		//When
		String result = exerciseMuscle.toString();
		
		//Then
		verifyString_for_toStringTest(exerciseMuscle, result);
	}
	
	private void verifyString_for_toStringTest(ExerciseMuscle exerciseMuscle, String result) {
		assertEquals("[ id = " + exerciseMuscle.getId() + ", isMain = " + exerciseMuscle.isMain() + ", muscleName = " + exerciseMuscle.getMuscleName() +" ]", result);
	}
	
	/*
	 * List<ExerciseMuscle> mainOf(Exercise exercise, List<Muscle> muscleList)
	 */
	
	@Test
	@DisplayName("Exercise, List of Muscle 를 이용해 isMain 필드 값이 true인 ExerciseMuscle List 생성 후 반환 -> 정상")
	void mainOfTest() {
		//Given
		Exercise exercise = new Exercise();
		int muscleNum = 5;
		
		List<Muscle> muscles = new ArrayList<>();
		
		for(int i=0; i < muscleNum; i++) {
			Muscle muscle = Muscle.builder()
					.name("muscle" + i)
					.build();
			
			muscles.add(muscle);
		}
		
		//when
		List<ExerciseMuscle> result = ExerciseMuscle.mainOf(exercise, muscles);
		
		//Then
		verifyResultFor_mainOfTest(exercise, muscles, result);
	}
	
	private void verifyResultFor_mainOfTest(Exercise exercise, List<Muscle> listOfMuscle, List<ExerciseMuscle> result) {
		verifyExerciseForExerciseMuscles(exercise, result);
		assertEquals(listOfMuscle.size(), result.size());
		verifyAllExerciseMusclesIsMain(true, result);
	}
	
	/*
	 * List<ExerciseMuscle> subOf(Exercise exercise, List<Muscle> muscleList)
	 */
	
	@Test
	@DisplayName("Exercise, List of Muscle 를 이용해 isMain 필드 값이 false인 ExerciseMuscle List 생성 후 반환 -> 정상")
	void subOfTest() {
		//Given
		Exercise exercise = new Exercise();
		int muscleNum = 5;
		
		List<Muscle> muscles = new ArrayList<>();
		
		for(int i=0; i < muscleNum; i++) {
			Muscle muscle = Muscle.builder()
					.name("muscle" + i)
					.build();
			
			muscles.add(muscle);
		}
		
		//when
		List<ExerciseMuscle> result = ExerciseMuscle.subOf(exercise, muscles);
		
		//Then
		verifyResultFor_subOfTest(exercise, muscles, result);
	}
	
	private void verifyResultFor_subOfTest(Exercise exercise, List<Muscle> listOfMuscle, List<ExerciseMuscle> result) {
		verifyExerciseForExerciseMuscles(exercise, result);
		assertEquals(listOfMuscle.size(), result.size());
		verifyAllExerciseMusclesIsMain(false, result);
	}
	
	private void verifyExerciseForExerciseMuscles(Exercise exercise, List<ExerciseMuscle> result) {
		for(ExerciseMuscle em: result) {
			assertEquals(exercise, em.getExercise());
		}
	}
	
	private void verifyAllExerciseMusclesIsMain(boolean wantedIsMainValue, List<ExerciseMuscle> result) {
		for(ExerciseMuscle em: result) {
			assertEquals(wantedIsMainValue, em.isMain());
		}
	}
}
