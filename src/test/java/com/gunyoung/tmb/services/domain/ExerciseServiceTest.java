package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.precondition.PreconditionViolationException;
import com.gunyoung.tmb.repos.ExerciseMuscleRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.FeedbackRepository;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.testutil.ExerciseMuscleTest;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.FeedbackTest;
import com.gunyoung.tmb.testutil.MuscleTest;
import com.gunyoung.tmb.testutil.TargetTypeTest;
import com.gunyoung.tmb.testutil.UserExerciseTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * ExerciseService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class ExerciseServiceTest {
	
	@Autowired
	UserExerciseRepository userExerciseRepository;
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Autowired
	MuscleRepository muscleRepository;
	
	@Autowired
	ExerciseMuscleRepository exerciseMuscleRepository;
	
	@Autowired
	FeedbackRepository feedbackRepository;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
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
	 *  Exercise findById(Long id)
	 */
	@Test
	@DisplayName("id로 Exercise 찾기 -> 해당 id의 Exercise 없음")
	void findByIdNonExist() {
		//Given
		long nonExistExerciseId = ExerciseTest.getNonExistExerciseId(exerciseRepository);
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseService.findById(nonExistExerciseId);
		});
	}
	
	@Test
	@DisplayName("id로 Exercise 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long existExerciseId = exercise.getId();
		
		//When
		Exercise result = exerciseService.findById(existExerciseId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  Exercise findByName(String name)
	 */
	
	@Test
	@DisplayName("name으로 Exercise 찾기 -> 해당 name의 Exercise 없음")
	void findByNameNonExist() {
		//Given
		String nonExistName = "None";
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseService.findByName(nonExistName);
		});
	}
	
	@Test
	@DisplayName("name으로 Exercise 찾기 -> 정상")
	void findByNameTest() {
		//Given
		String existName = exercise.getName();
		
		//When
		Exercise result = exerciseService.findByName(existName);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  Page<Exercise> findAllInPage(Integer pageNumber,int page_size)
	 */
	
	@Test
	@DisplayName("모든 운동 페이지로 가져오기 -> pageNumber < 1")
	void findAllInPageNegativePageNumber() {
		//Given
		Integer pageNumber = -1;
		int pageSize = PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize();
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exerciseService.findAllInPage(pageNumber, pageSize);
		});
	}
	
	@Test
	@DisplayName("모든 운동 페이지로 가져오기 -> pageSize < 1")
	void findAllInPageNegativePageSize() {
		//Given
		Integer pageNumber = 1;
		int pageSize = -1;
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exerciseService.findAllInPage(pageNumber, pageSize);
		});
	}
	
	@Test
	@Transactional
	@DisplayName("모든 운동 페이지로 가져오기 ->정상")
	void findAllInPageTest() {
		//Given
		Integer pageNumber = 1;
		
		ExerciseTest.addNewExercisesInDBByNum(10, exerciseRepository);
		
		long givenExerciseNum = exerciseRepository.count();
		
		//When
		Page<Exercise> result = exerciseService.findAllInPage(pageNumber, PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize());
		
		//Then
		assertEquals(Math.min(givenExerciseNum, PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize()), result.getContent().size());
	}
	
	/*
	 *  Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int page_size)
	 */
	
	@Test
	@DisplayName("이름에 키워드를 포함하는 모든 운동 페이지로 가져오기 -> pageNumber < 1")
	void findAllWithNameKeywordInPageNegativePageNumber() {
		//Given
		Integer pageNumber = -1;
		int pageSize = PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize();
		String keyword = "keyword";
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exerciseService.findAllWithNameKeywordInPage(keyword, pageNumber, pageSize);
		});
	}
	
	@Test
	@DisplayName("이름에 키워드를 포함하는 모든 운동 페이지로 가져오기-> pageSize < 1")
	void findAllWithNameKeywordInPageNegativePageSize() {
		//Given
		Integer pageNumber = 1;
		int pageSize = -1;
		String keyword = "keyword";
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exerciseService.findAllWithNameKeywordInPage(keyword, pageNumber, pageSize);
		});
	}
	
	
	@Test
	@Transactional
	@DisplayName(" 이름에 키워드를 포함하는 모든 운동 페이지로 가져오기 -> 정상, 모든 Exercise 만족 키워드")
	void findAllWithNameKeywordInPageAll() {
		//Given
		Integer pageNumber = 1;
		String keywordForAllExercise = ExerciseTest.getExerciseInstance().getName();
		
		ExerciseTest.addNewExercisesInDBByNum(5, exerciseRepository);
		
		long givenExerciseNum = exerciseRepository.count();
		
		//When
		Page<Exercise> result = exerciseService.findAllWithNameKeywordInPage(keywordForAllExercise, pageNumber, PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize());
		
		//Then
		assertEquals(Math.min(givenExerciseNum, PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize()), result.getContent().size());
	}
	
	@Test
	@Transactional
	@DisplayName(" 이름에 키워드를 포함하는 모든 운동 페이지로 가져오기 -> 정상, 오직 하나의 Exercise 만족 키워드")
	void findAllWithNameKeywordInPageOnlyOne() {
		//Given
		Integer pageNumber = 1;
		String keywordOnlyOneContains = "I am Only one Exercise";
		
		ExerciseTest.addNewExercisesInDBByNum(5, exerciseRepository);
		
		Exercise exerciseForOnlyOne = ExerciseTest.getExerciseInstance();
		exerciseForOnlyOne.setName(keywordOnlyOneContains);
		exerciseRepository.save(exerciseForOnlyOne);
		
		//When
		Page<Exercise> result = exerciseService.findAllWithNameKeywordInPage(keywordOnlyOneContains, pageNumber, PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize());
		
		//Then
		assertEquals(1, result.getContent().size());
	}
	
	@Test
	@Transactional
	@DisplayName(" 이름에 키워드를 포함하는 모든 운동 페이지로 가져오기 -> 정상, 키워드 만족하는 Exercise 없음")
	void findAllWithNameKeywordInPageNothing() {
		//Given
		Integer pageNumber = 1;
		String noContainsKeyword = "none!!!!";
		
		ExerciseTest.addNewExercisesInDBByNum(5, exerciseRepository);
		
		//When
		Page<Exercise> noResult = exerciseService.findAllWithNameKeywordInPage(noContainsKeyword, pageNumber, PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize());
		
		//Then
		assertEquals(0, noResult.getContent().size());
	}
	
	/*
	 *  Map<String, List<String>> getAllExercisesNamewithSorting()
	 */
	
	@Test
	@Transactional
	@DisplayName("모든 운동 이름과 주 타겟으로 분류해서 가져오기 -> 정상, 단 하나의 Exercise만 만족하는 target 확인")
	void getAllExercisesNamewithSortingTest() {
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
	 *  Exercise save(Exercise Exercise)
	 */
	
	@Test
	@DisplayName("Exercise 수정하기 -> 정상, 변화 확인")
	void mergeTestCheckChange() {
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
	void mergeTestCheckCount() {
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
	void saveTest() {
		//Given
		Exercise newExercise = ExerciseTest.getExerciseInstance("newExercise", TargetType.ARM);
		Long givenExerciseNum = exerciseRepository.count();
		
		//When
		exerciseService.save(newExercise);
		
		//Then
		assertEquals(givenExerciseNum + 1,exerciseRepository.count());
	}
	
	/*
	 *  Exercise saveWithSaveExerciseDTO(SaveExerciseDTO dto)
	 */
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 Muscle 없음, 그래도 Exercise 추가 확인")
	void saveWithSaveExerciseDTOMuscleNotFoundedCheckMuscleNotFoundedException() {
		//Given
		String nonExistMuscleName =  "none";
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExericse");
		dto.getMainMuscles().add(nonExistMuscleName);
		
		Exercise exercise = Exercise.builder()
				.build();
		
		long beforeExerciseNum = exerciseRepository.count();
		
		//When
		exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		
		//Then
		assertEquals(beforeExerciseNum + 1, exerciseRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 Muscle 없음, 그래도 exercise 추가 확인")
	void saveWithSaveExerciseDTOMuscleNotFoundedCheckExerciseNum() {
		//Given
		String nonExistMuscleName =  "none";
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExericse");
		dto.getMainMuscles().add(nonExistMuscleName);
		
		long beforeExerciseNum =  exerciseRepository.count();
		
		Exercise exercise = Exercise.builder().build();
		
		//When
		exerciseService.saveWithSaveExerciseDTO(exercise,dto);
		
		
		//Then
		assertEquals(beforeExerciseNum + 1, exerciseRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 Muscle 없음, Exercise 개수 추가 확인")
	void saveWithSaveExerciseDTOMuscleNotFoundedCheckExerciseMuscleNum() {
		//Given
		String nonExistMuscleName =  "none";
		
		SaveExerciseDTO dto = ExerciseTest.getSaveExerciseDTOInstance("newExericse");
		dto.getMainMuscles().add(nonExistMuscleName);
		
		Exercise exercise = Exercise.builder()
				.build();
		
		//When
		exerciseService.saveWithSaveExerciseDTO(exercise, dto);
		
		
		//Then
		assertNotNull(exercise.getId());
	}
	
	@Test
	@Transactional
	@DisplayName("SaveExerciseDTO로 Exercise 추가하기 -> 해당 TargetType 없음, TargetTypeNotFoundedException throw 확인")
	void saveWithSaveExerciseDTOTargetTypeNotFoundedCheckTargetTypeNotFoundedException() {
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
	void saveWithSaveExerciseDTOTargetTypeNotFoundedCheckExerciseNum() {
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
	void saveWithSaveExerciseDTOTargetTypeNotFounded() {
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
	void saveWithSaveExerciseDTOTestCheckExerciseNum() {
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
	void saveWithSaveExerciseDTOTestCheckExerciseMuscleNum() {
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
	 *  void delete(Exercise Exercise)
	 */
	
	@Test
	@DisplayName("Exercise 삭제하기 -> 정상, Exercise Delete Check")
	void deleteTestCheckExerciseDelete() {
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
	void deleteTestCheckUserExerciseDelete() {
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
	
	@Test
	@Transactional
	@DisplayName("Exercise 삭제하기 -> 정상, Check Feedback Delete")
	void deleteTestCheckFeedbackDelete() {
		//Given
		Feedback feedback = FeedbackTest.getFeedbackInstance();
		feedback.setExercise(exercise);
		feedbackRepository.save(feedback);
		
		Long feedbackId = feedback.getId();
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		assertFalse(feedbackRepository.existsById(feedbackId));
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise 삭제하기 -> 정상, Check ExerciseMuscle Delete")
	void deleteTestCheckExerciseMuscleDelete() {
		//Given
		ExerciseMuscle exerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance();
		exerciseMuscle.setExercise(exercise);
		exerciseMuscleRepository.save(exerciseMuscle);
		
		Long exerciseMuscleId = exerciseMuscle.getId();
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		assertFalse(exerciseMuscleRepository.existsById(exerciseMuscleId));
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise 삭제하기 -> 정상, Check ExercisePost Delete")
	void deleteTestCheckExercisePostDelete() {
		//Given
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePost.setExercise(exercise);
		exercisePostRepository.save(exercisePost);
		
		Long exercisePostId = exercisePost.getId();
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		assertFalse(exercisePostRepository.existsById(exercisePostId));
	}
	
	/*
	 *  long countAll()
	 */
	
	@Test
	@DisplayName("모든 Exercise 개수 세기 ->정상")
	void countAllTest() {
		//Given
		ExerciseTest.addNewExercisesInDBByNum(10, exerciseRepository);
		long givenExerciseNum = exerciseRepository.count();
		
		//When
		long result = exerciseService.countAll();
		
		//Then
		assertEquals(givenExerciseNum, result);
	}
	
	/*
	 * long countAllWithNameKeyword(String nameKeyword)
	 */
	@Test
	@DisplayName("이름 키워드를 만족하는 Exercise 개수 세기 -> 정상, 모든 Exercise가 만족하는 키워드")
	void countAllWithNameKeywordTestAll() {
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
	void countAllWithNameKeywordTestOnlyOne() {
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
	void countAllWithNameKeywordTestNothing() {
		//Given
		String noContainsKeyword = "none!!!!";
		
		ExerciseTest.addNewExercisesInDBByNum(10, exerciseRepository);
		
		//When
		long result = exerciseService.countAllWithNameKeyword(noContainsKeyword);
		
		//Then
		assertEquals(0, result);
	}
	
	/*
	 *  ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId)
	 */
	
	@Test
	@DisplayName("Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환 -> 해당 exercise 없음")
	void getExerciseForInfoViewDTOByExerciseIdNonExist() {
		//Given
		Long nonExistId = ExerciseTest.getNonExistExerciseId(exerciseRepository);
		
		//When, Then 
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseService.getExerciseForInfoViewDTOByExerciseId(nonExistId);
		});
	}
	
	@Test
	@DisplayName("Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환 -> 정상, ExerciseMuscle 미포함")
	void getExerciseForInfoViewDTOByExerciseIdTestWithOutExerciseMuscle() {
		//Given
		Long existId = exercise.getId();
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(existId);
		
		//Then
		assertNotNull(result);
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환 -> 정상, ExerciseMuscle 포함")
	void getExerciseForInfoViewDTOByExerciseIdTestWithExerciseMuscle() {
		//Given
		Long existId = exercise.getId();
		
		String mainExerciseMuscleName = "mainMuscle";
		ExerciseMuscle mainExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance(mainExerciseMuscleName, true);
		mainExerciseMuscle.setExercise(exercise);
		exerciseMuscleRepository.save(mainExerciseMuscle);
		
		String subExerciseMuscleName = "subMuscle";
		ExerciseMuscle subExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance(subExerciseMuscleName, false);
		subExerciseMuscle.setExercise(exercise);
		exerciseMuscleRepository.save(subExerciseMuscle);
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(existId);
		
		//Then
		assertNotNull(result);
	}
}
