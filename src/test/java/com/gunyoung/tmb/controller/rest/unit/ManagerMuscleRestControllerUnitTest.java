package com.gunyoung.tmb.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.controller.rest.ManagerMuscleRestController;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.SaveMuscleDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.duplication.MuscleNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;
import com.gunyoung.tmb.util.MuscleTest;
import com.gunyoung.tmb.util.TargetTypeTest;

/**
 * {@link ManagerMuscleRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerMuscleRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ManagerMuscleRestControllerUnitTest {
	
	@Mock
	MuscleService muscleService;
	
	@InjectMocks
	ManagerMuscleRestController managerMuscleRestController;
	
	/*
	 * public void modifyMuscle(@PathVariable("muscleId") Long muscleId, @ModelAttribute SaveMuscleDTO dto)
	 */
	
	@Test
	@DisplayName("Muscle 정보 수정 요청 처리 -> 해당 Id의 Muscle 없으면")
	public void modifyMuscleMuscleNonExist() {
		//Given
		Long nonExistMuscleId = Long.valueOf(1);
		given(muscleService.findById(nonExistMuscleId)).willReturn(null);
		
		SaveMuscleDTO saveMuscleDTO = MuscleTest.getSaveMuscleDTOInstance("changedMuscle", TargetType.ARM.getKoreanName());
		
		//When, Then
		assertThrows(MuscleNotFoundedException.class, () -> {
			managerMuscleRestController.modifyMuscle(nonExistMuscleId, saveMuscleDTO);
		});
	}
	
	@Test
	@DisplayName("Muscle 정보 수정 요청 처리 -> 수정된 이름이 이미 존재하는 Muscle의 이름과 일치한다면")
	public void modifyMuscleNameDuplicated() {
		//Given
		Long muscleId = Long.valueOf(1);
		stubbingMuscleServiceFindById(muscleId);
		
		String changedButDuplicatedMuscleName = "changedAndDuplicated";
		SaveMuscleDTO saveMuscleDTO = MuscleTest.getSaveMuscleDTOInstance(changedButDuplicatedMuscleName, TargetType.ARM.getKoreanName());
		
		given(muscleService.existsByName(changedButDuplicatedMuscleName)).willReturn(true);
		
		//When, Then
		assertThrows(MuscleNameDuplicationFoundedException.class, () -> {
			managerMuscleRestController.modifyMuscle(muscleId, saveMuscleDTO);
		});
	}
	
	@Test
	@DisplayName("Muscle 정보 수정 요청 처리 -> dto에 담긴 category을 한국이름으로 갖는 TargetType 없을 때")
	public void modifyMuscleTargetTypeNonExist() {
		//Given
		Long muscleId = Long.valueOf(1);
		stubbingMuscleServiceFindById(muscleId);
		
		String changedMuscleName = "changedName";
		String nonExistTargetTypeKoreanName = "nonexist";
		SaveMuscleDTO saveMuscleDTO = MuscleTest.getSaveMuscleDTOInstance(changedMuscleName, nonExistTargetTypeKoreanName);
		
		given(muscleService.existsByName(changedMuscleName)).willReturn(false);
		
		//When, Then
		assertThrows(TargetTypeNotFoundedException.class, () -> {
			managerMuscleRestController.modifyMuscle(muscleId, saveMuscleDTO);
		});
	}
	
	@Test
	@DisplayName("Muscle 정보 수정 요청 처리 -> 정상, 이름 변경 확인")
	public void modifyMuscleTestCheckName() {
		//Given
		Long muscleId = Long.valueOf(1);
		Muscle muscle = stubbingMuscleServiceFindById(muscleId);
		
		String changedMuscleName = "changedName";
		String muscleCategoryKoreanName = muscle.getCategory().getKoreanName();
		SaveMuscleDTO saveMuscleDTO = MuscleTest.getSaveMuscleDTOInstance(changedMuscleName, muscleCategoryKoreanName);
		
		given(muscleService.existsByName(changedMuscleName)).willReturn(false);
		
		//When
		managerMuscleRestController.modifyMuscle(muscleId, saveMuscleDTO);
		
		//Then
		assertEquals(changedMuscleName, muscle.getName());
	}
	
	@Test
	@DisplayName("Muscle 정보 수정 요청 처리 -> 정상, 카테고리 변경 확인")
	public void modifyMuscleTestCheckCategory() {
		//Given
		Long muscleId = Long.valueOf(1);
		Muscle muscle = stubbingMuscleServiceFindById(muscleId);
		
		String muscleName = muscle.getName();
		TargetType changedCategorty = TargetTypeTest.getAnotherTargetType(muscle.getCategory());
		String changeCategoryKoreanName = changedCategorty.getKoreanName();
		SaveMuscleDTO saveMuscleDTO = MuscleTest.getSaveMuscleDTOInstance(muscleName, changeCategoryKoreanName);
		
		//When
		managerMuscleRestController.modifyMuscle(muscleId, saveMuscleDTO);
		
		//Then
		assertEquals(changedCategorty, muscle.getCategory());
	}
	
	@Test
	@DisplayName("Muscle 정보 수정 요청 처리 -> 정상, 저장 확인")
	public void modifyMuscleTestCheckSave() {
		//Given
		Long muscleId = Long.valueOf(1);
		Muscle muscle = stubbingMuscleServiceFindById(muscleId);
		
		String muscleName = muscle.getName();
		TargetType changedCategorty = TargetTypeTest.getAnotherTargetType(muscle.getCategory());
		String changeCategoryKoreanName = changedCategorty.getKoreanName();
		SaveMuscleDTO saveMuscleDTO = MuscleTest.getSaveMuscleDTOInstance(muscleName, changeCategoryKoreanName);
		
		//When
		managerMuscleRestController.modifyMuscle(muscleId, saveMuscleDTO);
		
		//Then
		then(muscleService).should(times(1)).save(muscle);
	}
	
	private Muscle stubbingMuscleServiceFindById(Long muscleId) {
		Muscle muscle = MuscleTest.getMuscleInstance();
		given(muscleService.findById(muscleId)).willReturn(muscle);
		return muscle;
	}
}
