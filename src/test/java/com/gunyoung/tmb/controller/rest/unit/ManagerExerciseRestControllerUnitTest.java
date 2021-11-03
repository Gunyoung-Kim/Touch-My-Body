package com.gunyoung.tmb.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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

import com.gunyoung.tmb.controller.rest.ManagerExerciseRestController;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.MuscleInfoBySortDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.ExerciseNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.TargetTypeTest;

/**
 * {@link ManagerExerciseRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerExerciseRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ManagerExerciseRestControllerUnitTest {
	
	@Mock
	ExerciseService exerciseService;
	
	@Mock
	MuscleService muscleService;
	
	@InjectMocks
	ManagerExerciseRestController managerExerciseRestController;
	
	/*
	 * void addExercise(@ModelAttribute SaveExerciseDTO dto)
	 */
	
	@Test
	@DisplayName("매니저의 Exercise 추가 처리 -> 입력된 이름의 Exercise 이미 존재하면")
	void addExerciseNameDuplicated() {
		//Given
		String alreadtExistExerciseName = "exist";
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance(alreadtExistExerciseName);
		
		given(exerciseService.existsByName(alreadtExistExerciseName)).willReturn(true);
		
		//When, Then
		assertThrows(ExerciseNameDuplicationFoundedException.class, () -> {
			managerExerciseRestController.addExercise(dto);
		});
	}
	
	@Test
	@DisplayName("매니저의 Exercise 추가 처리 -> 정상")
	void addExerciseTest() {
		//Given
		String newExerciseName = "newExercise";
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance(newExerciseName);
		
		given(exerciseService.existsByName(newExerciseName)).willReturn(false);
		
		//When
		managerExerciseRestController.addExercise(dto);
		
		//Then
		then(exerciseService).should(times(1)).saveWithSaveExerciseDTO(any(Exercise.class), any(SaveExerciseDTO.class));
	}
	
	/*
	 * void modifyExercise(@PathVariable("exerciseId") Long exerciseId, @ModelAttribute SaveExerciseDTO dto)
	 */
	
	@Test
	@DisplayName("특정 Exercise 정보 수정 처리 -> 해당 Id의 Exercise 없으면")
	void modifyExerciseExerciseNonExist() {
		//Given
		String changedExerciseName = "changeExercise";
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance(changedExerciseName);
		
		Long nonExistExerciseId = Long.valueOf(1);
		given(exerciseService.findWithExerciseMusclesById(nonExistExerciseId)).willThrow(new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription()));
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			managerExerciseRestController.modifyExercise(nonExistExerciseId, dto);
		});
	}
	
	@Test
	@DisplayName("특정 Exercise 정보 수정 처리 -> 변경된 이름이 다른 Exercise의 이름과 일치하면")
	void modifyExerciseNameDuplicated() {
		//Given
		String changedButDuplicatedExerciseName = "hangedButDuplicated";
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance(changedButDuplicatedExerciseName);
		
		Long exerciseId = Long.valueOf(1);
		mockingExerciseServiceFindWithExerciseMusclesById(exerciseId);
		
		given(exerciseService.existsByName(changedButDuplicatedExerciseName)).willReturn(true);
		
		//When, Then
		assertThrows(ExerciseNameDuplicationFoundedException.class, () -> {
			managerExerciseRestController.modifyExercise(exerciseId, dto);
		});
	}
	
	@Test
	@DisplayName("특정 Exercise 정보 수정 처리 -> 정상, 이름이 변경")
	void modifyExerciseNameChangedTest() {
		//Given
		String changedExerciseName = "changeExercise";
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance(changedExerciseName);
		
		Long exerciseId = Long.valueOf(1);
		Exercise exercise = mockingExerciseServiceFindWithExerciseMusclesById(exerciseId);
		
		given(exerciseService.existsByName(changedExerciseName)).willReturn(false);
		
		//When
		managerExerciseRestController.modifyExercise(exerciseId, dto);
		
		//Then
		then(exerciseService).should(times(1)).saveWithSaveExerciseDTO(exercise, dto);
	}
	
	@Test
	@DisplayName("특정 Exercise 정보 수정 처리 -> 정상, 이름은 변경 안됨")
	void modifyExerciseNameNonchangedTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		Exercise exercise = mockingExerciseServiceFindWithExerciseMusclesById(exerciseId);
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance(exercise.getName());
		
		TargetType changedTargetType = TargetTypeTest.getAnotherTargetType(exercise.getTarget());
		dto.setTarget(changedTargetType.getKoreanName());
		
		//When
		managerExerciseRestController.modifyExercise(exerciseId, dto);
		
		//Then
		then(exerciseService).should(times(1)).saveWithSaveExerciseDTO(exercise, dto);
	}
	
	private Exercise mockingExerciseServiceFindWithExerciseMusclesById(Long exerciseId) {
		Exercise exercise = ExerciseTest.getExerciseInstance();
		given(exerciseService.findWithExerciseMusclesById(exerciseId)).willReturn(exercise);
		return exercise;
	}
	
	/*
	 * void deleteExercise(@RequestParam("exerciseId") Long exerciseId)
	 */
	
	@Test
	@DisplayName("특정 Exercise 삭제 요청 처리 -> 정상")
	void deleteExerciseTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		
		//When
		managerExerciseRestController.deleteExercise(exerciseId);
		
		//Then
		then(exerciseService).should(times(1)).deleteById(exerciseId);
	}
	
	/*
	 * List<MuscleInfoBySortDTO> getMusclesSortByCategory()
	 */
	
	@Test
	@DisplayName("클라이언트에게 근육 종류별로 분류해서 전송 -> 정상")
	void getMusclesSortByCategoryTest() {
		//Given
		Map<String ,List<String>> MusclesSortMap = new HashMap<>();
		
		for(TargetType tt: TargetType.values()) {
			List<String> musclesName = new ArrayList<>();
			musclesName.add(tt.getKoreanName());
			MusclesSortMap.put(tt.toString(), musclesName);
		}
		
		given(muscleService.getAllMusclesWithSortingByCategory()).willReturn(MusclesSortMap);
		
		//When
		List<MuscleInfoBySortDTO> result = managerExerciseRestController.getMusclesSortByCategory();
		
		//Then
		assertMuscleInfoBySortDTOList(result);
	}
	
	private void assertMuscleInfoBySortDTOList(List<MuscleInfoBySortDTO> result) {
		for(MuscleInfoBySortDTO dto: result) {
			String category = dto.getCategory();
			for(String exerciseName :dto.getMuscleNames()) {
				assertEquals(exerciseName, TargetType.valueOf(category).getKoreanName());
			}
		}
	}
}
