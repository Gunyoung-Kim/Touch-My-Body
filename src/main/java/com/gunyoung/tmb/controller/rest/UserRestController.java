package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserRestController {
	
	private final UserService userService;
	
	private final CommentService commentService;
	
	private final ExercisePostService exercisePostService;
	
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
	
	/**
	 * 
	 * @param commentId
	 */
	@RequestMapping(value="/user/profile/mycomments/remove", method=RequestMethod.DELETE)
	public void removeMyComments(@RequestParam("commentId") Long commentId) {
		commentService.deleteById(commentId);
	}
	
	/**
	 * 
	 * @param postId
	 */
	@RequestMapping(value="/user/profile/myposts/remove",method=RequestMethod.DELETE) 
	public void removeMyPosts(@RequestParam("postId") Long postId) {
		exercisePostService.deleteById(postId);
	}
}
