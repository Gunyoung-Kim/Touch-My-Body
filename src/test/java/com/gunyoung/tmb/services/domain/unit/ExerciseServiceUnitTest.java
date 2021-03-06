package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.jpa.ExerciseNameAndTargetDTO;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.precondition.PreconditionViolationException;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseMuscleService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.exercise.ExerciseServiceImpl;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;
import com.gunyoung.tmb.services.domain.user.UserExerciseService;
import com.gunyoung.tmb.testutil.ExerciseMuscleTest;

/**
 * {@link ExerciseServiceImpl} ??? ?????? ????????? ????????? <br>
 * ????????? ??????: (?????? ?????????) ExerciseServiceImpl only
 * {@link org.mockito.BDDMockito}??? ????????? ????????? ????????? ?????? ?????? ?????????
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ExerciseServiceUnitTest {
	
	@Mock
	ExerciseRepository exerciseRepository;
	
	@Mock
	MuscleService muscleService;
	
	@Mock
	ExerciseMuscleService exerciseMuscleService;
	
	@Mock
	ExercisePostService exercisePostService;
	
	@Mock
	UserExerciseService userExerciseService;
	
	@Mock
	FeedbackService feedbackService;
	
	@InjectMocks
	ExerciseServiceImpl exerciseService;
	
	private Exercise exercise;
	
	@BeforeEach
	void setup() {
		exercise = Exercise.builder()
				.id(Long.valueOf(24))
				.name("name")
				.description("description")
				.caution("caution")
				.movement("movement")
				.target(TargetType.ARM)
				.build();
	}
	
	/*
	 * Exercise findById(Long id)
	 */
	
	@Test
	@DisplayName("ID??? Exercisse ?????? -> ???????????? ??????")
	void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exerciseRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseService.findById(nonExistId);
		});
	}
	
	@Test
	@DisplayName("ID??? Exercisse ?????? -> ??????")
	void findByIdTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		given(exerciseRepository.findById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findById(exerciseId);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * Exercise findByName(String name)
	 */
	@Test
	@DisplayName("name?????? Exercisse ?????? -> ???????????? ??????")
	void findByNameNonExist() {
		//Given
		String nonExistName = "nonExist";
		given(exerciseRepository.findByName(nonExistName)).willReturn(Optional.empty());
		
		//When, Then 
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseService.findByName(nonExistName);
		});
	}
	
	@Test
	@DisplayName("name?????? Exercisse ?????? -> ??????")
	void findByNameTest() {
		//Given
		String exerciseName = "Exercise";
		given(exerciseRepository.findByName(exerciseName)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findByName(exerciseName);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * Exercise findWithFeedbacksById(Long id)
	 */
	
	@Test
	@DisplayName("ID ??? Feedbacks ?????? ?????? ??? ?????? -> ???????????? ??????")
	void findWithFeedbacksByIdNonExist() {
		Long nonExistId = Long.valueOf(1);
		given(exerciseRepository.findWithFeedbacksById(nonExistId)).willReturn(Optional.empty());
		
		//When, Then 
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseService.findWithFeedbacksById(nonExistId);
		});
	}
	
	@Test
	@DisplayName("ID ??? Feedbacks ?????? ?????? ??? ?????? -> ??????")
	void findWithFeedbacksByIdTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		given(exerciseRepository.findWithFeedbacksById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findWithFeedbacksById(exerciseId);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * Exercise findWithExercisePostsById(Long id)
	 */
	
	@Test
	@DisplayName("name?????? ExercisePosts ?????? ?????? ??? ?????? -> ???????????? ??????")
	void findWithExercisePostsByIdNonExist() {
		String nonExistName = "nonExist";
		given(exerciseRepository.findWithExercisePostsByName(nonExistName)).willReturn(Optional.empty());
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseService.findWithExercisePostsByName(nonExistName);
		});
	}
	
	@Test
	@DisplayName("name?????? ExercisePosts ?????? ?????? ??? ?????? -> ??????")
	void findWithExercisePostsByIdTest() {
		//Given
		String exerciseName = "Exercise";
		given(exerciseRepository.findWithExercisePostsByName(exerciseName)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findWithExercisePostsByName(exerciseName);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * Exercise findWithExerciseMusclesById(Long id)
	 */
	
	@Test
	@DisplayName("ID ??? ExerciseMuscles ?????? ?????? ??? ?????? -> ???????????? ??????")
	void findWithExerciseMusclesByIdNonExist() {
		Long nonExistId = Long.valueOf(1);
		given(exerciseRepository.findWithExerciseMusclesById(nonExistId)).willReturn(Optional.empty());
		
		//When
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseService.findWithExerciseMusclesById(nonExistId);
		});
	}
	
	@Test
	@DisplayName("ID ??? ExerciseMuscles ?????? ?????? ??? ?????? -> ??????")
	void findWithExerciseMusclesByIdTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		given(exerciseRepository.findWithExerciseMusclesById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findWithExerciseMusclesById(exerciseId);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * Page<Exercise> findAllInPage(Integer pageNumber,int page_size)
	 */
	
	@Test
	@DisplayName("?????? ?????? ???????????? ???????????? -> pageNumber < 1")
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
	@DisplayName("?????? ?????? ???????????? ???????????? -> pageSize < 1")
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
	@DisplayName("?????? Exercise ???????????? ???????????? -> ??????")
	void findAllInPageTest() {
		//Given
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exerciseService.findAllInPage(pageNum, pageSize);
		
		//Then
		then(exerciseRepository).should(times(1)).findAll(any(PageRequest.class));
	}
	
	/*
	 * Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int page_size)
	 */
	
	@Test
	@DisplayName("????????? ???????????? ???????????? ?????? ?????? ???????????? ???????????? -> pageNumber < 1")
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
	@DisplayName("????????? ???????????? ???????????? ?????? ?????? ???????????? ????????????-> pageSize < 1")
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
	@DisplayName("name??? ???????????? ???????????? ?????? Exercise ???????????? ???????????? ????????? -> ??????")
	void findAllWithNameKeywordInPageTest() {
		//Given
		String keyword = "keyword";
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exerciseService.findAllWithNameKeywordInPage(keyword, pageNum, pageSize);
		
		//Then
		then(exerciseRepository).should(times(1)).findAllWithNameKeyword(anyString(), any(PageRequest.class));
	}
	
	/*
	 * Map<String, List<String>> getAllExercisesNamewithSorting()
	 */
	
	@Test
	@DisplayName("?????? Exercise?????? target?????? ????????? ?????? -> ??????")
	void getAllExercisesNamewithSortingTest() {
		//Given
		List<ExerciseNameAndTargetDTO> exerciseNameAndTargetDTOList = new ArrayList<>();
		addExerciseNameAndTargetDTOListTwoForARMAndOneForOthers(exerciseNameAndTargetDTOList);
		given(exerciseRepository.findAllWithNameAndTarget()).willReturn(exerciseNameAndTargetDTOList);
		
		//When
		Map<String, List<String>> result = exerciseService.getAllExercisesNamewithSorting();
		
		//Then
		verifyResult_for_getAllExercisesNamewithSortingTest(result);
	}
	
	private void addExerciseNameAndTargetDTOListTwoForARMAndOneForOthers(List<ExerciseNameAndTargetDTO> exerciseNameAndTargetDTOList) {
		TargetType[] targetTypes = TargetType.values();
		for(TargetType target: targetTypes) {
			ExerciseNameAndTargetDTO dto = ExerciseNameAndTargetDTO.builder()
					.name("name")
					.target(target)
					.build();
			exerciseNameAndTargetDTOList.add(dto);
		}
		
		ExerciseNameAndTargetDTO dto = ExerciseNameAndTargetDTO.builder()
				.name("name")
				.target(TargetType.ARM)
				.build();
		exerciseNameAndTargetDTOList.add(dto);
	}
	
	private void verifyResult_for_getAllExercisesNamewithSortingTest(Map<String, List<String>> result) {
		Set<String> resultMapKeySet = result.keySet();
		
		assertEquals(TargetType.values().length, resultMapKeySet.size());
		
		for(String target: resultMapKeySet) {
			if(target.equals(TargetType.ARM.getKoreanName()))
				assertEquals(2, result.get(target).size());
			else 
				assertEquals(1, result.get(target).size());
		}
	}
	
	/*
	 * Exercise save(Exercise exercise)
	 */
	
	@Test
	@DisplayName("Exercise ?????? ??? ?????? -> ??????")
	void saveTest() {
		//Given
		given(exerciseRepository.save(exercise)).willReturn(exercise);
		
		//When
		Exercise result = exerciseService.save(exercise);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * Exercise saveWithSaveExerciseDTO(Exercise exercise,SaveExerciseDTO dto) 
	 */
	
	private SaveExerciseDTO getSaveExerciseDTOInstance(String TargetTypeName) {
		SaveExerciseDTO dto = SaveExerciseDTO.builder()
				.name("name")
				.description("description")
				.caution("caution")
				.movement("movement")
				.target(TargetTypeName)
				.mainMuscles(new ArrayList<>())
				.subMuscles(new ArrayList<>())
				.build();
		
		return dto;
	}
	
	@Test
	@DisplayName("SaveExerciseDTO ??? ?????? ????????? Exercise save -> ?????? exercise == null")
	void saveWithSaveExerciseDTOTestNullExercise() {
		//Given
		String targetTypeKoreanName = TargetType.ARM.getKoreanName();
		SaveExerciseDTO saveExerciseDTO = getSaveExerciseDTOInstance(targetTypeKoreanName);
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(null, saveExerciseDTO);
		});
	}
	
	@Test
	@DisplayName("SaveExerciseDTO ??? ?????? ????????? Exercise save -> ?????? dto == null")
	void saveWithSaveExerciseDTOTestNullDTO() {
		//Given
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exerciseService.saveWithSaveExerciseDTO(exercise, null);
		});
	}
	
	@Test
	@DisplayName("SaveExerciseDTO ??? ?????? ????????? Exercise save ->dto ????????? ?????? TargetType ????????? ???????????? TargetType ??????")
	void saveWithSaveExerciseDTOTargetTypeNonExist() {
		//Given
		String nonExistTargetTypeKoreanName = "??????????????????";
		SaveExerciseDTO saveExerciseDTO = getSaveExerciseDTOInstance(nonExistTargetTypeKoreanName);
		
		//When, Then
		assertThrows(TargetTypeNotFoundedException.class,() -> {
			exerciseService.saveWithSaveExerciseDTO(exercise, saveExerciseDTO);
		});
		
		then(exerciseRepository).should(never()).save(exercise);
		then(exerciseMuscleService).should(never()).saveAll(any());
	}
	
	@Test
	@DisplayName("SaveExerciseDTO ??? ?????? ????????? Exercise save -> ??????")
	void saveWithSaveExerciseDTOTest() {
		//Given
		String targetTypeKoreanName = TargetType.ARM.getKoreanName();
		SaveExerciseDTO saveExerciseDTO = getSaveExerciseDTOInstance(targetTypeKoreanName);
		
		List<Muscle> mainMuscles = new ArrayList<>();
		List<Muscle> subMucles = new ArrayList<>();
		
		given(muscleService.findAllByNames(saveExerciseDTO.getMainMuscles())).willReturn(mainMuscles);
		given(muscleService.findAllByNames(saveExerciseDTO.getSubMuscles())).willReturn(subMucles);
		
		//When
		exerciseService.saveWithSaveExerciseDTO(exercise, saveExerciseDTO);
		
		//Then
		then(exerciseRepository).should(times(1)).save(exercise);
		then(exerciseMuscleService).should(times(1)).saveAll(any());
	}
	
	/*
	 * void delete(Exercise exercise) 
	 */
	
	@Test
	@DisplayName("Exercise ?????? -> ??????, Exercise Null") 
	void deleteTestExerciseNull(){
		//Given
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exerciseService.delete(null);
		});
	}
	
	@Test
	@DisplayName("Exercise ?????? -> ??????, Check ExerciseRepository")
	void deleteTestCheckExerciseRepo() {
		//Given
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		then(exerciseRepository).should(times(1)).deleteByIdInQuery(exercise.getId());
	}
	
	@Test
	@DisplayName("Exercise ?????? -> ??????, Check UserExerciseService")
	void deleteTestCheckUserExerciseService() {
		//Given
		Long exerciseId = exercise.getId();
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		then(userExerciseService).should(times(1)).deleteAllByExerciseId(exerciseId);
	}
	
	@Test
	@DisplayName("Exercise ?????? -> ??????, Check FeedbackService")
	void deleteTestCheckFeedbackService() {
		//Given
		Long exerciseId = exercise.getId();
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		then(feedbackService).should(times(1)).deleteAllByExerciseId(exerciseId);
	}
	
	@Test
	@DisplayName("Exercise ?????? -> ??????, Check ExerciseMuscleService")
	void deleteTestCheckExerciseMuscleService() {
		//Given
		Long exerciseId = exercise.getId();
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		then(exerciseMuscleService).should(times(1)).deleteAllByExerciseId(exerciseId);
	}
	
	@Test
	@DisplayName("Exercise ?????? -> ??????, Check ExercisePostService")
	void deleteTestCheckExercisePostService() {
		//Given
		Long exerciseId = exercise.getId();
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		then(exercisePostService).should(times(1)).deleteAllByExerciseId(exerciseId);
	}
	
	/*
	 * void deleteById(Long id) 
	 */
	
	@Test
	@DisplayName("ID??? ???????????? Exercise ?????? -> ID ???????????? Exercise ??????")
	void deleteByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(exerciseRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		exerciseService.deleteById(nonExistId);
		
		//Then
		then(exerciseRepository).should(never()).delete(any(Exercise.class));
	}
	
	@Test
	@DisplayName("ID??? ???????????? Exercise ?????? -> ??????")
	void deleteByIdTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		
		given(exerciseRepository.findById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		exerciseService.deleteById(exerciseId);
		
		//Then
		then(exerciseRepository).should(times(1)).deleteByIdInQuery(exercise.getId());
	}
	
	/*
	 * long countAll()
	 */
	
	@Test
	@DisplayName("?????? Exercise??? ?????? ?????? -> ??????")
	void countAllTest() {
		//Given
		long num = 1;
		given(exerciseRepository.count()).willReturn(num);
		
		//When
		long result = exerciseService.countAll();
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * ?????? ???????????? ???????????? Exercise ?????? ??????
	 */
	
	@Test
	@DisplayName("?????? ???????????? ???????????? Exercise ?????? ?????? -> ??????")
	void countAllWithNameKeywordTest() {
		//Given
		String keyword = "keyword";
		long num = 1;
		given(exerciseRepository.countAllWithNameKeyword(keyword)).willReturn(num);
		
		//When
		long result = exerciseService.countAllWithNameKeyword(keyword);
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId) {
	 */
	
	@Test
	@DisplayName("Exercise Id??? ?????? Exercise??? ExerciseForInfoViewDTO ?????? ??? ?????? -> ?????? ID??? Exercise ?????? ???")
	void getExerciseForInfoViewDTOByExerciseIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(exerciseRepository.findWithExerciseMusclesById(nonExistId)).willReturn(Optional.empty());
		
		//When
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseService.getExerciseForInfoViewDTOByExerciseId(nonExistId);
		});
	}
	
	@Test
	@DisplayName("Exercise Id??? ?????? Exercise??? ExerciseForInfoViewDTO ?????? ??? ??????  -> ??????, ExerciseMuscle ?????? ??????")
	void getExerciseForInfoViewDTOByExerciseIdTestNoExerciseMuscle() {
		//Given
		Long exerciseId = Long.valueOf(1);
		
		given(exerciseRepository.findWithExerciseMusclesById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(exerciseId);
		
		//Then
		assertNotNull(result);
	}
	
	@Test
	@DisplayName("Exercise Id??? ?????? Exercise??? ExerciseForInfoViewDTO ?????? ??? ??????  -> ??????, ExerciseMuscle ?????? ??????")
	void getExerciseForInfoViewDTOByExerciseIdTestWithExerciseMuscle() {
		//Given
		Long exerciseId = Long.valueOf(1);
		ExerciseMuscle mainExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance();
		mainExerciseMuscle.setMain(true);
		ExerciseMuscle subExerciseMuscle = ExerciseMuscleTest.getExerciseMuscleInstance();
		subExerciseMuscle.setMain(false);
		
		exercise.getExerciseMuscles().addAll(List.of(mainExerciseMuscle, subExerciseMuscle));
		
		given(exerciseRepository.findWithExerciseMusclesById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(exerciseId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 * boolean existsByName(String name)
	 */
	
	@Test
	@DisplayName("?????? name??? Exercise ??????????????? ?????? ?????? -> ??????")
	void existsByNameTest() {
		//Given
		String name = "exercise";
		boolean isExist = true;
		
		given(exerciseRepository.existsByName(name)).willReturn(isExist);
		
		//When
		boolean result = exerciseService.existsByName(name);
		
		//Then
		assertEquals(isExist, result);
	}
}
