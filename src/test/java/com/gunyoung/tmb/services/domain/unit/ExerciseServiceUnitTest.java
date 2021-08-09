package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.jpa.ExerciseNameAndTargetDTO;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseMuscleService;
import com.gunyoung.tmb.services.domain.exercise.ExerciseServiceImpl;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;

/**
 * {@link ExerciseServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExerciseServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ExerciseServiceUnitTest {
	
	@Mock
	ExerciseRepository exerciseRepository;
	
	@Mock
	MuscleService muscleService;
	
	@Mock
	ExerciseMuscleService exerciseMuscleService;
	
	@InjectMocks
	ExerciseServiceImpl exerciseService;
	
	private Exercise exercise;
	
	@BeforeEach
	void setup() {
		exercise = Exercise.builder()
				.id(Long.valueOf(1))
				.name("name")
				.description("description")
				.caution("caution")
				.movement("movement")
				.target(TargetType.ARM)
				.build();
	}
	
	/*
	 * public Exercise findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Exercisse 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exerciseRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Exercise result = exerciseService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Exercisse 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		given(exerciseRepository.findById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findById(exerciseId);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * public Exercise findByName(String name)
	 */
	@Test
	@DisplayName("name으로 Exercisse 찾기 -> 존재하지 않음")
	public void findByNameNonExist() {
		//Given
		String nonExistName = "nonExist";
		given(exerciseRepository.findByName(nonExistName)).willReturn(Optional.empty());
		
		//When
		Exercise result = exerciseService.findByName(nonExistName);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("name으로 Exercisse 찾기 -> 정상")
	public void findByNameTest() {
		//Given
		String exerciseName = "Exercise";
		given(exerciseRepository.findByName(exerciseName)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findByName(exerciseName);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * public Exercise findWithFeedbacksById(Long id)
	 */
	
	@Test
	@DisplayName("ID 로 Feedbacks 페치 조인 후 반환 -> 존재하지 않음")
	public void findWithFeedbacksByIdNonExist() {
		Long nonExistId = Long.valueOf(1);
		given(exerciseRepository.findWithFeedbacksById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Exercise result = exerciseService.findWithFeedbacksById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID 로 Feedbacks 페치 조인 후 반환 -> 정상")
	public void findWithFeedbacksByIdTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		given(exerciseRepository.findWithFeedbacksById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findWithFeedbacksById(exerciseId);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * public Exercise findWithExercisePostsById(Long id)
	 */
	
	@Test
	@DisplayName("name으로 ExercisePosts 페치 조인 후 반환 -> 존재하지 않음")
	public void findWithExercisePostsByIdNonExist() {
		String nonExistName = "nonExist";
		given(exerciseRepository.findWithExercisePostsByName(nonExistName)).willReturn(Optional.empty());
		
		//When
		Exercise result = exerciseService.findWithExercisePostsByName(nonExistName);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("name으로 ExercisePosts 페치 조인 후 반환 -> 정상")
	public void findWithExercisePostsByIdTest() {
		//Given
		String exerciseName = "Exercise";
		given(exerciseRepository.findWithExercisePostsByName(exerciseName)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findWithExercisePostsByName(exerciseName);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * public Exercise findWithExerciseMusclesById(Long id)
	 */
	
	@Test
	@DisplayName("ID 로 ExerciseMuscles 페치 조인 후 반환 -> 존재하지 않음")
	public void findWithExerciseMusclesByIdNonExist() {
		Long nonExistId = Long.valueOf(1);
		given(exerciseRepository.findWithExerciseMusclesById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Exercise result = exerciseService.findWithExerciseMusclesById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID 로 ExerciseMuscles 페치 조인 후 반환 -> 정상")
	public void findWithExerciseMusclesByIdTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		given(exerciseRepository.findWithExerciseMusclesById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		Exercise result = exerciseService.findWithExerciseMusclesById(exerciseId);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * public Page<Exercise> findAllInPage(Integer pageNumber,int page_size)
	 */
	
	@Test
	@DisplayName("모든 Exercise 페이지로 가져오기 -> 정상")
	public void findAllInPageTest() {
		//Given
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exerciseService.findAllInPage(pageNum, pageSize);
		
		//Then
		then(exerciseRepository).should(times(1)).findAll(any(PageRequest.class));
	}
	
	/*
	 * public Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int page_size)
	 */
	@Test
	@DisplayName("name에 키워드를 포함하는 모든 Exercise 페이지로 가져오는 메소드 -> 정상")
	public void findAllWithNameKeywordInPageTest() {
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
	 * public Map<String, List<String>> getAllExercisesNamewithSorting()
	 */
	
	@Test
	@DisplayName("모든 Exercise들을 target들로 분류해 반환 -> 정상")
	public void getAllExercisesNamewithSortingTest() {
		//Given
		List<ExerciseNameAndTargetDTO> exerciseNameAndTargetDTOList = new ArrayList<>();
		
		TargetType[] targetTypes = TargetType.values();
		for(TargetType target: targetTypes) {
			ExerciseNameAndTargetDTO dto = ExerciseNameAndTargetDTO.builder()
					.name("name")
					.target(target)
					.build();
			exerciseNameAndTargetDTOList.add(dto);
		}
		
		given(exerciseRepository.findAllWithNameAndTarget()).willReturn(exerciseNameAndTargetDTOList);
		
		//When
		Map<String, List<String>> result = exerciseService.getAllExercisesNamewithSorting();
		
		//Then
		Set<String> resultMapKeySet = result.keySet();
		
		assertEquals(targetTypes.length, resultMapKeySet.size());
		
		for(String target: resultMapKeySet) {
			assertEquals(1, result.get(target).size());
		}
	}
	
	/*
	 * public Exercise save(Exercise exercise)
	 */
	
	@Test
	@DisplayName("Exercise 생성 및 수정 -> 정상")
	public void saveTest() {
		//Given
		given(exerciseRepository.save(exercise)).willReturn(exercise);
		
		//When
		Exercise result = exerciseService.save(exercise);
		
		//Then
		assertEquals(exercise, result);
	}
	
	/*
	 * public Exercise saveWithSaveExerciseDTO(Exercise exercise,SaveExerciseDTO dto) 
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
	@DisplayName("SaveExerciseDTO 에 담긴 정보로 Exercise save ->dto 객체에 담긴 TargetType 이름에 해당하는 TargetType 없음")
	public void saveWithSaveExerciseDTOTargetTypeNonExist() {
		//Given
		String nonExistTargetTypeKoreanName = "존재하지않아";
		SaveExerciseDTO saveExerciseDTO = getSaveExerciseDTOInstance(nonExistTargetTypeKoreanName);
		
		//When, Then
		assertThrows(TargetTypeNotFoundedException.class,() -> {
			exerciseService.saveWithSaveExerciseDTO(exercise, saveExerciseDTO);
		});
		
		then(exerciseRepository).should(never()).save(exercise);
		then(exerciseMuscleService).should(never()).saveAll(any());
	}
	
	@Test
	@DisplayName("SaveExerciseDTO 에 담긴 정보로 Exercise save -> 정상")
	public void saveWithSaveExerciseDTOTest() {
		//Given
		String targetTypeKoreanName = TargetType.ARM.getKoreanName();
		SaveExerciseDTO saveExerciseDTO = getSaveExerciseDTOInstance(targetTypeKoreanName);
		
		List<Muscle> mainMuscles = new ArrayList<>();
		List<Muscle> subMucles = new ArrayList<>();
		
		given(muscleService.getMuscleListFromMuscleNameList(saveExerciseDTO.getMainMuscles())).willReturn(mainMuscles);
		given(muscleService.getMuscleListFromMuscleNameList(saveExerciseDTO.getSubMuscles())).willReturn(subMucles);
		given(exerciseMuscleService.getExerciseMuscleListFromExerciseAndMuscleListAndIsMain(exercise, mainMuscles, true)).willReturn(new ArrayList<>());
		given(exerciseMuscleService.getExerciseMuscleListFromExerciseAndMuscleListAndIsMain(exercise, subMucles, true)).willReturn(new ArrayList<>());
		
		//When
		exerciseService.saveWithSaveExerciseDTO(exercise, saveExerciseDTO);
		
		//Then
		then(exerciseRepository).should(times(1)).save(exercise);
		then(exerciseMuscleService).should(times(1)).saveAll(any());
	}
	
	/*
	 * public void delete(Exercise exercise) 
	 */
	
	@Test
	@DisplayName("Exercise 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		exerciseService.delete(exercise);
		
		//Then
		then(exerciseRepository).should(times(1)).delete(exercise);
	}
	
	/*
	 * public void deleteById(Long id) 
	 */
	
	@Test
	@DisplayName("ID를 만족하는 Exercise 삭제 -> ID 만족하는 Exercise 없음")
	public void deleteByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(exerciseRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		exerciseService.deleteById(nonExistId);
		
		//Then
		then(exerciseRepository).should(never()).delete(any(Exercise.class));
	}
	
	@Test
	@DisplayName("ID를 만족하는 Exercise 삭제 -> 정상")
	public void deleteByIdTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		
		given(exerciseRepository.findById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		exerciseService.deleteById(exerciseId);
		
		//Then
		then(exerciseRepository).should(times(1)).delete(exercise);
	}
	
	/*
	 * public long countAll()
	 */
	
	@Test
	@DisplayName("모든 Exercise들 개수 반환 -> 정상")
	public void countAllTest() {
		//Given
		long num = 1;
		given(exerciseRepository.count()).willReturn(num);
		
		//When
		long result = exerciseService.countAll();
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * 이름 키워드를 만족하는 Exercise 개수 반환
	 */
	
	@Test
	@DisplayName("이름 키워드를 만족하는 Exercise 개수 반환 -> 정상")
	public void countAllWithNameKeywordTest() {
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
	 * public ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId) {
	 */
	
	@Test
	@DisplayName("Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환 -> 해당 ID의 Exercise 없을 때")
	public void getExerciseForInfoViewDTOByExerciseIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(exerciseRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환  -> 정상")
	public void getExerciseForInfoViewDTOByExerciseIdTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		
		given(exerciseRepository.findById(exerciseId)).willReturn(Optional.of(exercise));
		
		//When
		ExerciseForInfoViewDTO result = exerciseService.getExerciseForInfoViewDTOByExerciseId(exerciseId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 * public boolean existsByName(String name)
	 */
	
	@Test
	@DisplayName("해당 name의 Exercise 존재하는지 여부 반환 -> 정상")
	public void existsByNameTest() {
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
