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

import com.gunyoung.tmb.domain.exercise.ExerciseSpace;
import com.gunyoung.tmb.repos.ExerciseSpaceRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseSpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class ExerciseSpaceServiceTest {

	private static final int INIT_EXERCISE_SPACE_NUM = 30;
	
	@Autowired
	ExerciseSpaceRepository exerciseSpaceRepository;
	
	@Autowired
	ExerciseSpaceService exerciseSpaceService;
	
	@BeforeEach
	void setup() {
		List<ExerciseSpace> list = new ArrayList<>();
		for(int i=1;i<=INIT_EXERCISE_SPACE_NUM;i++) {
			ExerciseSpace exerciseSpace = ExerciseSpace.builder()
									 				.build();
									 
			list.add(exerciseSpace);
		}
		exerciseSpaceRepository.saveAll(list);
	}
	
	@AfterEach
	void tearDown() {
		exerciseSpaceRepository.deleteAll();
	}
	
	/*
	 *  public ExerciseSpace findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 ExerciseSpace 찾기 -> 해당 id의 exerciseSpace 없음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<ExerciseSpace> list = exerciseSpaceRepository.findAll();
		
		for(ExerciseSpace c: list) {
			maxId = Math.max(maxId, c.getId());
		}
		
		//When
		ExerciseSpace result = exerciseSpaceService.findById(maxId+ 1000);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 ExerciseSpace 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		ExerciseSpace exerciseSpace = exerciseSpaceRepository.findAll().get(0);
		Long id = exerciseSpace.getId();
		
		//When
		ExerciseSpace result = exerciseSpaceService.findById(id);
		
		//Then
		
		assertEquals(result != null, true);
		
	}
	
	/*
	 *  public ExerciseSpace save(ExerciseSpace exerciseSpace)
	 *  변경할 사항 없어서 패스 
	 */
	
	@Test
	@Transactional
	@DisplayName("ExerciseSpace 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		
		
		//When
		
		
		//Then
		
	}
	
	@Test
	@Transactional
	@DisplayName("ExerciseSpace 추가하기 -> 정상")
	public void saveTest() {
		//Given
		ExerciseSpace newExerciseSpace = ExerciseSpace.builder()
									.build();
		Long beforeNum = exerciseSpaceRepository.count();
		
		//When
		exerciseSpaceService.save(newExerciseSpace);
		
		//Then
		assertEquals(beforeNum+1,exerciseSpaceRepository.count());
	}
	
	/*
	 *  public void delete(ExerciseSpace exerciseSpace)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExerciseSpace 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		ExerciseSpace existExerciseSpace = exerciseSpaceRepository.findAll().get(0);
		Long beforeNum = exerciseSpaceRepository.count();
		
		//When
		exerciseSpaceService.delete(existExerciseSpace);
		
		//Then
		assertEquals(beforeNum-1,exerciseSpaceRepository.count());
	}
}
