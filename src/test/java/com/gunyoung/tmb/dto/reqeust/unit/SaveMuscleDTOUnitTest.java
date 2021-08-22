package com.gunyoung.tmb.dto.reqeust.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.SaveMuscleDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.util.MuscleTest;
import com.gunyoung.tmb.util.TargetTypeTest;

/**
 * {@link SaveMuscleDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SaveExerciseDTO only
 * @author kimgun-yeong
 *
 */
public class SaveMuscleDTOUnitTest {
	
	/*
	 * public static SaveMuscleDTO of(Muscle muscle) 
	 */
	
	@Test
	@DisplayName("Muscle 인스턴스 정보를 통해 SaveMuscleDTO 생성-> 정상, MuscleName 체크")
	public void ofTestCheckName() {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		String muscleName = muscle.getName();
		
		//When
		SaveMuscleDTO result = SaveMuscleDTO.of(muscle);
		
		//Then
		assertEquals(muscleName, result.getName());
	}
	
	@Test
	@DisplayName("Muscle 인스턴스 정보를 통해 SaveMuscleDTO 생성-> 정상, Category 체크")
	public void ofTestCheckCategory() {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		TargetType category = muscle.getCategory();
		
		//When
		SaveMuscleDTO result = SaveMuscleDTO.of(muscle);
		
		//Then
		assertEquals(category.getKoreanName(), result.getCategory());
	}
	
	/*
	 * public static Muscle updateMuscleBySaveMuscleDTO(Muscle muscle, SaveMuscleDTO dto) throws TargetTypeNotFoundedException
	 */
	
	@Test
	@DisplayName("SaveMuscleDTO를 통해 Muscle 업데이트 후 반환 -> dto category에 해당하는 TargetType KoreanName 없을 때")
	public void updateMuscleBySaveMuscleDTOInValidCategory() {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		
		String invalidCateogryKoreanName = "무효다";
		SaveMuscleDTO saveMuscleDTO = SaveMuscleDTO.builder()
				.name("modifiedName")
				.category(invalidCateogryKoreanName)
				.build();
		
		//When, Then
		assertThrows(TargetTypeNotFoundedException.class, () -> {
			SaveMuscleDTO.updateMuscleBySaveMuscleDTO(muscle, saveMuscleDTO);
		});
	}
	
	@Test
	@DisplayName("SaveMuscleDTO를 통해 Muscle 업데이트 후 반환 -> 정상, name 확인")
	public void updateMuscleBySaveMuscleDTOTestCheckName() {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		
		String modifiedName = "modifiedName";
		SaveMuscleDTO saveMuscleDTO = SaveMuscleDTO.builder()
				.name(modifiedName)
				.category(muscle.getCategory().getKoreanName())
				.build();
		
		//When
		Muscle result = SaveMuscleDTO.updateMuscleBySaveMuscleDTO(muscle, saveMuscleDTO);
		
		//Then
		assertEquals(modifiedName, result.getName());
	}
	
	@Test
	@DisplayName("SaveMuscleDTO를 통해 Muscle 업데이트 후 반환 -> 정상, category 확인")
	public void updateMuscleBySaveMuscleDTOTestCheckCategory() {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		
		TargetType modifiedCategory = TargetTypeTest.getAnotherTargetType(muscle.getCategory());
		String modifiedCategoryKoreanName = modifiedCategory.getKoreanName();
		SaveMuscleDTO saveMuscleDTO = SaveMuscleDTO.builder()
				.name(muscle.getName())
				.category(modifiedCategoryKoreanName)
				.build();
		
		//When
		Muscle result = SaveMuscleDTO.updateMuscleBySaveMuscleDTO(muscle, saveMuscleDTO);
		
		//Then
		assertEquals(modifiedCategory, result.getCategory());
	}
}
