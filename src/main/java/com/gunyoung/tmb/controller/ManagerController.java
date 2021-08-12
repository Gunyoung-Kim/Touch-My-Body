package com.gunyoung.tmb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 매니저의 메인 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
public class ManagerController {
	
	/**
	 * 매니저 메인 화면 반환하는 메소드
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager",method = RequestMethod.GET)
	public ModelAndView managerView(ModelAndView mav) {
		mav.setViewName("manager");
		return mav;
	}
}
