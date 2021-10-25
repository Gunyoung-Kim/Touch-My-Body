package com.gunyoung.tmb.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.MuscleInfoBySortDTO;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.ExerciseNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;

import lombok.RequiredArgsConstructor;

/**
 * 매니저의 Exercise 관련 요청 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ManagerExerciseRestController {
	
	private final ExerciseService exerciseService;
	
	private final MuscleService muscleService;
	
	/**
	 * 매니저의 Exercise 추가 처리하는 메소드
	 * @throws ExerciseNameDuplicationFoundedException 입력된 이름의 Exercise 이미 존재하면
	 * @author kimgun-yeong
	 */
	@PostMapping(value = "/manager/exercise/add")
	public void addExercise(@ModelAttribute SaveExerciseDTO dto) {
		if(exerciseService.existsByName(dto.getName())) {
			throw new ExerciseNameDuplicationFoundedException(ExerciseErrorCode.EXERCISE_NAME_DUPLICATION_ERROR.getDescription());
		}
		
		Exercise newExercise = Exercise.builder().build();
		exerciseService.saveWithSaveExerciseDTO(newExercise,dto);
	}
	
	/**
	 * 특정 Exercise 정보 수정 처리하는 메소드
	 * @param exerciseId 정보 수정하려는 대상 Exercise
	 * @throws ExerciseNotFoundedException 해당 Id의 Exercise 없으면
	 * @throws ExerciseNameDuplicationFoundedException 변경된 이름이 다른 Exercise의 이름과 일치하면
	 * @author kimgun-yeong
	 */
	@PutMapping(value = "/manager/exercise/modify/{exerciseId}")
	public void modifyExercise(@PathVariable("exerciseId") Long exerciseId, @ModelAttribute SaveExerciseDTO dto) {
		Exercise exercise = exerciseService.findWithExerciseMusclesById(exerciseId);
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription());
		}
		if(isNameChangedAndDuplicatedWithNamesOfOtherThings(exercise, dto)) {
			throw new ExerciseNameDuplicationFoundedException(ExerciseErrorCode.EXERCISE_NAME_DUPLICATION_ERROR.getDescription());
		}
		
		exerciseService.saveWithSaveExerciseDTO(exercise, dto);
	}
	
	private boolean isNameChangedAndDuplicatedWithNamesOfOtherThings(Exercise exercise, SaveExerciseDTO dto) {
		return !exercise.getName().equals(dto.getName()) && exerciseService.existsByName(dto.getName());
	}
	
	/**
	 * 특정 Exercise 삭제 요청 처리하는 메소드
	 * @param exerciseId 삭제하려는 대상 Exercise의 Id
	 * @author kimgun-yeong
	 */
	@DeleteMapping(value = "/manager/exercise/remove")
	public void deleteExercise(@RequestParam("exerciseId") Long exerciseId) {
		exerciseService.deleteById(exerciseId);
	}
	
	/**
	 * 클라이언트에게 근육 종류별로 분류해서 전송하는 메소드 <br>
	 * Muscle을 category로 분류해서 전송
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/manager/exercise/getmuscles")
	public List<MuscleInfoBySortDTO> getMusclesSortByCategory() {
		Map<String, List<String>> classificationOfMuscleNamesByCategory = muscleService.getAllMusclesWithSortingByCategory();
		
		return getListOfMuscleInfoBySortDTOByMapOfMuscleNames(classificationOfMuscleNamesByCategory);
	}
	
	private List<MuscleInfoBySortDTO> getListOfMuscleInfoBySortDTOByMapOfMuscleNames(Map<String, List<String>> classificationOfMuscleNamesByCategory) {
		List<MuscleInfoBySortDTO> result = new ArrayList<>();
		for(Entry<String, List<String>> entry: classificationOfMuscleNamesByCategory.entrySet()) {
			String category = entry.getKey();
			List<String> listOfMuscleName = entry.getValue();
			MuscleInfoBySortDTO dto = MuscleInfoBySortDTO.builder()
					.target(category)
					.muscleNames(listOfMuscleName)
					.build();
			result.add(dto);
		}
		return result;
	}
}
