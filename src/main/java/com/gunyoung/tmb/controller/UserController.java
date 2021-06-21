package com.gunyoung.tmb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.dto.UserJoinDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.error.codes.JoinErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.EmailDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.NickNameDuplicationFoundedException;
import com.gunyoung.tmb.services.domain.user.UserService;

/**
 * User 관련 처리 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	/**
	 * 메인 화면을 반환하는 메소드
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		
		return mav;
	}
	
	/**
	 * 로그인 화면을 반환하는 메소드
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView loginView(ModelAndView mav) {
		mav.setViewName("login");
		
		return mav;
	}
	
	/**
	 * 회원가입 화면을 반환하는 메소드
	 * @param formModel
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/join",method=RequestMethod.GET)
	public ModelAndView joinView(@ModelAttribute("formModel")UserJoinDTO formModel,ModelAndView mav) {
		mav.setViewName("join");
		
		return mav;
	}
	
	/**
	 * 회원가입을 처리하는 메소드
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/join",method=RequestMethod.POST)
	public ModelAndView join(@ModelAttribute("formModel")UserJoinDTO formModel) {
		String email = formModel.getEmail();
		
		if(userService.existsByEmail(email)) {
			throw new EmailDuplicationFoundedException(JoinErrorCode.EmailDuplicationFound.getDescription());
		}
		
		String nickName = formModel.getNickName();
		
		if(userService.existsByNickName(nickName)) {
			throw new NickNameDuplicationFoundedException(JoinErrorCode.NickNameDuplicationFound.getDescription());
		}
		
		userService.saveByJoinDTO(formModel, RoleType.USER);
		
		return new ModelAndView("redirect:/");
	}
}
