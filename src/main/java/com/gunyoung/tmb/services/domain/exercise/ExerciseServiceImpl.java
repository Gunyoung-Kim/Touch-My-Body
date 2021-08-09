package com.gunyoung.tmb.services.domain.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.jpa.ExerciseNameAndTargetDTO;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.utils.CacheUtil;

import lombok.RequiredArgsConstructor;


/**
 * ExerciesService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("exerciseService")
@Transactional
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
	
	private final ExerciseRepository exerciseRepository;
	
	private final MuscleService muscleService;
	
	private final ExerciseMuscleService exerciseMuscleService;

	/**
	 * ID로 Exercise 찾기
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Exercise findById(Long id) {
		Optional<Exercise> result = exerciseRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * name으로 Exercise 찾기
	 * @param name 찾으려는 Exercise의 이름
	 * @return Exercise, Null(해당 name의 Exercise가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Exercise findByName(String name) {
		Optional<Exercise> result = exerciseRepository.findByName(name);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * ID 로 Feedbacks 페치 조인 후 반환 
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Exercise findWithFeedbacksById(Long id) {
		Optional<Exercise> result = exerciseRepository.findWithFeedbacksById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * ID로 ExercisePosts 페치 조인 후 반환
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @since 11 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Exercise findWithExercisePostsByName(String name) {
		Optional<Exercise> result = exerciseRepository.findWithExercisePostsByName(name);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * ID로 ExerciseMuscles 페치 조인 후 반환
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @since 11 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Exercise findWithExerciseMusclesById(Long id) {
		Optional<Exercise> result = exerciseRepository.findWithExerciseMusclesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * 모든 Exercise 페이지로 가져오기 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<Exercise> findAllInPage(Integer pageNumber,int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, page_size);
		return exerciseRepository.findAll(pageRequest);
	}
	
	/**
	 * name에 키워드를 포함하는 모든 Exercise 페이지로 가져오는 메소드
	 * @param keyword name 검색 키워드 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, page_size);
		return exerciseRepository.findAllWithNameKeyword(keyword, pageRequest);
	}
	
	
	/**
	 * 모든 Exercise들을 target들로 분류해 반환 <br>
	 * Cache 이용
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=CacheUtil.EXERCISE_SORT_NAME,key="#root.methodName")
	public Map<String, List<String>> getAllExercisesNamewithSorting() {
		Map<String, List<String>> result = new HashMap<>();
		List<ExerciseNameAndTargetDTO> list = exerciseRepository.findAllWithNameAndTarget();
		
		for(ExerciseNameAndTargetDTO dto: list) {
			String target = dto.getTarget().getKoreanName();
			String name = dto.getName();
			
			if(result.containsKey(target)) {
				result.get(target).add(name);
			} else {
				List<String> newList = new ArrayList<>();
				newList.add(name);
				result.put(target, newList);
			}
		}
		
		return result;
	}

	/** 
	 * Exercise 생성 및 수정 <br>
	 * {@code CacheUtil.EXERCISE_SORT_NAME} 관련 Cache 삭제 
	 * @param exercise 저장하려는 Exercise
	 * @return 저장된 Exercise
	 * @author kimgun-yeong
	 */
	@Override
	@CacheEvict(cacheNames=CacheUtil.EXERCISE_SORT_NAME,allEntries=true)
	public Exercise save(Exercise exercise) {
		return exerciseRepository.save(exercise);
	}
	
	/**
	 * {@link SaveExerciseDTO} 에 담긴 정보로 Exercise save
	 * @param dto 클라이언트로부터 받은 Exercise save하기 위한 {@link SaveExerciseDTO} 객체
	 * @throws TargetTypeNotFoundedException dto 객체에 담긴 target이 아무런 TargetType의 이름이 아닐때 
	 * @author kimgun-yeong
	 */
	@Override
	public Exercise saveWithSaveExerciseDTO(Exercise exercise,SaveExerciseDTO dto) {
		exercise.setName(dto.getName());
		exercise.setDescription(dto.getDescription());
		exercise.setCaution(dto.getCaution());
		exercise.setMovement(dto.getMovement());
		
		// SaveExerciseDTO에 입력된 target에 해당하는 TargetType 있는지 확인
		
		TargetType exerciseTarget = TargetType.getFromKoreanName(dto.getTarget());
		
		if(exerciseTarget == null) {
			throw new TargetTypeNotFoundedException(TargetTypeErrorCode.TARGET_TYPE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		exercise.setTarget(exerciseTarget);
		
		// Main Muscle 들 이름으로 Muscle 객체들 가져오기
		List<String> mainMusclesName = dto.getMainMuscles();
		List<Muscle> mainMuscles = muscleService.getMuscleListFromMuscleNameList(mainMusclesName);
		
		
		// Sub Muscle 들 이름으로 Muscle 객체들 가져오기
		List<String> subMusclesName = dto.getSubMuscles();
		List<Muscle> subMuscles = muscleService.getMuscleListFromMuscleNameList(subMusclesName);
		
		// 생성한 Exercise 객체와 가져온 Muscle 객체로 ExerciseMuscle 객체 생성
		List<ExerciseMuscle> exerciseMuscles = new ArrayList<>();
		
		List<ExerciseMuscle> mainExerciseMuscleList =  exerciseMuscleService.getExerciseMuscleListFromExerciseAndMuscleListAndIsMain(exercise, mainMuscles, true);
		
		exerciseMuscles.addAll(mainExerciseMuscleList);
		exercise.getExerciseMuscles().addAll(mainExerciseMuscleList);
		
		
		List<ExerciseMuscle> subExerciseMuscleList = exerciseMuscleService.getExerciseMuscleListFromExerciseAndMuscleListAndIsMain(exercise, subMuscles, false);
		
		exerciseMuscles.addAll(subExerciseMuscleList);
		exercise.getExerciseMuscles().addAll(subExerciseMuscleList);
		
		//Exercise 객체 저장 
		save(exercise);
		
		//ExerciseMuscle 객체 저장
		exerciseMuscleService.saveAll(exerciseMuscles);
		
		return exercise;
	}

	/**
	 * Exercise 삭제 <br>
	 * {@code CacheUtil.EXERCISE_SORT_NAME} 관련 Cache 삭제 
	 * @param exercise 삭제하려는 Exercise
	 * @author kimgun-yeong
	 */
	@Override
	@CacheEvict(cacheNames=CacheUtil.EXERCISE_SORT_NAME,allEntries=true)
	public void delete(Exercise exercise) {
		exerciseRepository.delete(exercise);
	}
	
	/**
	 * ID를 만족하는 Exercise 삭제
	 * @author kimgun-yeong
	 */
	@Override
	public void deleteById(Long id) {
		Exercise exercise = findById(id);
		if(exercise != null)
			delete(exercise);
	}

	/**
	 * 모든 Exercise들 개수 반환
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countAll() {
		return exerciseRepository.count();
	}

	/**
	 * 이름 키워드를 만족하는 Exercise 개수 반환
	 * @param nameKeyword 이름 키워드
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countAllWithNameKeyword(String nameKeyword) {
		return exerciseRepository.countAllWithNameKeyword(nameKeyword);
	}

	/**
	 * Exercise Id로 찾은 Exercise로 {@link ExerciseForInfoViewDTO} 생성 및 반환 
	 * @param exerciseId 찾으려는 Exercise의 ID
	 * @return {@link ExerciseForInfoViewDTO}, null(해당 id의 Exercise 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId) {
		Exercise exercise = findById(exerciseId);
		
		if(exercise == null)
			return null;
		
		ExerciseForInfoViewDTO dto = ExerciseForInfoViewDTO.builder()
				.id(exercise.getId())
				.name(exercise.getName())
				.description(exercise.getDescription())
				.caution(exercise.getCaution())
				.movement(exercise.getMovement())
				.target(exercise.getTarget().getKoreanName())
				.build();
		
		List<ExerciseMuscle> muscles = exercise.getExerciseMuscles();
		StringBuilder mainMuscleBuilder = new StringBuilder();
		StringBuilder subMuscleBuilder = new StringBuilder();
		
		for(ExerciseMuscle muscle: muscles) {
			if(muscle.isMain()) {
				mainMuscleBuilder.append(muscle.getMuscleName()+".");
			} else {
				subMuscleBuilder.append(muscle.getMuscleName()+".");
			}
		}
		
		dto.setMainMuscle(mainMuscleBuilder.toString());
		dto.setSubMuscle(subMuscleBuilder.toString());
		
		return dto;
	}

	/**
	 * 해당 name의 Exercise 존재하는지 여부 반환
	 * @param 찾으려는 Exercise의 name
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean existsByName(String name) {
		return exerciseRepository.existsByName(name);
	}
}
