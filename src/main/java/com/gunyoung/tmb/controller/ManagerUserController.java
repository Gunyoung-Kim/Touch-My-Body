package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForManageViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostForManageViewDTO;
import com.gunyoung.tmb.dto.response.UserManageListDTO;
import com.gunyoung.tmb.error.codes.SearchCriteriaErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.AccessDeniedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.PageUtil;
import com.gunyoung.tmb.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;

/**
 * 매니저의 User 관리 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerUserController {
	
	private final UserService userService;
	
	private final CommentService commentService;
	
	private final ExercisePostService exercisePostService;
	
	private final RoleHierarchy roleHierarchy;
	
	/**
	 * 매니저들의 유저 검색 (for managing) 페이지 반환
	 * @param mav
	 * @param page 검색하려는 페이지
	 * @param keyword User 닉네임 검색 키워드
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage",method= RequestMethod.GET)
	public ModelAndView userManageView(@RequestParam(value="page", required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,
			ModelAndPageView mav) {
		int pageSize = PageUtil.BY_NICKNAME_NAME_PAGE_SIZE;
		
		Page<User> pageResult;
		long totalPageNum;
		
		if(keyword != null) {
			pageResult = userService.findAllByNickNameOrName(keyword, page);
			totalPageNum = userService.countAllByNickNameOrName(keyword)/pageSize +1;
		} else {
			pageResult = new PageImpl<User>(new ArrayList<>());
			totalPageNum = 1;
		}
		
		List<UserManageListDTO> listObject = new ArrayList<>();
		
		for(User p: pageResult) {
			listObject.add(UserManageListDTO.of(p));
		}
		
		mav.addObject("listObject",listObject);
		
		mav.setPageNumbers(page, pageSize, totalPageNum);
		
		mav.setViewName("userManage");
		
		return mav;
	}
	
	/**
	 * 특정 User 관리 화면 반환하는 메소드
	 * @param userId 관리하려는 대상 User의 Id
	 * @param mav
	 * @throws UserNotFoundedException 해당 Id의 User 없으면 
	 * @throws AccessDeniedException 접속자의 권한이 대상 User의 권한보다 낮다면
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}" , method = RequestMethod.GET)
	public ModelAndView manageUserProfileView(@PathVariable("user_id") Long userId, ModelAndView mav) {
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
		
		mav.addObject("userInfo", user);
		mav.addObject("roleList", myReachableAuthStringList);
		mav.addObject("userId", userId);
		
		mav.setViewName("userProfileForManage");
		
		return mav;
	}
	
	/**
	 * 특정 유저의 댓글 목록 보여주는 화면 반환하는 메소드
	 * @param userId 댓글 목록 확인하려는 대상 User의 Id
	 * @param order 검색의 정렬 조건 (최신순 또는 오래된순)
	 * @param mav
	 * @throws UserNotFoundedException 해당 Id의 User 없으면
	 * @throws SearchCriteriaInvalidException 검색 결과 정렬 방식이 올바르지 못하다면 
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}/comments", method = RequestMethod.GET) 
	public ModelAndView manageUserComments(@PathVariable("user_id") Long userId,@RequestParam(value="page", required=false,defaultValue="1") Integer page
			,@RequestParam(value="order", defaultValue="desc") String order , ModelAndPageView mav) {
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		int pageSize = PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE;
		
		Page<Comment> pageResult;
		
		if(order.equals("asc")) {
			pageResult = commentService.findAllByUserIdOrderByCreatedAtASC(userId,page,pageSize);
		} else if(order.equals("desc")) {
			pageResult = commentService.findAllByUserIdOrderByCreatedAtDESC(userId,page,pageSize);
		} else {
			throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getDescription());
		}
		
		long totalPageNum = commentService.countByUserId(userId)/pageSize+1;
		
		List<CommentForManageViewDTO> commentListForView = new ArrayList<>();
		
		for(Comment c: pageResult.getContent()) {
			CommentForManageViewDTO dto = CommentForManageViewDTO.builder()
					.commentId(c.getId())
					.userName(user.getNickName())
					.writerIp(c.getWriterIp())
					.contents(c.getContents())
					.build();
			
			commentListForView.add(dto);
		}
		
		mav.addObject("commentList", commentListForView);
		mav.addObject("userId", userId);
		mav.addObject("username", user.getFullName()+": " +user.getNickName());
		
		mav.setPageNumbers(page, pageSize, totalPageNum);
		
		mav.setViewName("userCommentList");
		
		return mav;
	}
	/**
	 * 특정 유저의 게시글 목록 보여주는 화면 반환하는 메소드
	 * @param userId 열람하려는 게시글 목록의 대상 User의 Id 
	 * @param order 검색의 정렬 조건 (최신순 또는 오래된순)
	 * @param mav
	 * @throws UserNotFoundedException 해당 Id의 User 없으면
	 * @throws SearchCriteriaInvalidException 검색 결과 정렬 방식이 올바르지 못하다면
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}/posts",method=RequestMethod.GET)
	public ModelAndView managerUserPosts(@PathVariable("user_id") Long userId, @RequestParam(value="page", required=false,defaultValue="1") Integer page
			,@RequestParam(value="order", defaultValue="desc") String order , ModelAndPageView mav) {
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		int pageSize = PageUtil.POST_FOR_MANAGE_PAGE_SIZE;
		
		Page<ExercisePost> pageResult; 
		
		if(order.equals("asc")) {
			pageResult = exercisePostService.findAllByUserIdOrderByCreatedAtAsc(userId, page, pageSize);
		} else if(order.equals("desc")) {
			pageResult = exercisePostService.findAllByUserIdOrderByCreatedAtDesc(userId, page, pageSize);
		} else {
			throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getDescription());
		}
		
		long totalPageNum = exercisePostService.countWithUserId(userId) / pageSize + 1;
		
		List<ExercisePostForManageViewDTO> postListForView = new ArrayList<>();
		
		for(ExercisePost ep: pageResult.getContent()) {
			ExercisePostForManageViewDTO dto = ExercisePostForManageViewDTO.builder()
					.postId(ep.getId())
					.title(ep.getTitle())
					.writer(user.getNickName())
					.createdAt(ep.getCreatedAt())
					.viewNum(ep.getViewNum())
					.build();
			postListForView.add(dto);
		}
		
		mav.addObject("postList", postListForView);
		mav.addObject("userId", userId);
		mav.addObject("username", user.getFullName()+": " +user.getNickName());
		
		mav.setPageNumbers(page, pageSize, totalPageNum);
		
		mav.setViewName("userPostList");
		
		return mav;
	}
	
	/**
	 * 입력된 권한으로 접근 가능한 권한이 목록 (string) 반환하는 메소드
	 * @param authorities 
	 * @return
	 * @author kimgun-yeong
	 */
	private List<String> getReachableAuthorityStrings(Collection<? extends GrantedAuthority> authorities) {
		return SecurityUtil.getAuthorityStrings(roleHierarchy.getReachableGrantedAuthorities(authorities));
	}
}
