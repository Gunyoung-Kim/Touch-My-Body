package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.repos.ExerciseMuscleRepository;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.util.ExerciseTest;
import com.gunyoung.tmb.util.MuscleTest;
import com.gunyoung.tmb.util.TargetTypeTest;
import com.gunyoung.tmb.util.UserExerciseTest;
import com.gunyoung.tmb.util.tag.Integration;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * ExerciseService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
public class ExerciseServiceTest {
	
	@Autowired
	UserExerciseRepository userExerciseRepository;
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Autowired
	MuscleRepository muscleRepository;
	
	@Autowired
	ExerciseMuscleRepository exerciseMuscleRepository;
	
	@Autowired
	ExerciseService exerciseService;
	
	private Exercise exercise;
	
	@BeforeEach
	void setup() {
		exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
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
		long nonExistExerciseId = ExerciseTest.getNonExistExerciseId(exerciseRepository);
		
		//When
		Exercise result = exerciseService.findById(nonExistExerciseId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("id로 Exercise 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existExerciseId = exercise.getId();
		
		//When
		Exercise result = exerciseService.findById(existExerciseId);
		
		//Then
		assertNotNull(result);
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
		assertNull(result);
	}
	
	@Test
	@DisplayName("name으로 Exercise 찾기 -> 정상")
	public void findByNameTest() {
		//Given
		String existName = exercise.getName();
		
		//When
		Exercise result = exerciseService.findByName(existName);
		
		//Then
		assertNotNull(result);
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
		
		ExerciseTest.addNewExercisesInDBByNum(10, exerciseRepository);
		
		long givenExerciseNum = exerciseRepository.count();
		
		//When
		Page<Exercise> result =exerciseService.findAllInPage(pageNumber, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE);
		
		//Then
		assertEquals(Math.min(givenExerciseNum, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE), result.getContent().size());
	}
	
	/*
	 *  public Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int page_size)
	 */
	
	@Test
	@Transactional
	@DisplayName(" 이름에 키워드를 포함하는 모든 운동 페이지로 가져오기 -> 정상, 모든 Exercise 만족 키워드")
	public void findAllWithNameKeywordInPageAll() {
		//Given
		Integer pageNumber = 1;
		String keywordForAllExercise = ExerciseTest.getExerciseInstance().getName();
		
		ExerciseTest.addNewExercisesInDBByNum(5, exerciseRepository);
		
		long givenExerciseNum = exerciseRepository.count();
		
		//When
		Page<Exercise> result = exerciseService.findAllWithNameKeywordInPage(keywordForAllExercise, pageNumber, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE);
		
		//Then
		assertEquals(Math.min(givenExerciseNum, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE), result.getContent().size());
	}
	
	@Test
	@Transactional
	@DisplayName(" 이름에 키워드를 포함하는 모든 운동 페이지로 가져오기 -> 정상, 오직 하나의 Exercise 만족 키워드")
	public void findAllWithNameKeywordInPageOnlyOne() {
		//Given
		Integer pageNumber = 1;
		String keywordOnlyOneContains = "I am Only one Exercise";
		
		ExerciseTest.addNewExercisesInDBByNum(5, exerciseRepository);
		
		Exercise exerciseForOnlyOne = ExerciseTest.getExerciseInstance();
		exerciseForOnlyOne.setName(keywordOnlyOneContains);
		exerciseRepository.save(exerciseForOnlyOne);
		
		//When
		Page<Exercise> result = exerciseService.findAllWithNameKeywordInPage(keywordOnlyOneContains, pageNumber, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE);
		
		//Then
		assertEquals(1, result.getContent().size());
	}
	
	@Test
	@Transactional
	@DisplayName(" 이름에 키워드를 포함하는 모든 운동 페이지로 가져오기 -> 정상, 키워드 만족하는 Exercise 없음")
	public void findAllWithNameKeywordInPageNothing() {
		//Given
		Integer pageNumber = 1;
		String noContainsKeyword = "none!!!!";
		
		ExerciseTest.addNewExercisesInDBByNum(5, exerciseRepository);
		
		//When
		Page<Exercise> noResult = exerciseService.findAllWithNameKeywordInPage(noContainsKeyword, pageNumber, PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE);
		
		//Then
		assertEquals(0, noResult.getContent().size());
	}
	
	/*
	 *  public Map<String, List<String>> getAllExercisesNamewithSorting()
	 */
	
	@Test
	@Transactional
	@DisplayName("모든 운동 이름과 주 타겟으로 분류해서 가져오기 -> 정상, 단 하나의 Exercise만 만족하는 target 확인")
	public void getAllExercisesNamewithSortingTest() {
		//Given
		TargetType targetTypeForOnlyOne = TargetTypeTest.getAnotherTargetType(exercise.getTarget());
		Exercise exerciseAnotherTarget = ExerciseTest.getExerciseInstance("only one", targetTypeForOnlyOne);
		exerciseRepository.save(exerciseAnotherTarget);
		
		ExerciseTest.addNewExercisesInDBByNum(5, exerciseRepository);
		
		//When
		Map<String,List<String>> result = exerciseService.getAllExercisesNamewithSorting();
		
		//Then
		assertEquals(1, result.get(targetTypeForOnlyOne.getKoreanName()).size());
	}
	
	/*
	 *  public Exercise save(Exercise Exercise)
	 */
	
	@Test
	@DisplayName("Exercise 수정하기 -> 정상, 변화 확인")
	public void mergeTestCheckChange() {
		//Given
		String changeDescription = "Changed Description";
		Long exerciseId = exercise.getId();
		exercise.setDescription(changeDescription);
		
		//When
		exerciseService.save(exercise);
		
		//Then
		Exercise result = exerciseRepository.findById(exerciseId).get();
		assertEquals(changeDescription, result.getDescription());
	}
	
	@Test
	@DisplayName("Exercise 수정하기 -> 정상, 개수 변화 없음 확인")
	public void mergeTestCheckCount() {
		//Given
		String changeDescription = "Changed Description";
		exercise.setDescription(changeDescription);
		
		long givenExerciseNum = exerciseRepository.count();
		
		//When
		exerciseService.save(exercise);
		
		//Then
		assertEquals(givenExerciseNum, exerciseRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise 추가하기 -> 정상")
	public void saveTest() {
		//Given
		Exercise newExercise = ExerciseTest.getExerciseInstance("newExercise", TargetType.ARM);
		Long givenExerciseNum = exerciseRepository.count();
		
		//When
		exerciseService.save(newExercise);
		
		//Then
		assertEquals(givenExerciseNum + 1,exerciseRepository.count());
	}
	
	/*
	 *  public Exercise saveWithSaveExerciseDTO(SaveExerciseDTO dto)
	 */
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 Muscle 없음, MuscleNotFoundedException throw 확인")
	public void saveWithSaveExerciseDTOMuscleNotFoundedCheckMuscleNotFoundedException() {
		//Given
		String nonExistMuscleName =  "none";
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExericse");
		dto.getMainMuscles().add(nonExistMuscleName);
		
		Exercise exercise = Exercise.builder().build();
		
		//When,Then
		assertThrows(MuscleNotFoundedException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		});
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 Muscle 없음, Exercise 개수 변화 없음 확인")
	public void saveWithSaveExerciseDTOMuscleNotFoundedCheckExerciseNum() {
		//Given
		String nonExistMuscleName =  "none";
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExericse");
		dto.getMainMuscles().add(nonExistMuscleName);
		
		long givenExerciseNum =  exerciseRepository.count();
		
		Exercise exercise = Exercise.builder().build();
		
		//When
		assertThrows(MuscleNotFoundedException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		});
		
		//Then
		assertEquals(givenExerciseNum, exerciseRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 Muscle 없음, Exercise 개수 변화 없음 확인")
	public void saveWithSaveExerciseDTOMuscleNotFoundedCheckExerciseMuscleNum() {
		//Given
		String nonExistMuscleName =  "none";
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExericse");
		dto.getMainMuscles().add(nonExistMuscleName);
		
		long givenExerciseMuscleNum = exerciseMuscleRepository.count();
		
		Exercise exercise = Exercise.builder().build();
		
		//When
		assertThrows(MuscleNotFoundedException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		});
		
		//Then
		assertEquals(givenExerciseMuscleNum, exerciseMuscleRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 TargetType 없음, TargetTypeNotFoundedException throw 확인")
	public void saveWithSaveExerciseDTOTargetTypeNotFoundedCheckTargetTypeNotFoundedException() {
		//Given
		String existMuscleName = "new Muscle";
		Muscle muscle = MuscleTest.getMuscleInstance(existMuscleName, TargetType.ARM);
		muscleRepository.save(muscle);
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExercise");
		dto.setTarget("none");
		dto.getMainMuscles().add(existMuscleName);
		
		Exercise exercise = Exercise.builder().build();
		
		//When, Then
		assertThrows(TargetTypeNotFoundedException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		});
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 TargetType 없음, Exercise 개수 변화 없음 확인")
	public void saveWithSaveExerciseDTOTargetTypeNotFoundedCheckExerciseNum() {
		//Given
		String existMuscleName = "new Muscle";
		Muscle muscle = MuscleTest.getMuscleInstance(existMuscleName, TargetType.ARM);
		muscleRepository.save(muscle);
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExercise");
		dto.setTarget("none");
		dto.getMainMuscles().add(existMuscleName);
		
		long givenExerciseNum =  exerciseRepository.count();
		
		Exercise exercise = Exercise.builder().build();
		
		//When
		assertThrows(TargetTypeNotFoundedException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		});
		
		//Then
		assertEquals(givenExerciseNum, exerciseRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 TargetType 없음, ExerciseMuscle 개수 변화 없음 확인")
	public void saveWithSaveExerciseDTOTargetTypeNotFounded() {
		//Given
		String existMuscleName = "new Muscle";
		Muscle muscle = MuscleTest.getMuscleInstance(existMuscleName, TargetType.ARM);
		muscleRepository.save(muscle);
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExercise");
		dto.setTarget("none");
		dto.getMainMuscles().add(existMuscleName);
		
		long givenExerciseMuscleNum = exerciseMuscleRepository.count();
		
		Exercise exercise = Exercise.builder().build();
		
		//When
		assertThrows(TargetTypeNotFoundedException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		});
		
		//Then
		assertEquals(givenExerciseMuscleNum, exerciseMuscleRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 정상, Exercise 개수 추가 확인")
	public void saveWithSaveExerciseDTOTestCheckExerciseNum() {
		//Given
		String existMuscleName = "new Muscle";
		Muscle muscle = MuscleTest.getMuscleInstance(existMuscleName, TargetType.ARM);
		muscleRepository.save(muscle);
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExercise");
		dto.getMainMuscles().add(existMuscleName);
		
		long givenExerciseNum =  exerciseRepository.count();
		Exercise exercise = Exercise.builder().build();
		
		//When
		exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		
		//Then
		assertEquals(givenExerciseNum + 1, exerciseRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 정상, ExerciseMuscle 추가 확인")
	public void saveWithSaveExerciseDTOTestCheckExerciseMuscleNum() {
		//Given
		String existMuscleName = "new Muscle";
		Muscle muscle = MuscleTest.getMuscleInstance(existMuscleName, TargetType.ARM);
		muscleRepository.save(muscle);
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExercise");
		dto.getMainMuscles().add(existMuscleName);
		
		long givenExerciseMuscleNum = exerciseMuscleRepository.count();
		Exercise exercise = Exercise.builder().build();
		
		//When
		exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		
		//Then
		assertEquals(givenExerciseMuscleNum + 1, exerciseMuscleRepository.count());
	}
	
	/*
	 *  public void delete(Exercise Exercise)
	 */
	
	@Test
	@DisplayName("Exercise 삭제하기 -> 정상, Exercise Delete Check")
	public void deleteTestCheckExerciseDelete() {
		//Given
		Long givenExerciseNum = exerciseRepository.count();
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		assertEquals(givenExerciseNum - 1, exerciseRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise 삭제하기 -> 정상, 관련 UserExercise Delete check")
	public void deleteTestCheckUserExerciseDelete() {
		//Given
		Long givenUserExerciseNum = Long.valueOf(9);
		saveUserExercisesWithExercise(givenUserExerciseNum, exercise);
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		assertEquals(0, userExerciseRepository.count());
	}
	
	private void saveUserExercisesWithExercise(Long givenUserExerciseNum, Exercise exercise) {
		List<UserExercise> userExercises = new ArrayList<>();
		for(int i=0; i < givenUserExerciseNum; i++) {
			UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
			userExercise.setExercise(exercise);
			userExercises.add(userExercise);
		}
		
		userExerciseRepository.saveAll(userExercises);
	}
	
	
	/*
	 *  public long countAll()
	 */
	
	@Test
	@DisplayName("모든 Exercise 개수 세기 ->정상")
	public void countAllTest() {
		//Given
		ExerciseTest.addNewExercisesInDBByNum(10, exerciseRepository);
		long givenExerciseNum = exerciseRepository.count();
		
		//When
		long result = exerciseService.countAll();
		
		//Then
		assertEquals(givenExerciseNum, result);
	}
	
	/*
	 * public long countAllWithNameKeyword(String nameKeyword)
	 */
	@Test
	@DisplayName("이름 키워드를 만족하는 Exercise 개수 세기 -> 정상, 모든 Exercise가 만족하는 키워드")
	public void countAllWithNameKeywordTestAll() {
		//Given
		String allContainsKeyword = ExerciseTest.getExerciseInstance().getName();
		
		ExerciseTest.addNewExercisesInDBByNum(10, exerciseRepository);
		
		long givenExerciseNum = exerciseRepository.count();
		
		//When
		long result = exerciseService.countAllWithNameKeyword(allContainsKeyword);
		
		//Then
		assertEquals(givenExerciseNum, result);
	}
	
	@Test
	@DisplayName("이름 키워드를 만족하는 Exercise 개수 세기 -> 정상, 오직 하나의 Exercise가 만족하는 키워드")
	public void countAllWithNameKeywordTestOnlyOne() {
		//Given
		String onlyOneContainsKeyword = "I am only one";
		Exercise onlyOneExercise = ExerciseTest.getExerciseInstance(onlyOneContainsKeyword, TargetType.ARM);
		exerciseRepository.save(onlyOneExercise);
		
		ExerciseTest.addNewExercisesInDBByNum(10, exerciseRepository);
		
		//When
		long result = exerciseService.countAllWithNameKeyword(onlyOneContainsKeyword);
		
		//Then
		assertEquals(1, result);
	}
	
	@Test
	@DisplayName("이름 키워드를 만족하는 Exercise 개수 세기 -> 정상, 모든 Exercise가 만족하지 않는 키워드")
	public void countAllWithNameKeywordTestNothing() {
		//Given
		String noContainsKeyword = "none!!!!";
		
		ExerciseTest.addNewExercisesInDBByNum(10, exerciseRepository);
		
		//When
		long result = exerciseService.countAllWithNameKeyword(noContainsKeyword);
		
		//Then
		assertEquals(0, result);
	}
	
	/*
	 *  public ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId)
	 */
	
	@Test
	@Transactional
	@DisplayName("Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환 -> 해당 exercise 없음")
	public void getExerciseForInfoViewDTOByExerciseIdNonExist() {
		//Given
		Long nonExistId = ExerciseTest.getNonExistExerciseId(exerciseRepository);
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환 -> 정상")
	public void getExerciseForInfoViewDTOByExerciseIdTest() {
		//Given
		Long existId = exercise.getId();
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(existId);
		
		//Then
		assertNotNull(result);
	}
}
