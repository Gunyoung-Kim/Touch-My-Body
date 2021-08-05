package com.gunyoung.tmb.controller.rest;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.SecurityUtil;

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
	
	private final RoleHierarchy roleHierarchy;
	
	/**
	 * User의 정보 수정 요청 처리하는 메소드 (아직은 권한만 변경 가능)
	 * @param userId 정보 수정하려는 대상 User의 Id
	 * @param dto
	 * @throws UserNotFoundedException 해당 Id의 User 없으면
	 * @throws AccessDeniedException 접속자가 대상 User 보다 권한이 낮으면
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{userId}", method = RequestMethod.PUT)
	public void manageUserProfile(@PathVariable("userId") Long userId,@ModelAttribute UserProfileForManagerDTO dto) {
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		List<String> myReachableAuthStringList = getReachableAuthorityStrings(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		List<String> targetReacheableStringList = getReachableAuthorityStrings(SecurityUtil.getAuthoritiesByUserRoleType(user.getRole()));
		
		/*
		 *  접속자의 권한이 대상 유저의 권한 보다 낮으면 예외 발생 
		 *  권한 구조가 일자이기에 가능한 로직, 추후에 일자에서 변경 되면 이 로직도 변경 요망
		 */
		if(targetReacheableStringList.size() > myReachableAuthStringList.size()) {
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
	
	/**
	 * 입력된 권한이 접근 가능한 권한 목록 반환하는 메소드
	 * @param authorities
	 * @return
	 * @author kimgun-yeong
	 */
	private List<String> getReachableAuthorityStrings(Collection<? extends GrantedAuthority> authorities) {
		return SecurityUtil.getAuthorityStrings(roleHierarchy.getReachableGrantedAuthorities(authorities));
	}
}
