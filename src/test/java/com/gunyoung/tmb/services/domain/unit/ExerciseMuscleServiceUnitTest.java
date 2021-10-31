package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.repos.ExerciseMuscleRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseMuscleServiceImpl;

/**
 * {@link ExerciseMuscleServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExerciseMuscleServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ExerciseMuscleServiceUnitTest {
	
	@Mock
	ExerciseMuscleRepository exerciseMuscleRepository;
	
	@InjectMocks
	ExerciseMuscleServiceImpl exerciseMuscleService;
	
	private ExerciseMuscle exerciseMuscle;
	
	@BeforeEach
	void setup() {
		exerciseMuscle = new ExerciseMuscle();
	}
	
	/*
	 * ExerciseMuscle save(ExerciseMuscle exerciseMuscle)
	 */
	
	@Test
	@DisplayName("ExerciseMuscle 저장 -> 정상")
	void saveTest() {
		//Given
		given(exerciseMuscleRepository.save(exerciseMuscle)).willReturn(exerciseMuscle);
		
		//When
		ExerciseMuscle result = exerciseMuscleService.save(exerciseMuscle);
		
		//Then
		assertEquals(exerciseMuscle, result);
	}
	
	/*
	 * List<ExerciseMuscle> saveAll(Iterable<ExerciseMuscle> exerciseMuscles)
	 */
	
	@Test
	@DisplayName("다수의 ExerciseMuscle 생성 및 수정 -> 정상")
	void saveAllTest() {
		//Given
		List<ExerciseMuscle> exerciseMuscleList = new ArrayList<>();
		given(exerciseMuscleRepository.saveAll(exerciseMuscleList)).willReturn(exerciseMuscleList);
		
		//When
		List<ExerciseMuscle> result = exerciseMuscleService.saveAll(exerciseMuscleList);
		
		//Then
		assertEquals(exerciseMuscleList, result);
	}
	
	/*
	 * void delete(ExerciseMuscle exerciseMuscle)
	 */
	
	@Test
	@DisplayName("ExerciseMuscle 삭제 -> 정상")
	void deleteTest() {
		//Given
		
		//When
		exerciseMuscleService.delete(exerciseMuscle);
		
		//Then
		then(exerciseMuscleRepository).should(times(1)).delete(exerciseMuscle);
	}
	
	/*
	 * void deleteAllByMuscleId(Long muscleId)
	 */
	
	@Test
	@DisplayName("Muscle ID를 통한 ExerciseMuscle 모두 삭제 -> 정상, check repository")
	void deleteAllByMuscleIdTestCheckRepo() {
		//Given
		Long muscleId = Long.valueOf(52);
		
		//When
		exerciseMuscleService.deleteAllByMuscleId(muscleId);
		
		//Then
		then(exerciseMuscleRepository).should(times(1)).deleteAllByMuscleIdInQuery(muscleId);
	}
	
	/*
	 * void deleteAllByExerciseId(Long exerciseId)
	 */
	
	@Test
	@DisplayName("Exercise ID를 통한 ExerciseMuscle 모두 삭제 -> 정상, check repository")
	void deleteAllByExerciseIdTestCheckRepo() {
		//Given
		Long exerciseId = Long.valueOf(52);
		
		//When
		exerciseMuscleService.deleteAllByExerciseId(exerciseId);
		
		//Then
		then(exerciseMuscleRepository).should(times(1)).deleteAllByExerciseIdInQuery(exerciseId);
	}
}
