package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.repos.ExerciseMuscleRepository;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseMuscleService;
import com.gunyoung.tmb.testutil.ExerciseMuscleTest;
import com.gunyoung.tmb.testutil.MuscleTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * ExerciseMuscleService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class ExerciseMuscleServiceTest {
	
	@Autowired
	MuscleRepository muscleRepository;
	
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
	 *  ExerciseMuscle findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 ExerciseMuscle 찾기 -> 해당 id의 exerciseMuscle 없음")
	void findByIdNonExist() {
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
	void findByIdTest() {
		//Given
		Long exerciseMuscleId = exerciseMuscle.getId();
		
		//When
		ExerciseMuscle result = exerciseMuscleService.findById(exerciseMuscleId);
		
		//Then
		assertNotNull(result);
		
	}
	
	/*
	 *  ExerciseMuscle save(ExerciseMuscle exerciseMuscle)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExerciseMuscle 수정하기 -> 정상")
	void mergeTest() {
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
	void saveTest() {
		//Given
		ExerciseMuscle newExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance();
		Long givenExerciseMuscleNum = exerciseMuscleRepository.count();
		
		//When
		exerciseMuscleService.save(newExerciseMuscle);
		
		//Then
		assertEquals(givenExerciseMuscleNum + 1, exerciseMuscleRepository.count());
	}
	
	/*
	 *  void delete(ExerciseMuscle exerciseMuscle)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExerciseMuscle 삭제하기 -> 정상")
	void deleteTest() {
		//Given
		ExerciseMuscle existExerciseMuscle = exerciseMuscleRepository.findAll().get(0);
		Long givenExerciseMuscleNum = exerciseMuscleRepository.count();
		
		//When
		exerciseMuscleService.delete(existExerciseMuscle);
		
		//Then
		assertEquals(givenExerciseMuscleNum - 1, exerciseMuscleRepository.count());
	}
	
	/*
	 * void deleteAllByMuscleId(Long muscleId)
	 */
	
	@Test
	@Transactional
	@DisplayName("Muscle ID를 통한 ExerciseMuscle 모두 삭제 -> 정상")
	void deleteAllByMuscleIdTest() {
		//Given
		Long addExerciseMuscleNum = Long.valueOf(6);
		addExerciseMuscles(addExerciseMuscleNum);
		
		Muscle muscle = MuscleTest.getMuscleInstance();
		muscleRepository.save(muscle);
		Long muscleId = muscle.getId();
		
		setMuscleForAllExerciseMuscles(muscle);
		
		//When
		exerciseMuscleService.deleteAllByMuscleId(muscleId);
		
		//Then
		assertEquals(0, exerciseMuscleRepository.count());
	}
	
	private void addExerciseMuscles(Long exerciseMuscleNum) {
		List<ExerciseMuscle> exerciseMuscles = new ArrayList<>();
		for(int i=0;i<exerciseMuscleNum;i++) {
			ExerciseMuscle em = ExerciseMuscleTest.getExerciseMuscleInstance();
			exerciseMuscles.add(em);
		}
		exerciseMuscleRepository.saveAll(exerciseMuscles);
	}
	
	private void setMuscleForAllExerciseMuscles(Muscle muscle) {
		List<ExerciseMuscle> exerciseMuscles = exerciseMuscleRepository.findAll();
		for(ExerciseMuscle em: exerciseMuscles) {
			em.setMuscle(muscle);
		}
		exerciseMuscleRepository.saveAll(exerciseMuscles);
	}
}
