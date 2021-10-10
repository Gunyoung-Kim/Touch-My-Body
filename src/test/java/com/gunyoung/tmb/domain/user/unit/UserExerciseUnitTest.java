package com.gunyoung.tmb.domain.user.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.testutil.UserExerciseTest;

/**
* {@link UserExercise} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) UserExercise only
* @author kimgun-yeong
*
*/
class UserExerciseUnitTest {
	
	/*
	 *  String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상") 
	void toStringTest() {
		//Given
		Long userExerciseId = Long.valueOf(736);
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
		userExercise.setId(userExerciseId);
		
		//When
		String result = userExercise.toString();
		
		//Then
		verifyString_for_toStringTest(userExercise, result);
	}
	
	private void verifyString_for_toStringTest(UserExercise userExercise, String result) {
		assertEquals("[ id = " + userExercise.getId() + ", laps = " + userExercise.getLaps() + ", sets = " + userExercise.getSets() + ", weight = " + userExercise.getWeight() + ", description = " + userExercise.getDescription() 
				+ ", date = " + userExercise.getDate().get(Calendar.YEAR) + "." + userExercise.getDate().get(Calendar.MONTH) + 1  + "." + userExercise.getDate().get(Calendar.DATE) + " ]", result);
	}
}
