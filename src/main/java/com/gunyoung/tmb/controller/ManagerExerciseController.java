package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.dto.reqeust.AddExerciseDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;

import lombok.RequiredArgsConstructor;


/**
 * 
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerExerciseController {
	
	private final ExerciseService exerciseService;
	
	@RequestMapping(value="/manager/exercise/addExercise")
	public ModelAndView addExerciseView(ModelAndView mav) {
		List<String> targetTypeKoreanNames = new ArrayList<>();
		
		for(TargetType t: TargetType.values()) {
			targetTypeKoreanNames.add(t.getKoreanName());
		}
		
		mav.setViewName("addExercise");
		mav.addObject("targetTypes", targetTypeKoreanNames);
		
		return mav;
	}
	
	
	@RequestMapping(value="/manager/exercise/addExercise" ,method = RequestMethod.POST)
	public ModelAndView addExercisePost(@ModelAttribute AddExerciseDTO dto,ModelAndView mav) {
		exerciseService.saveWithAddExerciseDTO(dto);
		
		return new ModelAndView("redirect:/manager/exercise/addExercise");
	}
	
	
}
