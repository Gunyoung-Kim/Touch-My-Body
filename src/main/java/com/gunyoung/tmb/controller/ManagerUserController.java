package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForManageViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostForManageViewDTO;
import com.gunyoung.tmb.dto.response.UserManageListDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.error.codes.SearchCriteriaErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.AccessDeniedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;
import com.gunyoung.tmb.security.AuthorityService;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 매니저의 User 관리 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerUserController {
	
	public static final int USER_MANAGE_VIEW_PAGE_SIZE = PageSize.BY_NICKNAME_NAME_PAGE_SIZE.getSize();
	public static final int USER_COMMENT_LIST_VIEW_FOR_MANAGER_PAGE_SIZE = PageSize.COMMENT_FOR_MANAGE_PAGE_SIZE.getSize();
	public static final int USER_POST_LIST_VIEW_FOR_MANAGER_PAGE_SIZE = PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize();
	
	private static final String ATTRIBUTE_NAME_OF_USER_ID = "userId";
	
	private final UserService userService;
	
	private final CommentService commentService;
	
	private final ExercisePostService exercisePostService;
	
	private final AuthorityService authorityService;
	
	/**
	 * 매니저들의 유저 검색 (for managing) 페이지 반환
	 * @param page 검색하려는 페이지
	 * @param keyword User 닉네임 검색 키워드
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/manager/usermanage")
	public ModelAndView userManageView(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "keyword", required = false) String keyword, ModelAndPageView mav) {
		Page<User> pageResult = getPageResultForUserManageView(keyword, page);
		long totalPageNum = getTotalPageNumForUserManageView(keyword);
		
		List<UserManageListDTO> listObject = UserManageListDTO.of(pageResult);
		
		mav.addObject("listObject",listObject);
		mav.setPageNumbers(page, totalPageNum);
		
		mav.setViewName("userManage");
		
		return mav;
	}
	
	private Page<User> getPageResultForUserManageView(String keyword, Integer page) {
		if(keyword == null) {
			return new PageImpl<>(new ArrayList<>());
		}
		return userService.findAllByNickNameOrNameInPage(keyword, page);
	}
	
	private long getTotalPageNumForUserManageView(String keyword) {
		if(keyword == null) {
			return 1;
		}
		return userService.countAllByNickNameOrName(keyword)/USER_MANAGE_VIEW_PAGE_SIZE +1;
	}
	
	/**
	 * 특정 User 관리 화면 반환하는 메소드
	 * @param userId 관리하려는 대상 User의 Id
	 * @throws UserNotFoundedException 해당 Id의 User 없으면 
	 * @throws AccessDeniedException 접속자의 권한이 대상 User의 권한보다 낮다면
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/manager/usermanage/{user_id}")
	public ModelAndView manageUserProfileView(@PathVariable("user_id") Long userId, ModelAndView mav) {
		User targetUser = userService.findById(userId);
		if(!authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(targetUser)) {
			throw new AccessDeniedException(UserErrorCode.ACESS_DENIED_ERROR.getDescription());
		}
		
		Collection<? extends GrantedAuthority> sessionUserAuthorities = authorityService.getSessionUserAuthorities();
		List<String> myReachableAuthStringList = authorityService.getReachableAuthorityStrings(sessionUserAuthorities);
		
		mav.addObject(ATTRIBUTE_NAME_OF_USER_ID, userId);
		mav.addObject("userInfo", targetUser);
		mav.addObject("roleList", myReachableAuthStringList);
		
		mav.setViewName("userProfileForManage");
		
		return mav;
	}
	
	/**
	 * 특정 유저의 댓글 목록 보여주는 화면 반환하는 메소드
	 * @param userId 댓글 목록 확인하려는 대상 User의 Id
	 * @param order 검색의 정렬 조건 (최신순 또는 오래된순)
	 * @throws UserNotFoundedException 해당 Id의 User 없으면
	 * @throws SearchCriteriaInvalidException 검색 결과 정렬 방식이 올바르지 못하다면 
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/manager/usermanage/{user_id}/comments") 
	public ModelAndView manageUserComments(@PathVariable("user_id") Long userId, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "order", defaultValue = "desc") String order, ModelAndPageView mav) {
		User user = userService.findById(userId);
		
		Page<Comment> pageResult = getPageResultForUserCommentListViewForManage(order, userId, page);
		long totalPageNum = getTotalPageNumForUserCommentListViewForManage(userId);
		
		List<CommentForManageViewDTO> commentListForView = CommentForManageViewDTO.of(pageResult, user);
		
		mav.addObject(ATTRIBUTE_NAME_OF_USER_ID, userId);
		mav.addObject("username", user.getFullName() + " : " + user.getNickName());
		mav.setPageNumbers(page, totalPageNum);
		mav.addObject("commentList", commentListForView);
		
		mav.setViewName("userCommentList");
		
		return mav;
	}
	
	private Page<Comment> getPageResultForUserCommentListViewForManage(String order, Long userId, int page) throws SearchCriteriaInvalidException {
		if(order.equals("asc")) {
			return commentService.findAllByUserIdOrderByCreatedAtAsc(userId, page, USER_COMMENT_LIST_VIEW_FOR_MANAGER_PAGE_SIZE);
		} else if(order.equals("desc")) {
			return commentService.findAllByUserIdOrderByCreatedAtDesc(userId, page, USER_COMMENT_LIST_VIEW_FOR_MANAGER_PAGE_SIZE);
		}
		throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getDescription());
	}
	
	private long getTotalPageNumForUserCommentListViewForManage(Long userId) {
		return commentService.countByUserId(userId) / USER_COMMENT_LIST_VIEW_FOR_MANAGER_PAGE_SIZE+1;
	}
	
	/**
	 * 특정 유저의 게시글 목록 보여주는 화면 반환하는 메소드
	 * @param userId 열람하려는 게시글 목록의 대상 User의 Id 
	 * @param order 검색의 정렬 조건 (최신순 또는 오래된순)
	 * @throws UserNotFoundedException 해당 Id의 User 없으면
	 * @throws SearchCriteriaInvalidException 검색 결과 정렬 방식이 올바르지 못하다면
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/manager/usermanage/{userId}/posts")
	public ModelAndView managerUserPosts(@PathVariable("userId") Long userId, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "order", defaultValue = "desc") String order, ModelAndPageView mav) {
		User user = userService.findById(userId);
		
		Page<ExercisePost> pageResult = getPageResultForUserPostListViewForManage(order, userId, page);
		long totalPageNum = getTotalPageNumForUserPostListViewForManage(userId);
		
		List<ExercisePostForManageViewDTO> postListForView = ExercisePostForManageViewDTO.of(pageResult, user);
		
		mav.addObject(ATTRIBUTE_NAME_OF_USER_ID, userId);
		mav.addObject("username", user.getFullName()+": " +user.getNickName());
		mav.setPageNumbers(page, totalPageNum);
		mav.addObject("postList", postListForView);
		
		mav.setViewName("userPostList");
		
		return mav;
	}
	
	private Page<ExercisePost> getPageResultForUserPostListViewForManage(String order, Long userId, Integer page) throws SearchCriteriaInvalidException {
		if(order.equals("asc")) {
			return exercisePostService.findAllByUserIdOrderByCreatedAtAsc(userId, page, USER_POST_LIST_VIEW_FOR_MANAGER_PAGE_SIZE);
		} else if(order.equals("desc")) {
			return exercisePostService.findAllByUserIdOrderByCreatedAtDesc(userId, page, USER_POST_LIST_VIEW_FOR_MANAGER_PAGE_SIZE);
		}
		throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getDescription());
	}
	
	private long getTotalPageNumForUserPostListViewForManage(Long userId) {
		return exercisePostService.countWithUserId(userId) / USER_POST_LIST_VIEW_FOR_MANAGER_PAGE_SIZE + 1;
	}
}
