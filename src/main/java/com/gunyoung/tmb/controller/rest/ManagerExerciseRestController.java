package com.gunyoung.tmb.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.AddExerciseDTO;
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
	 * @param dto
	 * @param mav
	 * @throws ExerciseNameDuplicationFoundedException 입력된 이름의 Exercise 이미 존재하면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/add" ,method = RequestMethod.POST)
	public void addExercise(@ModelAttribute AddExerciseDTO dto) {
		if(exerciseService.existsByName(dto.getName())) {
			throw new ExerciseNameDuplicationFoundedException(ExerciseErrorCode.EXERCISE_NAME_DUPLICATION_ERROR.getDescription());
		}
		
		Exercise exercise = new Exercise();
		exerciseService.saveWithAddExerciseDTO(exercise,dto);
	}
	
	/**
	 * 특정 Exercise 정보 수정 처리하는 메소드
	 * @param exerciseId 정보 수정하려는 대상 Exercise
	 * @param dto
	 * @throws ExerciseNotFoundedException 해당 Id의 Exercise 없으면
	 * @throws ExerciseNameDuplicationFoundedException 변경된 이름이 다른 Exercise의 이름과 일치하면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/modify/{exerciseId}",method=RequestMethod.PUT) 
	public void modifyExercise(@PathVariable("exerciseId") Long exerciseId, @ModelAttribute AddExerciseDTO dto) {
		Exercise exercise = exerciseService.findById(exerciseId);
		
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription());
		}
		
		if(!exercise.getName().equals(dto.getName()) && exerciseService.existsByName(dto.getName())) {
			throw new ExerciseNameDuplicationFoundedException(ExerciseErrorCode.EXERCISE_NAME_DUPLICATION_ERROR.getDescription());
		}
		
		exerciseService.saveWithAddExerciseDTO(exercise, dto);
	}
	
	/**
	 * 특정 Exercise 삭제 요청 처리하는 메소드
	 * @param exerciseId 삭제하려는 대상 Exercise의 Id
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/remove" ,method = RequestMethod.DELETE) 
	public void deleteExercise(@RequestParam("exerciseId") Long exerciseId) {
		exerciseService.deleteById(exerciseId);
	}
	
	
	
	/**
	 * 클라이언트에게 근육 종류별로 분류해서 전송하는 메소드 <br>
	 * Muscle을 category로 분류해서 전송
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/getmuscles",method = RequestMethod.GET)
	public List<MuscleInfoBySortDTO> getMusclesSortByCategory() {
		List<MuscleInfoBySortDTO> response = new ArrayList<>();
		Map<String,List<String>> resultMap = muscleService.getAllMusclesWithSortingByCategory();
		
		for(String category: resultMap.keySet()) {
			MuscleInfoBySortDTO dto = MuscleInfoBySortDTO.builder()
					.target(category)
					.muscleNames(resultMap.get(category))
					.build();
			
			response.add(dto);
		}
		
		return response;
	}
	
	
}
