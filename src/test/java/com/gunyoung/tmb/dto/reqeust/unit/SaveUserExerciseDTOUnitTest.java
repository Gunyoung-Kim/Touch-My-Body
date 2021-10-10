package com.gunyoung.tmb.dto.reqeust.unit;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.dto.reqeust.SaveUserExerciseDTO;

/**
 * {@link SaveUserExerciseDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SaveExerciseDTO only
 * @author kimgun-yeong
 *
 */
class SaveUserExerciseDTOUnitTest {
	
	/*
	 * static UserExercise toUserExercise(SaveUserExerciseDTO dto)
	 */
	
	@Test
	@DisplayName("SaveUserExerciseDTO에 담긴 정보를 통해 UserExercise 생성 후 반환 -> 정상")
	void toUserExerciseTest() {
		//Given
		SaveUserExerciseDTO saveUserExerciseDTO = SaveUserExerciseDTO.builder()
				.laps(1)
				.sets(1)
				.weight(100)
				.date(new GregorianCalendar(1999, Calendar.JANUARY, 16))
				.build();
		
		//When
		SaveUserExerciseDTO.toUserExercise(saveUserExerciseDTO);
		
		//Then
		//nothing to do
	}
}
