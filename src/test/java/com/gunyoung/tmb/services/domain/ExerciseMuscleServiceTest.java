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
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseMuscleRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseMuscleService;

@SpringBootTest
public class ExerciseMuscleServiceTest {

	private static final int INIT_EXERCISE_MUSCLE_NUM = 30;
	
	@Autowired
	ExerciseMuscleRepository exerciseMuscleRepository;
	
	@Autowired
	ExerciseMuscleService exerciseMuscleService;
	
	@BeforeEach
	void setup() {
		List<ExerciseMuscle> list = new ArrayList<>();
		for(int i=1;i<=INIT_EXERCISE_MUSCLE_NUM;i++) {
			ExerciseMuscle exerciseMuscle = ExerciseMuscle.builder()
									 .muscleName("muscleName")
									 .isMain(true)
									 .build();
			list.add(exerciseMuscle);
		}
		exerciseMuscleRepository.saveAll(list);
	}
	
	@AfterEach
	void tearDown() {
		exerciseMuscleRepository.deleteAll();
	}
	
	private Muscle getMuscleInstance(String name, TargetType category) {
		Muscle muscle = Muscle.builder()
				.name(name)
				.category(category)
				.build();
		return muscle;
	}
	
	/*
	 *  public ExerciseMuscle findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 ExerciseMuscle 찾기 -> 해당 id의 exerciseMuscle 없음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<ExerciseMuscle> list = exerciseMuscleRepository.findAll();
		
		for(ExerciseMuscle c: list) {
			maxId = Math.max(maxId, c.getId());
		}
		
		//When
		ExerciseMuscle result = exerciseMuscleService.findById(maxId+ 1000);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 ExerciseMuscle 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		ExerciseMuscle exerciseMuscle = exerciseMuscleRepository.findAll().get(0);
		Long id = exerciseMuscle.getId();
		
		//When
		ExerciseMuscle result = exerciseMuscleService.findById(id);
		
		//Then
		
		assertEquals(result != null, true);
		
	}
	
	/*
	 *  public ExerciseMuscle save(ExerciseMuscle exerciseMuscle)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExerciseMuscle 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		ExerciseMuscle existExerciseMuscle = exerciseMuscleRepository.findAll().get(0);
		Long id = existExerciseMuscle.getId();
		existExerciseMuscle.setMain(false);
		
		//When
		exerciseMuscleService.save(existExerciseMuscle);
		
		//Then
		ExerciseMuscle result = exerciseMuscleRepository.findById(id).get();
		assertEquals(result.isMain(),false);
	}
	
	@Test
	@Transactional
	@DisplayName("ExerciseMuscle 추가하기 -> 정상")
	public void saveTest() {
		//Given
		ExerciseMuscle newExerciseMuscle = ExerciseMuscle.builder()
									.muscleName("muscleName")
									.isMain(true)
									.build();
		Long beforeNum = exerciseMuscleRepository.count();
		
		//When
		exerciseMuscleService.save(newExerciseMuscle);
		
		//Then
		assertEquals(beforeNum+1,exerciseMuscleRepository.count());
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
		Long beforeNum = exerciseMuscleRepository.count();
		
		//When
		exerciseMuscleService.delete(existExerciseMuscle);
		
		//Then
		assertEquals(beforeNum-1,exerciseMuscleRepository.count());
	}
	
	/*
	 * public List<ExerciseMuscle> getExerciseMuscleListFromExerciseAndMuscleListAndIsMain(Exercise exercise, List<Muscle> muscleList,boolean isMain)
	 */
	
	@Test
	@DisplayName("Exercise, Muscle List, isMain 인자를 이용해 ExerciseMuscle List 생성 후 반환 -> 정상")
	public void getExerciseMuscleListFromExerciseAndMuscleListAndIsMain() {
		//Given
		int muscleNum = 5;
		List<Muscle> muscleList = new ArrayList<>();
		
		for(int i=0;i< muscleNum; i++) {
			Muscle muscle = getMuscleInstance("muscle" + i,TargetType.ARM);
			muscleList.add(muscle);
		}
		
		Exercise exercise = new Exercise();
		boolean isMain = true;
		
		//When
		List<ExerciseMuscle> result = exerciseMuscleService.getExerciseMuscleListFromExerciseAndMuscleListAndIsMain(exercise, muscleList, isMain);
		
		//Then
		assertEquals(muscleNum, result.size());
	}
}
