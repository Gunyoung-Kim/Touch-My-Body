package com.gunyoung.tmb.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.dto.reqeust.AddExerciseDTO;
import com.gunyoung.tmb.dto.response.MuscleInfoBySortDTO;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerExerciseRestController {
	
	private final ExerciseService exerciseService;
	
	private final MuscleService muscleService;
	
	/**
	 * 
	 * @param dto
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/exercise/add" ,method = RequestMethod.POST)
	public void addExercise(@ModelAttribute AddExerciseDTO dto,ModelAndView mav) {
		exerciseService.saveWithAddExerciseDTO(dto);
	}
	
	@RequestMapping(value="/manager/exercise/remove" ,method = RequestMethod.DELETE) 
	public void deleteExercise(@RequestParam("exerciseId") Long exerciseId) {
		exerciseService.deleteById(exerciseId);
	}
	
	/**
	 * 클라이언트에게 근육 종류별로 분류해서 전송하는 메소드
	 * @return
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
