package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.UserProfileForManagerDTO;
import com.gunyoung.tmb.dto.response.CommentForManageViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostForManageViewDTO;
import com.gunyoung.tmb.dto.response.UserManageListDTO;
import com.gunyoung.tmb.error.codes.SearchCriteriaErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.BusinessException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.PageUtil;

import lombok.RequiredArgsConstructor;

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
	 * @param keyword 검색 키워드
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage",method= RequestMethod.GET)
	public ModelAndView userManageView(@RequestParam(value="page", required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav) {
		mav.setViewName("userManage");
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
		
		List<UserManageListDTO> resultList = new ArrayList<>();
		
		for(User p: pageResult) {
			resultList.add(UserManageListDTO.of(p));
		}
		
		mav.addObject("listObject",resultList);
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		return mav;
	}
	
	/**
	 * 
	 * @param userId
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}" , method = RequestMethod.GET)
	public ModelAndView manageUserProfileView(@PathVariable("user_id") Long userId, ModelAndView mav) {
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
	
		List<String> list = getManageableRoles();
		
		mav.setViewName("userProfileForManage");
		mav.addObject("userInfo", user);
		mav.addObject("roleList", list);
		
		return mav;
	}
	
	/**
	 * 
	 * @param userId
	 * @param dto
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}", method = RequestMethod.POST)
	public ModelAndView manageUserProfile(@PathVariable("user_id") Long userId,@ModelAttribute UserProfileForManagerDTO dto) {
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		try {
			UserProfileForManagerDTO.mergeUserWithUserProfileForManagerDTO(user, dto);
		} catch(BusinessException be) {
			throw be;
		}
		
		// 만약 접속자의 것이라면 처리하는거 나중에 추가
		userService.save(user);
		
		return new ModelAndView("redirect:/manager/usermanage/"+userId);
	}
	
	/**
	 * 특정 유저의 댓글 목록 보여주는 화면 반환하는 메소드 
	 * @param userId
	 * @param order
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}/comments", method = RequestMethod.GET) 
	public ModelAndView manageUserComments(@PathVariable("user_id") Long userId,@RequestParam(value="page", required=false,defaultValue="1") Integer page
			,@RequestParam(value="order", defaultValue="desc") String order ,ModelAndView mav) {
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
		
		List<CommentForManageViewDTO> dtos = new ArrayList<>();
		
		for(Comment c: pageResult.getContent()) {
			CommentForManageViewDTO dto = CommentForManageViewDTO.builder()
					.commentId(c.getId())
					.userName(user.getNickName())
					.writerIp(c.getWriterIp())
					.contents(c.getContents())
					.build();
			
			dtos.add(dto);
		}
		
		mav.addObject("commentList", dtos);
		mav.addObject("userId", userId);
		mav.addObject("username", user.getFullName()+": " +user.getNickName());
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		mav.setViewName("userCommentList");
		return mav;
	}
	/**
	 * 특정 유저의 게시글 목록 보여주는 화면 반환하는 메소드
	 * @param userId
	 * @param order
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}/posts",method=RequestMethod.GET)
	public ModelAndView managerUserPosts(@PathVariable("user_id") Long userId, @RequestParam(value="page", required=false,defaultValue="1") Integer page
			,@RequestParam(value="order", defaultValue="desc") String order ,ModelAndView mav) {
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		int pageSize = PageUtil.POST_FOR_MANAGE_PAGE_SIZE;
		
		Page<ExercisePost> pageResult; 
		
		if(order.equals("asc")) {
			pageResult = exercisePostService.findAllByUserIdOrderByCreatedAtAsc(userId,page,pageSize);
		} else if(order.equals("desc")) {
			pageResult = exercisePostService.findAllByUserIdOrderByCreatedAtDesc(userId,page,pageSize);
		} else {
			throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getDescription());
		}
		
		long totalPageNum = exercisePostService.countWithUserId(userId)/pageSize+1;
		
		List<ExercisePostForManageViewDTO> dtos = new ArrayList<>();
		
		for(ExercisePost ep: pageResult.getContent()) {
			ExercisePostForManageViewDTO dto = ExercisePostForManageViewDTO.builder()
					.postId(ep.getId())
					.title(ep.getTitle())
					.writer(user.getNickName())
					.createdAt(ep.getCreatedAt())
					.viewNum(ep.getViewNum())
					.build();
			dtos.add(dto);
		}
		
		mav.addObject("postList", dtos);
		mav.addObject("userId", userId);
		mav.addObject("username", user.getFullName()+": " +user.getNickName());
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		mav.setViewName("userPostList");
		return mav;
	}
	
	
	/**
	 * 현재 접속자보다 roleHierarchy 상 낮거나 같은 Role들의 리스트 반환하는 메소드
	 * @return
	 * @author kimgun-yeong
	 */
	private List<String> getManageableRoles() {
		List<String> result = new ArrayList<>();
		for(GrantedAuthority a : roleHierarchy.getReachableGrantedAuthorities(SecurityContextHolder.getContext().getAuthentication().getAuthorities())) {
			result.add(a.getAuthority().substring(5));
		}
		return result;
	}
	
}
