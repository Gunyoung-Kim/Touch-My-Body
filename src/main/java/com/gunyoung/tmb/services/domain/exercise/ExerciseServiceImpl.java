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
import com.gunyoung.tmb.services.domain.user.UserExerciseService;
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
	
	public static final String EXERCISE_SORT_BY_CATEGORY_DEFAULT_KEY = "bycategory";
	
	private final ExerciseRepository exerciseRepository;
	
	private final MuscleService muscleService;
	
	private final ExerciseMuscleService exerciseMuscleService;
	
	private final UserExerciseService userExerciseService;

	@Override
	@Transactional(readOnly=true)
	public Exercise findById(Long id) {
		Optional<Exercise> result = exerciseRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Exercise findByName(String name) {
		Optional<Exercise> result = exerciseRepository.findByName(name);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Exercise findWithFeedbacksById(Long id) {
		Optional<Exercise> result = exerciseRepository.findWithFeedbacksById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Exercise findWithExercisePostsByName(String name) {
		Optional<Exercise> result = exerciseRepository.findWithExercisePostsByName(name);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Exercise findWithExerciseMusclesById(Long id) {
		Optional<Exercise> result = exerciseRepository.findWithExerciseMusclesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Exercise> findAllInPage(Integer pageNumber,int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, page_size);
		return exerciseRepository.findAll(pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, page_size);
		return exerciseRepository.findAllWithNameKeyword(keyword, pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=CacheUtil.EXERCISE_SORT_NAME, key="#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
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

	@Override
	@CacheEvict(cacheNames=CacheUtil.EXERCISE_SORT_NAME, key="#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public Exercise save(Exercise exercise) {
		return exerciseRepository.save(exercise);
	}
	
	@Override
	@CacheEvict(cacheNames=CacheUtil.EXERCISE_SORT_NAME, key="#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public Exercise saveWithSaveExerciseDTO(Exercise exercise,SaveExerciseDTO dto) {
		exercise.setName(dto.getName());
		exercise.setDescription(dto.getDescription());
		exercise.setCaution(dto.getCaution());
		exercise.setMovement(dto.getMovement());
		
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
		
		save(exercise);
		
		exerciseMuscleService.saveAll(exerciseMuscles);
		
		return exercise;
	}

	@Override
	@CacheEvict(cacheNames=CacheUtil.EXERCISE_SORT_NAME, key="#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public void delete(Exercise exercise) {
		userExerciseService.deleteAllByExerciseId(exercise.getId());
		exerciseRepository.delete(exercise);
	}
	
	@Override
	@CacheEvict(cacheNames=CacheUtil.EXERCISE_SORT_NAME, key="#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public void deleteById(Long id) {
		Exercise exercise = findById(id);
		if(exercise != null)
			delete(exercise);
	}

	@Override
	@Transactional(readOnly=true)
	public long countAll() {
		return exerciseRepository.count();
	}

	@Override
	@Transactional(readOnly=true)
	public long countAllWithNameKeyword(String nameKeyword) {
		return exerciseRepository.countAllWithNameKeyword(nameKeyword);
	}

	@Override
	@Transactional(readOnly=true)
	public ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId) {
		Exercise exercise = findWithExerciseMusclesById(exerciseId);
		if(exercise == null)
			return null;
		ExerciseForInfoViewDTO dto = ExerciseForInfoViewDTO.of(exercise);
		
		return dto;
	}

	@Override
	@Transactional(readOnly=true)
	public boolean existsByName(String name) {
		return exerciseRepository.existsByName(name);
	}
}
