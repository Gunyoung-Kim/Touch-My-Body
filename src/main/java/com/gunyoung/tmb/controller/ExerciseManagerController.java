package com.gunyoung.tmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.dto.reqeust.AddExerciseDTO;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;

@Controller
public class ExerciseManagerController {
	
	@Autowired
	ExerciseService exerciseService;
	
	@Autowired
	MuscleService muscleService;
	
	@RequestMapping(value="/manager/exercise")
	public ModelAndView manageExerciseView(ModelAndView mav) {
		return mav;
	}
	
	
	@RequestMapping(value="/manager/exercise/addExercise")
	public ModelAndView addExerciseView(ModelAndView mav) {
		return mav;
	}
	
	
	@RequestMapping(value="/manager/exercise/addExercise" ,method = RequestMethod.POST)
	public ModelAndView addExercisePost(@ModelAttribute AddExerciseDTO dto,ModelAndView mav) {
		exerciseService.saveWithAddExerciseDTO(dto);
		
		return new ModelAndView("redirect:/manager/exercise/addExercise");
	}
	
	
}
