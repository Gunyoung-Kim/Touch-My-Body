package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.response.MuscleForTableDTO;
import com.gunyoung.tmb.testutil.MuscleTest;

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
		verifyMuscleForTableDTOFieldWithMuscle(muscle, result);
	}
	
	private void verifyMuscleForTableDTOFieldWithMuscle(Muscle muscle, MuscleForTableDTO muscleForTableDTO) {
		assertEquals(muscle.getId(), muscleForTableDTO.getId());
		assertEquals(muscle.getName(), muscleForTableDTO.getName());
		assertEquals(muscle.getCategory().getKoreanName(), muscleForTableDTO.getCategory());
	}
	
	/*
	 * public static List<MuscleForTableDTO> of(Iterable<Muscle> muscles)
	 */
	
	@Test
	@DisplayName("Muscle 컬렉션을 통해 MuscleForTableDTO 리스트 반환 -> 정상")
	public void ofListTest() {
		//Given
		List<Muscle> muscles = new ArrayList<>();
		int givenMuscleNum = 15;
		for(int i = 0 ; i < givenMuscleNum; i++) {
			Muscle muscle = MuscleTest.getMuscleInstance();
			muscle.setId(Long.valueOf(i));
			muscles.add(muscle);
		}
		
		//When
		List<MuscleForTableDTO> result = MuscleForTableDTO.of(muscles);
		
		//Then
		assertEquals(givenMuscleNum, result.size());
	}
}
