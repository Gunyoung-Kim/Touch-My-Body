package com.gunyoung.tmb.domain.exercise.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.testutil.MuscleTest;

/**
* {@link Muscle} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) Muscle only
* @author kimgun-yeong
*
*/
public class MuscleUnitTest {
	/*
	 *  public String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상") 
	public void toStringTest() {
		//Given
		Long muscleId = Long.valueOf(778);
		Muscle muscle = MuscleTest.getMuscleInstance();
		muscle.setId(muscleId);
		
		//When
		String result = muscle.toString();
		
		//Then
		verifyString_for_toStringTest(muscle, result);
	}
	
	private void verifyString_for_toStringTest(Muscle muscle, String result) {
		assertEquals("[ id = " + muscle.getId() + ", name = " + muscle.getName() + ", category = " + muscle.getCategory() +" ]", result);
	}
}
