package com.gunyoung.tmb.controller.rest.unit;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.controller.rest.ManagerExercisePostRestController;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;

/**
 * {@link ManagerExercisePostRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerExercisePostRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ManagerExercisePostRestControllerUnitTest {
	
	@Mock
	ExercisePostService exercisePostService;
	
	@InjectMocks
	ManagerExercisePostRestController managerExercisePostRestController;
	
	/*
	 * void removeExercisePostByManager(@RequestParam("postId") Long postId)
	 */
	
	@Test
	@DisplayName("매니저가 특정 게시글을 삭제하려는 요청 처리 -> 정상")
	void removeExercisePostByManagerTest() {
		//Given
		Long exercisePostId = Long.valueOf(1);
		
		//When
		managerExercisePostRestController.removeExercisePostByManager(exercisePostId);
		
		//Then
		then(exercisePostService).should(times(1)).deleteById(exercisePostId);
	}
}
