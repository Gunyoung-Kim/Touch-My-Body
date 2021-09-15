package com.gunyoung.tmb.domain.exercise.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.testutil.ExerciseMuscleTest;

/**
* {@link ExerciseMuscle} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) ExerciseMuscle only
* @author kimgun-yeong
*
*/
public class ExerciseMuscleUnitTest {
	/*
	 *  public String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상") 
	public void toStringTest() {
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
}
