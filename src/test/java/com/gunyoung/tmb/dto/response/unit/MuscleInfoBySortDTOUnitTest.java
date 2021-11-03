package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.dto.response.MuscleInfoBySortDTO;
import com.gunyoung.tmb.enums.TargetType;

/**
 * {@link MuscleInfoBySortDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) MuscleInfoBySortDTO only
 * @author kimgun-yeong
 *
 */
class MuscleInfoBySortDTOUnitTest {
	
	/*
	 * public static MuscleInfoBySortDTO of(String category, List<String> listOfMuscleNames)
	 */
	
	@Test
	@DisplayName("category, muscleNames를 통해 MusclInfoBySortDTO 생성 후 반환 -> 정상")
	void ofCategoryAndListOfMuscleNamesTest() {
		//Given
		String category = TargetType.ARM.getKoreanName();
		List<String> muscleNames = getMuscleNames();
		
		//When
		MuscleInfoBySortDTO result = MuscleInfoBySortDTO.of(category, muscleNames);
		
		//Then
		assertEquals(category, result.getCategory());
		assertEquals(muscleNames, result.getMuscleNames());
	}
	
	private List<String> getMuscleNames() {
		int sizeOfMuscleNames = 10;
		List<String> muscleNames = new LinkedList<>();
		for(int i = 0; i < sizeOfMuscleNames; i++) {
			muscleNames.add("muscle"+i);
		}
		return muscleNames;
	}
}
