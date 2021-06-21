package com.gunyoung.tmb.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.services.domain.user.UserService;

@RestController
public class UserRestController {
	@Autowired
	UserService userService;
	
	/**
	 * 
	 * @param email 중복여부 확인하려는 email
	 * @return 중복여부
	 */
	@RequestMapping(value="join/emailverification",method=RequestMethod.GET)
	public boolean emailVerification(@RequestParam("email") String email) {
		return userService.existsByEmail(email);
	}
	
	/**
	 * 
	 * @param nickName 중복여부 확인하려는 nickName
	 * @return 중복여부
	 */
	@RequestMapping(value="join/nickNameverification",method=RequestMethod.GET)
	public boolean nickNameVerification(@RequestParam("nickName")String nickName) {
		return userService.existsByNickName(nickName);
	}
	
}
