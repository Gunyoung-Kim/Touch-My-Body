package com.gunyoung.tmb.domain.exercise.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.testutil.ExerciseTest;

/**
* {@link Exercise} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) Exercise only
* @author kimgun-yeong
*
*/
public class ExerciseUnitTest {
	
	/*
	 *  public String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상") 
	public void toStringTest() {
		//Given
		Long exerciseId = Long.valueOf(35);
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exercise.setId(exerciseId);
		
		//When
		String result = exercise.toString();
		
		//Then
		verifyString_for_toStringTest(exercise, result);
	}
	
	private void verifyString_for_toStringTest(Exercise exercise, String result) {
		assertEquals("[ id = " + exercise.getId() + ", name = " + exercise.getName() + ", description = " + exercise.getDescription() 
				+ ", caution = " + exercise.getCaution() + ", movement = " + exercise.getMovement() + ", target = " + exercise.getTarget() +" ]", result);
	}
}
