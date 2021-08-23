package com.gunyoung.tmb.dto.reqeust.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.dto.reqeust.SaveExercisePostDTO;

/**
 * {@link SaveExercisePostDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SaveExercisePostDTO only
 * @author kimgun-yeong
 *
 */
public class SaveExercisePostDTOUnitTest {
	
	/*
	 * public ExercisePost createExercisePost() 
	 */
	
	@Test
	@DisplayName("이 객체를 통해 ExercisePost 생성 후 반환 -> 정상, 필드 값 체크")
	public void createExercisePostTestCheckField() {
		//Given
		SaveExercisePostDTO saveExercisePostDTO = SaveExercisePostDTO.builder()
				.title("title")
				.contents("contents")
				.build();
		
		//When
		ExercisePost result = saveExercisePostDTO.createExercisePost();
		
		//Then
		verifyFieldForExercisePostWithSaveExercisePostDTO(saveExercisePostDTO, result);
	}
	
	private void verifyFieldForExercisePostWithSaveExercisePostDTO(SaveExercisePostDTO saveExercisePostDTO, ExercisePost result) {
		assertEquals(saveExercisePostDTO.getTitle(), result.getTitle());
		assertEquals(saveExercisePostDTO.getContents(), result.getContents());
	}
}
