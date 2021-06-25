package com.gunyoung.tmb.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.ExerciseInfoDTO;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;

@RestController
public class ExerciseRestController {
	
	@Autowired
	ExerciseService exerciseService;
	
	@RequestMapping(value="/exericse/about/info",method = RequestMethod.GET)
	public ExerciseInfoDTO getExerciseInfo(@RequestParam("name") String name) {
		Exercise exercise = exerciseService.findByName(name);
		
		if(exercise == null)
			throw new ExerciseNotFoundedException(ExerciseErrorCode.ExerciseByNameNotFoundedError.getDescription());
		
		ExerciseInfoDTO response = ExerciseInfoDTO.of(exercise);
		
		return response;
	}
}
