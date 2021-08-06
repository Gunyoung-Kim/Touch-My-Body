package com.gunyoung.tmb.controller.rest;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

/**
 * User 관련 요청 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class UserRestController {
	
	private final UserService userService;
	
	private final CommentService commentService;
	
	private final ExercisePostService exercisePostService;
	
	private final HttpSession session;
	
	/**
	 * email 중복여부 반환하는 메소드
	 * @param email 중복여부 확인하려는 email
	 * @return 중복여부
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="join/emailverification",method=RequestMethod.GET)
	public boolean emailVerification(@RequestParam("email") String email) {
		return userService.existsByEmail(email);
	}
	
	/**
	 * nickName 중복 여부 반환하는 메소드
	 * @param nickName 중복여부 확인하려는 nickName
	 * @return 중복여부
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="join/nickNameverification",method=RequestMethod.GET)
	public boolean nickNameVerification(@RequestParam("nickName")String nickName) {
		return userService.existsByNickName(nickName);
	}
	
	/**
	 * 접속자가 자신이 작성한 특정 댓글 삭제 요청 처리하는 메소드
	 * @param commentId 삭제하려는 대상 Comment의 Id
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/profile/mycomments/remove", method=RequestMethod.DELETE)
	@LoginIdSessionNotNull
	public void removeMyComments(@RequestParam("commentId") Long commentId) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		
		commentService.checkIsMineAndDelete(loginUserId, commentId);
	}
	
	/**
	 * 접속자가 자신이 작성한 특정 게시글 삭제 요청 처리하는 메소드
	 * @param postId 삭제하려는 대상 exercisePost의 Id
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/profile/myposts/remove",method=RequestMethod.DELETE)
	@LoginIdSessionNotNull
	public void removeMyPosts(@RequestParam("postId") Long postId) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		
		exercisePostService.checkIsMineAndDelete(loginUserId, postId);
	}
}
