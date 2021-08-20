package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.repos.ExerciseMuscleRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseMuscleService;
import com.gunyoung.tmb.util.ExerciseMuscleTest;

/**
 * ExerciseMuscleService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@SpringBootTest
public class ExerciseMuscleServiceTest {
	
	@Autowired
	ExerciseMuscleRepository exerciseMuscleRepository;
	
	@Autowired
	ExerciseMuscleService exerciseMuscleService;
	
	private ExerciseMuscle exerciseMuscle;
	
	@BeforeEach
	void setup() {
		exerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance();
		exerciseMuscleRepository.save(exerciseMuscle);
	}
	
	@AfterEach
	void tearDown() {
		exerciseMuscleRepository.deleteAll();
	}
	
	/*
	 *  public ExerciseMuscle findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 ExerciseMuscle 찾기 -> 해당 id의 exerciseMuscle 없음")
	public void findByIdNonExist() {
		//Given
		long nonExistExerciseMuscleId = ExerciseMuscleTest.getNonExistExerciseMuscleId(exerciseMuscleRepository);
		
		//When
		ExerciseMuscle result = exerciseMuscleService.findById(nonExistExerciseMuscleId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 ExerciseMuscle 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long exerciseMuscleId = exerciseMuscle.getId();
		
		//When
		ExerciseMuscle result = exerciseMuscleService.findById(exerciseMuscleId);
		
		//Then
		assertNotNull(result);
		
	}
	
	/*
	 *  public ExerciseMuscle save(ExerciseMuscle exerciseMuscle)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExerciseMuscle 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		Long exerciseMuscleId = exerciseMuscle.getId();
		boolean givenExerciseMuscleIsMain = exerciseMuscle.isMain();
		exerciseMuscle.setMain(!givenExerciseMuscleIsMain);
		
		//When
		exerciseMuscleService.save(exerciseMuscle);
		
		//Then
		ExerciseMuscle result = exerciseMuscleRepository.findById(exerciseMuscleId).get();
		assertEquals(!givenExerciseMuscleIsMain, result.isMain());
	}
	
	@Test
	@Transactional
	@DisplayName("ExerciseMuscle 추가하기 -> 정상")
	public void saveTest() {
		//Given
		ExerciseMuscle newExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance();
		Long givenExerciseMuscleNum = exerciseMuscleRepository.count();
		
		//When
		exerciseMuscleService.save(newExerciseMuscle);
		
		//Then
		assertEquals(givenExerciseMuscleNum + 1, exerciseMuscleRepository.count());
	}
	
	/*
	 *  public void delete(ExerciseMuscle exerciseMuscle)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExerciseMuscle 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		ExerciseMuscle existExerciseMuscle = exerciseMuscleRepository.findAll().get(0);
		Long givenExerciseMuscleNum = exerciseMuscleRepository.count();
		
		//When
		exerciseMuscleService.delete(existExerciseMuscle);
		
		//Then
		assertEquals(givenExerciseMuscleNum - 1, exerciseMuscleRepository.count());
	}
}
