package com.gunyoung.tmb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExercisePostcontroller {
	
	/**
	 * 특정 운동 관련 게시글 화면 반환하는 메소드 
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/community/",method=RequestMethod.GET)
	public ModelAndView exercisePostView(ModelAndView mav) {
		
		return mav;
	}
	
	
	
}
