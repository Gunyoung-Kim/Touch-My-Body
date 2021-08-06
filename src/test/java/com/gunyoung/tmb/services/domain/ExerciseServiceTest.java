package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.repos.ExerciseMuscleRepository;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.utils.PageUtil;

@SpringBootTest
public class ExerciseServiceTest {

	private static final int INIT_EXERCISE_NUM = 30;
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Autowired
	MuscleRepository muscleRepository;
	
	@Autowired
	ExerciseMuscleRepository exerciseMuscleRepository;
	
	@Autowired
	ExerciseService exerciseService;
	
	@BeforeEach
	void setup() {
		List<Exercise> list = new ArrayList<>();
		for(int i=1;i<=INIT_EXERCISE_NUM;i++) {
			Exercise exercise = Exercise.builder()
									    .name("Exercise" +i)
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
	 *  public Exercise findByName(String name)
	 */
	
	@Test
	@DisplayName("name으로 Exercise 찾기 -> 해당 name의 Exercise 없음")
	public void findByNameNonExist() {
		//Given
		String nonExistName = "None";
		
		//When
		Exercise result = exerciseService.findByName(nonExistName);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@DisplayName("name으로 Exercise 찾기 -> 정상")
	public void findByNameTest() {
		//Given
		String existName = "Exercise1";
		
		//When
		Exercise result = exerciseService.findByName(existName);
		
		//Then
		assertEquals(result != null, true);
	}
	
	/*
	 *  public Page<Exercise> findAllInPage(Integer pageNumber,int page_size)
	 */
	
	@Test
	@Transactional
	@DisplayName("모든 운동 페이지로 가져오기 ->정상")
	public void findAllInPageTest() {
		//Given
		Integer pageNumber = 1;
		
		//When
		Page<Exercise> result =exerciseService.findAllInPage(pageNumber, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE);
		
		//Then
		assertEquals(result.getContent().size(),Math.min(INIT_EXERCISE_NUM, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE));
	}
	
	/*
	 *  public Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int page_size)
	 */
	
	@Test
	@Transactional
	@DisplayName(" 이름에 키워드를 포함하는 모든 운동 페이지로 가져오기 -> 정상")
	public void findAllWithNameKeywordInPage() {
		//Given
		Integer pageNumber = 1;
		String allContainsKeyword = "Exercise";
		String noContainsKeyword = "none!!!!";
		String onlyOneContainsKeyword = String.valueOf(INIT_EXERCISE_NUM);
		
		//When
		
		Page<Exercise> allResult = exerciseService.findAllWithNameKeywordInPage(allContainsKeyword, pageNumber, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE);
		Page<Exercise> noResult = exerciseService.findAllWithNameKeywordInPage(noContainsKeyword, pageNumber, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE);
		Page<Exercise> oneResult = exerciseService.findAllWithNameKeywordInPage(onlyOneContainsKeyword, pageNumber, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE);
		
		//Then
		assertEquals(allResult.getContent().size(),Math.min(INIT_EXERCISE_NUM, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE));
		assertEquals(noResult.getContent().size(),0);
		assertEquals(oneResult.getContent().size(),1);
	}
	
	/*
	 *  public Map<String, List<String>> getAllExercisesNamewithSorting()
	 */
	
	@Test
	@Transactional
	@DisplayName("모든 운동 이름과 주 타겟으로 분류해서 가져오기 -> 정상")
	public void getAllExercisesNamewithSortingTest() {
		//Given
		Exercise exercise = Exercise.builder()
			    .name("ArmExercies")
			    .description("Description")
			    .caution("Caution")
			    .movement("Movement")
			    .target(TargetType.ARM)
			    .build();
		exerciseRepository.save(exercise);
		
		//When
		Map<String,List<String>> result = exerciseService.getAllExercisesNamewithSorting();
		
		//Then
		assertEquals(result.size(),2);
		assertEquals(result.get(TargetType.ARM.getKoreanName()).size(),1);
		assertEquals(result.get(TargetType.CHEST.getKoreanName()).size(),INIT_EXERCISE_NUM);
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
	 *  public Exercise saveWithSaveExerciseDTO(SaveExerciseDTO dto)
	 */
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 Muscle 없음")
	public void saveWithSaveExerciseDTOMuscleNotFounded() {
		//Given
		String nonExistMuscleName =  "none";
		
		SaveExerciseDTO dto = SaveExerciseDTO.builder()
				.name("newName")
				.description("newDes")
				.caution("newCau")
				.movement("newMove")
				.target(TargetType.BACK.getKoreanName())
				.build();
		
		dto.getMainMuscles().add(nonExistMuscleName);
		long exerciseNum =  exerciseRepository.count();
		long exerciseMuscleNum = exerciseMuscleRepository.count();
		
		Exercise exercise = new Exercise();
		//When,Then
		assertThrows(MuscleNotFoundedException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		});
		
		
		assertEquals(exerciseNum, exerciseRepository.count());
		assertEquals(exerciseMuscleNum,exerciseMuscleRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 TargetType 없음")
	public void saveWithSaveExerciseDTOTargetTypeNotFounded() {
		//Given
		String existMuscleName = "new Muscle";
		Muscle muscle = Muscle.builder()
				.name(existMuscleName)
				.category(TargetType.ARM)
				.build();
		muscleRepository.save(muscle);
		
		
		SaveExerciseDTO dto = SaveExerciseDTO.builder()
				.name("newName")
				.description("newDes")
				.caution("newCau")
				.movement("newMove")
				.target("none")
				.build();
		dto.getMainMuscles().add(existMuscleName);
		
		long exerciseNum =  exerciseRepository.count();
		long exerciseMuscleNum = exerciseMuscleRepository.count();
		Exercise exercise = new Exercise();
		//When
		assertThrows(TargetTypeNotFoundedException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		});
		
		//Then
		
		assertEquals(exerciseNum, exerciseRepository.count());
		assertEquals(exerciseMuscleNum,exerciseMuscleRepository.count());
	}
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 정상")
	public void saveWithSaveExerciseDTOTest() {
		//Given
		String existMuscleName = "new Muscle";
		Muscle muscle = Muscle.builder()
				.name(existMuscleName)
				.category(TargetType.ARM)
				.build();
		muscleRepository.save(muscle);
		
		SaveExerciseDTO dto = SaveExerciseDTO.builder()
				.name("newName")
				.description("newDes")
				.caution("newCau")
				.movement("newMove")
				.target(TargetType.BACK.getKoreanName())
				.build();
		dto.getMainMuscles().add(existMuscleName);
		
		long exerciseNum =  exerciseRepository.count();
		long exerciseMuscleNum = exerciseMuscleRepository.count();
		Exercise exercise = new Exercise();
		//When
		Exercise result = exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		
		//Then
		assertEquals(exerciseNum+1, exerciseRepository.count());
		assertEquals(result.getExerciseMuscles().size(),1);
		assertEquals(exerciseMuscleNum+1,exerciseMuscleRepository.count());
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
	
	/*
	 *  public long countAll()
	 */
	
	@Test
	@DisplayName("모든 Exercise 개수 세기 ->정상")
	public void countAllTest() {
		//Given
		
		//When
		long result = exerciseService.countAll();
		
		//Then
		assertEquals(result,INIT_EXERCISE_NUM);
	}
	
	/*
	 * public long countAllWithNameKeyword(String nameKeyword)
	 */
	@Test
	@DisplayName("이름 키워드를 만족하는 Exercise 개수 세기 -> 정상")
	public void countAllWithNameKeywordTest() {
		//Given
		String allContainsKeyword = "Exercise";
		String noContainsKeyword = "none!!!!";
		String onlyOneContainsKeyword = String.valueOf(INIT_EXERCISE_NUM);
		
		//When
		long resultAll = exerciseService.countAllWithNameKeyword(allContainsKeyword);
		long resultNone = exerciseService.countAllWithNameKeyword(noContainsKeyword);
		long resultOne = exerciseService.countAllWithNameKeyword(onlyOneContainsKeyword);
		
		//Then
		assertEquals(resultAll,INIT_EXERCISE_NUM);
		assertEquals(resultNone,0);
		assertEquals(resultOne,1);
	}
	
	/*
	 *  public ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId)
	 */
	
	@Test
	@Transactional
	@DisplayName("Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환 -> 해당 exercise 없음")
	public void getExerciseForInfoViewDTOByExerciseIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		for(Exercise e: exerciseRepository.findAll()) {
			nonExistId = Math.max(nonExistId, e.getId());
		}
		nonExistId++;
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(nonExistId);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환 -> 정상")
	public void getExerciseForInfoViewDTOByExerciseIdTest() {
		//Given
		Exercise existExercise = exerciseRepository.findAll().get(0);
		Long existId = existExercise.getId();
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(existId);
		
		//Then
		assertEquals(result != null,true);
	}
}
