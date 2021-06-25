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

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class ExercisePostServiceTest {

	private static final int INIT_EXERCISE_POST_NUM = 30;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	@Autowired
	ExercisePostService exercisePostService;
	
	@BeforeEach
	void setup() {
		List<ExercisePost> list = new ArrayList<>();
		for(int i=1;i<=INIT_EXERCISE_POST_NUM;i++) {
			ExercisePost exercisePost = ExercisePost.builder()
									 .title("title" +i)
									 .contents("contents"+i)
									 .build();
									 
			list.add(exercisePost);
		}
		exercisePostRepository.saveAll(list);
	}
	
	@AfterEach
	void tearDown() {
		exercisePostRepository.deleteAll();
	}
	
	/*
	 *  public ExercisePost findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 ExercisePost 찾기 -> 해당 id의 exercisePost 없음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<ExercisePost> list = exercisePostRepository.findAll();
		
		for(ExercisePost c: list) {
			maxId = Math.max(maxId, c.getId());
		}
		
		//When
		ExercisePost result = exercisePostService.findById(maxId+ 1000);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 ExercisePost 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		ExercisePost exercisePost = exercisePostRepository.findAll().get(0);
		Long id = exercisePost.getId();
		
		//When
		ExercisePost result = exercisePostService.findById(id);
		
		//Then
		
		assertEquals(result != null, true);
		
	}
	
	/*
	 *  public ExercisePost save(ExercisePost exercisePost)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		ExercisePost existExercisePost = exercisePostRepository.findAll().get(0);
		Long id = existExercisePost.getId();
		existExercisePost.setTitle("Changed Title");
		
		//When
		exercisePostService.save(existExercisePost);
		
		//Then
		ExercisePost result = exercisePostRepository.findById(id).get();
		assertEquals(result.getTitle(),"Changed Title");
	}
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 추가하기 -> 정상")
	public void saveTest() {
		//Given
		ExercisePost newExercisePost = ExercisePost.builder()
									.title("New Title")
									.contents("New contents")
									.build();
		Long beforeNum = exercisePostRepository.count();
		
		//When
		exercisePostService.save(newExercisePost);
		
		//Then
		assertEquals(beforeNum+1,exercisePostRepository.count());
	}
	
	/*
	 *  public void delete(ExercisePost exercisePost)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		ExercisePost existExercisePost = exercisePostRepository.findAll().get(0);
		Long beforeNum = exercisePostRepository.count();
		
		//When
		exercisePostService.delete(existExercisePost);
		
		//Then
		assertEquals(beforeNum-1,exercisePostRepository.count());
	}
}
