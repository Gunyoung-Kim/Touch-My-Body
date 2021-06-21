package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class ExerciseServiceTest {

	private static final int INIT_EXERCISE_NUM = 30;
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Autowired
	ExerciseService exerciseService;
	
	@BeforeEach
	void setup() {
		List<Exercise> list = new ArrayList<>();
		for(int i=1;i<=INIT_EXERCISE_NUM;i++) {
			Exercise exercise = Exercise.builder()
									    .name("Exercies" +i)
									    .description("Description" + i)
									    .caution("Caution" + i)
									    .movement("Movement" + i)
									    .target(TargetType.CHEST)
									    .build();
			list.add(exercise);
		}
		exerciseRepository.saveAll(list);
	}
	
	@AfterEach
	void tearDown() {
		exerciseRepository.deleteAll();
	}
	
	/*
	 *  public Exercise findById(Long id)
	 */
	@Test
	@DisplayName("id로 Exercise 찾기 -> 해당 id의 Exercise 없음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<Exercise> list = exerciseRepository.findAll();
		
		for(Exercise c: list) {
			maxId = Math.max(maxId, c.getId());
		}
		
		//When
		Exercise result = exerciseService.findById(maxId+ 1000);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@DisplayName("id로 Exercise 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existId = exerciseRepository.findAll().get(0).getId();
		
		//When
		Exercise result = exerciseService.findById(existId);
		
		//Then
		assertEquals(result != null, true);
	}
	
	/*
	 *  public Exercise save(Exercise Exercise)
	 */
	
	@Test
	@Transactional
	@DisplayName("Exercise 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		Exercise existExercise = exerciseRepository.findAll().get(0);
		Long id = existExercise.getId();
		existExercise.setDescription("Changed Description");
		
		//When
		exerciseService.save(existExercise);
		
		//Then
		Exercise result = exerciseRepository.getById(id);
		assertEquals(result.getDescription(),"Changed Description");
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise 추가하기 -> 정상")
	public void saveTest() {
		//Given
		Exercise newExercise = Exercise.builder()
								 	   .name("New Exercies")
								 	   .description("New Description")
								 	   .caution("New Caution")
									   .movement("New Movement")
									   .target(TargetType.CHEST)
									   .build();
		Long beforeNum = exerciseRepository.count();
		
		//When
		exerciseService.save(newExercise);
		
		//Then
		assertEquals(beforeNum+1,exerciseRepository.count());
	}
	
	/*
	 *  public void delete(Exercise Exercise)
	 */
	
	@Test
	@Transactional
	@DisplayName("Exercise 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		Exercise existExercise = exerciseRepository.findAll().get(0);
		Long beforeNum = exerciseRepository.count();
		
		//When
		exerciseService.delete(existExercise);
		
		//Then
		assertEquals(beforeNum-1,exerciseRepository.count());
	}
}
