package com.gunyoung.tmb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExerciseController {
	
	@RequestMapping(value="/exercise",method = RequestMethod.GET)
	public ModelAndView exerciseInfoMainView(ModelAndView mav) {
		return mav;
	}
}
