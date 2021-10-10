package com.gunyoung.tmb.services.domain.exercise;

import static com.gunyoung.tmb.utils.CacheConstants.EXERCISE_SORT_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
	
	private final ExercisePostService exercisePostService;
	
	private final UserExerciseService userExerciseService;
	
	private final FeedbackService feedbackService;

	@Override
	@Transactional(readOnly=true)
	public Exercise findById(Long id) {
		Optional<Exercise> result = exerciseRepository.findById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Exercise findByName(String name) {
		Optional<Exercise> result = exerciseRepository.findByName(name);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Exercise findWithFeedbacksById(Long id) {
		Optional<Exercise> result = exerciseRepository.findWithFeedbacksById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Exercise findWithExercisePostsByName(String name) {
		Optional<Exercise> result = exerciseRepository.findWithExercisePostsByName(name);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Exercise findWithExerciseMusclesById(Long id) {
		Optional<Exercise> result = exerciseRepository.findWithExerciseMusclesById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Exercise> findAllInPage(Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exerciseRepository.findAll(pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exerciseRepository.findAllWithNameKeyword(keyword, pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=EXERCISE_SORT_NAME, key="#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public Map<String, List<String>> getAllExercisesNamewithSorting() {
		Map<String, List<String>> sortingResult = new HashMap<>();
		List<ExerciseNameAndTargetDTO> listOfDTOFromRepo = exerciseRepository.findAllWithNameAndTarget();
		listOfDTOFromRepo.stream().forEach( dto -> {
			String koreanNameOfTarget = dto.getTarget().getKoreanName();
			String nameOfExercise = dto.getName();
			sortingResult.putIfAbsent(koreanNameOfTarget, new ArrayList<>());
			sortingResult.get(koreanNameOfTarget).add(nameOfExercise);
		});
		
		return sortingResult;
	}

	@Override
	@CacheEvict(cacheNames=EXERCISE_SORT_NAME, key="#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public Exercise save(Exercise exercise) {
		return exerciseRepository.save(exercise);
	}
	
	@Override
	@CacheEvict(cacheNames = EXERCISE_SORT_NAME, key = "#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public Exercise saveWithSaveExerciseDTO(Exercise exercise, SaveExerciseDTO dto) {
		Objects.requireNonNull(exercise, "Given exercise must not be null!");
		Objects.requireNonNull(dto, "Given saveExerciseDTO must not be null");
		
		exercise.setName(dto.getName());
		exercise.setDescription(dto.getDescription());
		exercise.setCaution(dto.getCaution());
		exercise.setMovement(dto.getMovement());
		setTargetOfExerciseByDTO(exercise, dto);
		
		List<ExerciseMuscle> mainExerciseMuscleList =  getListOfMainExerciseMuscle(exercise, dto);
		List<ExerciseMuscle> subExerciseMuscleList = getListOfSubExerciseMuscle(exercise, dto);
		
		List<ExerciseMuscle> exerciseMusclesForSaving = mergeExerciseMuscleList(mainExerciseMuscleList, subExerciseMuscleList);
		exerciseMuscleService.saveAll(exerciseMusclesForSaving);
		
		exercise.getExerciseMuscles().addAll(exerciseMusclesForSaving);
		save(exercise);
		
		return exercise;
	}
	
	private void setTargetOfExerciseByDTO(Exercise exercise, SaveExerciseDTO dto) {
		TargetType exerciseTarget = TargetType.getFromKoreanName(dto.getTarget());
		if(exerciseTarget == null) {
			throw new TargetTypeNotFoundedException(TargetTypeErrorCode.TARGET_TYPE_NOT_FOUNDED_ERROR.getDescription());
		}
		exercise.setTarget(exerciseTarget);
	}
	
	private List<ExerciseMuscle> getListOfMainExerciseMuscle(Exercise exercise, SaveExerciseDTO dto) {
		List<String> mainMusclesName = dto.getMainMuscles();
		List<Muscle> mainMuscles = muscleService.findAllByNames(mainMusclesName);
		return ExerciseMuscle.mainOf(exercise, mainMuscles);
	}
	
	private List<ExerciseMuscle> getListOfSubExerciseMuscle(Exercise exercise, SaveExerciseDTO dto) {
		List<String> subMusclesName = dto.getSubMuscles();
		List<Muscle> subMuscles = muscleService.findAllByNames(subMusclesName);
		return ExerciseMuscle.subOf(exercise, subMuscles);
	}
	
	private List<ExerciseMuscle> mergeExerciseMuscleList(List<ExerciseMuscle> mainExerciseMuscleList, List<ExerciseMuscle> subExerciseMuscleList) {
		mainExerciseMuscleList.addAll(subExerciseMuscleList);
		return mainExerciseMuscleList;
	}
	
	@Override
	@CacheEvict(cacheNames=EXERCISE_SORT_NAME, key="#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public void deleteById(Long id) {
		Exercise exercise = findById(id);
		if(exercise != null)
			delete(exercise);
	}

	@Override
	@CacheEvict(cacheNames=EXERCISE_SORT_NAME, key="#root.target.EXERCISE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public void delete(Exercise exercise) {
		Objects.requireNonNull(exercise);
		deleteAllOneToManyEntityForExercise(exercise);
		exerciseRepository.deleteByIdInQuery(exercise.getId());
	}
	
	private void deleteAllOneToManyEntityForExercise(Exercise exercise) {
		Long exerciseId = exercise.getId();
		userExerciseService.deleteAllByExerciseId(exerciseId);
		feedbackService.deleteAllByExerciseId(exerciseId);
		exerciseMuscleService.deleteAllByExerciseId(exerciseId);
		exercisePostService.deleteAllByExerciseId(exerciseId);
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
		return ExerciseForInfoViewDTO.of(exercise);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean existsByName(String name) {
		return exerciseRepository.existsByName(name);
	}
}
