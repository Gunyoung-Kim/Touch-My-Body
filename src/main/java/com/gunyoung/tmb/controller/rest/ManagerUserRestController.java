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
import com.gunyoung.tmb.error.exceptions.request.AccessDeniedException;
import com.gunyoung.tmb.security.AuthorityService;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 매니저의 User 관련 요청 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ManagerUserRestController {
	
	private final UserService userService;
	
	private final CommentService commentService;
	
	private final ExercisePostService exercisePostService;
	
	private final AuthorityService authorityService;
	
	/**
	 * User의 정보 수정 요청 처리하는 메소드 (아직은 권한만 변경 가능)
	 * @param userId 정보 수정하려는 대상 User의 Id
	 * @throws UserNotFoundedException 해당 Id의 User 없으면
	 * @throws AccessDeniedException 접속자가 대상 User 보다 권한이 낮으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{userId}", method = RequestMethod.PUT)
	public void manageUserProfile(@PathVariable("userId") Long userId,@ModelAttribute UserProfileForManagerDTO dto) {
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		if(!authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(user)) {
			throw new AccessDeniedException(UserErrorCode.ACESS_DENIED_ERROR.getDescription());
		}
		
		try {
			UserProfileForManagerDTO.mergeUserWithUserProfileForManagerDTO(user, dto);
		} catch(BusinessException be) {
			throw be;
		}
		
		userService.save(user);
	}
	
	/**
	 * 특정 유저가 작성한 댓글 삭제 요청 처리하는 메소드
	 * @param userId 댓글 작성한 User의 Id
	 * @param commentId 삭제하려는 대상 Comment의 Id
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}/comments/remove", method = RequestMethod.DELETE)
	public void removeCommentByManager(@PathVariable("user_id") Long userId,@RequestParam("comment_id") Long commentId) {
		commentService.deleteById(commentId);
	}
	
	/**
	 * 특정 유저가 작성한 게시글 삭제 요청 처리하는 메소드
	 * @param userId 게시글 작성한 User의 Id
	 * @param postId 삭제하려는 대상 ExercisePost의 Id
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}/posts/remove",method = RequestMethod.DELETE) 
	public void removePostByManager(@PathVariable("user_id") Long userId,@RequestParam("post_id") Long postId) {
		exercisePostService.deleteById(postId);
	}
}
