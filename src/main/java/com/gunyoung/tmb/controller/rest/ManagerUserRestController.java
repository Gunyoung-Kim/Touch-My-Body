package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.UserProfileForManagerDTO;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.BusinessException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerUserRestController {
	
	private final UserService userService;
	
	private final CommentService commentService;
	
	private final ExercisePostService exercisePostService;
	
	/**
	 * 
	 * @param userId
	 * @param dto
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}", method = RequestMethod.PUT)
	public void manageUserProfile(@PathVariable("user_id") Long userId,@ModelAttribute UserProfileForManagerDTO dto) {
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		try {
			UserProfileForManagerDTO.mergeUserWithUserProfileForManagerDTO(user, dto);
		} catch(BusinessException be) {
			throw be;
		}
		
		userService.save(user);
	}
	
	/**
	 * 
	 * @param commentId
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}/comments/remove", method = RequestMethod.DELETE)
	public void removeCommentByManager(@PathVariable("user_id") Long userId,@RequestParam("comment_id") Long commentId) {
		commentService.deleteById(commentId);
	}
	
	/**
	 * 
	 * @param userId
	 * @param postId
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}/posts/remove",method = RequestMethod.DELETE) 
	public void removePostByManager(@PathVariable("user_id") Long userId,@RequestParam("post_id") Long postId) {
		exercisePostService.deleteById(postId);
	}
}
