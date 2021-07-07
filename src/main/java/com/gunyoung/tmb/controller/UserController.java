package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.dto.response.CommentForManageViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostForManageViewDTO;
import com.gunyoung.tmb.dto.response.UserProfileDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.error.codes.JoinErrorCode;
import com.gunyoung.tmb.error.codes.SearchCriteriaErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.EmailDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.NickNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.PageUtil;
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

/**
 * User 관련 처리 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	private final CommentService commentService;
	
	private final ExercisePostService exercisePostService;
	
	private final HttpSession session;
	
	private final PasswordEncoder passwordEncoder;
	
	
	/**
	 * 메인 화면을 반환하는 메소드
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		
		System.out.println(SessionUtil.getLoginUserId(session));
		
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
		
		// 비밀번호 암호화
		formModel.setPassword(passwordEncoder.encode(formModel.getPassword()));
		userService.saveByJoinDTO(formModel, RoleType.USER);
		
		return new ModelAndView("redirect:/");
	}
	
	/**
	 * 
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/user/profile",method=RequestMethod.GET)
	public ModelAndView profileView(ModelAndView mav) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findById(userId);
		
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.UserNotFoundedError.getDescription());
		}
		
		UserProfileDTO dto = UserProfileDTO.of(user);
		
		mav.setViewName("userProfile");
		mav.addObject("profile", dto);
		return mav;
	}
	
	/**
	 * 
	 * @param mav
	 * @param page
	 * @param order
	 * @return
	 */
	@RequestMapping(value="/user/profile/mycomments", method=RequestMethod.GET)
	public ModelAndView myCommentsView(ModelAndView mav,@RequestParam(value="page", required=false,defaultValue="1") Integer page
			,@RequestParam(value="order", defaultValue="desc") String order) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.UserNotFoundedError.getDescription());
		}
		
		int pageSize = PageUtil.COMMENT_FOR_PROFILE_PAGE_SIZE;
		
		Page<Comment> pageResult;
		
		if(order.equals("asc")) {
			pageResult = commentService.findAllByUserIdOrderByCreatedAtASC(userId,page,pageSize);
		} else if(order.equals("desc")) {
			pageResult = commentService.findAllByUserIdOrderByCreatedAtDESC(userId,page,pageSize);
		} else {
			throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.OrderByCriteriaError.getDescription());
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
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		mav.setViewName("profileCommentList");
		return mav;
	}
	
	/**
	 * 
	 * @param mav
	 * @param page
	 * @param order
	 * @return
	 */
	@RequestMapping(value="/user/profile/myposts",method=RequestMethod.GET)
	public ModelAndView myPostsView(ModelAndView mav, @RequestParam(value="page", required=false,defaultValue="1") Integer page
			,@RequestParam(value="order", defaultValue="desc") String order ) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findById(userId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.UserNotFoundedError.getDescription());
		}
		int pageSize = PageUtil.POST_FOR_PROFILE_PAGE_SIZE;
		
		Page<ExercisePost> pageResult; 
		
		if(order.equals("asc")) {
			pageResult = exercisePostService.findAllByUserIdOrderByCreatedAtAsc(userId,page,pageSize);
		} else if(order.equals("desc")) {
			pageResult = exercisePostService.findAllByUserIdOrderByCreatedAtDesc(userId,page,pageSize);
		} else {
			throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.OrderByCriteriaError.getDescription());
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
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		mav.setViewName("profilePostList");
		
		return mav;
	}
}
