package com.gunyoung.tmb.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.controller.rest.ExerciseRestController;
import com.gunyoung.tmb.dto.response.ExerciseInfoBySortDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;

/**
 * {@link ExerciseRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExerciseRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ExerciseRestControllerUnitTest {
	
	@Mock
	ExerciseService exerciseService;
	
	@InjectMocks
	ExerciseRestController exerciseRestController;
	
	/*
	 * public List<ExerciseInfoBySortDTO> getExercisesByNameAndTarget()
	 */
	
	@Test
	@DisplayName("각 부위별 운동 종류 반환 -> 정상")
	public void getExercisesByNameAndTargetTest() {
		//Given
		Map<String ,List<String>> exerciseNamesSortMap = new HashMap<>();
		
		for(TargetType tt: TargetType.values()) {
			List<String> exerciseNames = new ArrayList<>();
			exerciseNames.add(tt.getKoreanName());
			exerciseNamesSortMap.put(tt.toString(), exerciseNames);
		}
		
		given(exerciseService.getAllExercisesNamewithSorting()).willReturn(exerciseNamesSortMap);
		
		//When
		List<ExerciseInfoBySortDTO> result = exerciseRestController.getExercisesByNameAndTarget();
		
		//Then
		assertExerciseInfoBySortDTOList(result);
	}
	
	private void assertExerciseInfoBySortDTOList(List<ExerciseInfoBySortDTO> result) {
		for(ExerciseInfoBySortDTO dto: result) {
			String target = dto.getTarget();
			for(String exerciseName :dto.getExerciseNames()) {
				assertEquals(exerciseName, TargetType.valueOf(target).getKoreanName());
			}
		}
	}
}
