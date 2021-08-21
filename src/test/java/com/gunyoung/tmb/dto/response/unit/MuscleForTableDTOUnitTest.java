package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.response.MuscleForTableDTO;
import com.gunyoung.tmb.util.MuscleTest;

/**
 * {@link MuscleForTableDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) MuscleForTableDTO only
 * @author kimgun-yeong
 *
 */
public class MuscleForTableDTOUnitTest {
	
	/*
	 * public static MuscleForTableDTO of(Muscle muscle) 
	 */
	
	@Test
	@DisplayName("Muscle 객체로 MuscleForTableDTO 객체 생성 및 반환 -> 정상")
	public void ofTest() {
		//Given
		Long muscleId = Long.valueOf(24);
		Muscle muscle = MuscleTest.getMuscleInstance();
		muscle.setId(muscleId);
		
		//When
		MuscleForTableDTO result = MuscleForTableDTO.of(muscle);
		
		//Then
		assertEquals(muscleId, result.getId());
	}
}
