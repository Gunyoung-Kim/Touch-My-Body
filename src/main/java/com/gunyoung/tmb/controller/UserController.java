package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull;
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
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		
		return mav;
	}
	
	/**
	 * 로그인 화면을 반환하는 메소드 <br>
	 * 로그인 화면으로 들어오기전 위치 로그인 성공 후 리다이렉트 위해 세션에 저장
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView loginView(HttpServletRequest request,ModelAndView mav) {
		String afterLoginRedirectedUrl = request.getHeader("Referer");
		
		if(afterLoginRedirectedUrl != null && !afterLoginRedirectedUrl.contains("/login")) {
			SessionUtil.setAfterLoginRedirectedUrl(session,afterLoginRedirectedUrl);
		}
		
		mav.setViewName("login");
		
		return mav;
	}
	
	/**
	 * 회원가입 화면을 반환하는 메소드
	 * @param formModel
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/join",method=RequestMethod.GET)
	public ModelAndView joinView(@ModelAttribute("formModel")UserJoinDTO formModel,ModelAndView mav) {
		mav.setViewName("join");
		
		return mav;
	}
	
	/**
	 * 회원가입을 처리하는 메소드 <br>
	 * 입력된 password는 암호화를 거쳐 저장 <br>
	 * 정상 처리 시 메인화면으로 리다이렉트
	 * @param mav
	 * @throws EmailDuplicationFoundedException 입력된 email이 이미 존재한다면
	 * @throws NickNameDuplicationFoundedException 입력된 nickName이 이미 존재한다면
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/join",method=RequestMethod.POST)
	public ModelAndView join(@ModelAttribute("formModel")UserJoinDTO formModel) {
		String email = formModel.getEmail();
		
		if(userService.existsByEmail(email)) {
			throw new EmailDuplicationFoundedException(JoinErrorCode.EMAIL_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		String nickName = formModel.getNickName();
		
		if(userService.existsByNickName(nickName)) {
			throw new NickNameDuplicationFoundedException(JoinErrorCode.NICKNAME_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		// 비밀번호 암호화
		formModel.setPassword(passwordEncoder.encode(formModel.getPassword()));
		userService.saveByJoinDTO(formModel, RoleType.USER);
		
		return new ModelAndView("redirect:/");
	}
	
	/**
	 * 접속자의 개인정보 화면 반환하는 메소드
	 * @param mav
	 * @throws UserNotFoundedException 세션에 저장된 Id에 해당하는 User 없으면
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/profile",method=RequestMethod.GET)
	@LoginIdSessionNotNull
	public ModelAndView profileView(ModelAndView mav) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findById(loginUserId);
		
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		UserProfileDTO userProfileDTO = UserProfileDTO.of(user);
		
		mav.addObject("profile", userProfileDTO);
		
		mav.setViewName("userProfile");
		
		return mav;
	}
	
	/**
	 * 접속자의 본인이 작성한 댓글 목록 화면 반환하는 메소드
	 * @param mav
	 * @param page
	 * @param order 검색 결과의 정렬 조건 (최신순 또는 오래된순)
	 * @throws UserNotFoundedException 세션에 저장된 Id에 해당하는 User 없으면 
	 * @throws SearchCriteriaInvalidException 검색 정렬 조건이 적절치 못하다면 
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/profile/mycomments", method=RequestMethod.GET)
	@LoginIdSessionNotNull
	public ModelAndView myCommentsView(ModelAndView mav,@RequestParam(value="page", required=false,defaultValue="1") Integer page
			,@RequestParam(value="order", defaultValue="desc") String order) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findById(loginUserId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		int pageSize = PageUtil.COMMENT_FOR_PROFILE_PAGE_SIZE;
		
		Page<Comment> pageResult;
		
		if(order.equals("asc")) {
			pageResult = commentService.findAllByUserIdOrderByCreatedAtASC(loginUserId, page, pageSize);
		} else if(order.equals("desc")) {
			pageResult = commentService.findAllByUserIdOrderByCreatedAtDESC(loginUserId, page, pageSize);
		} else {
			throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getDescription());
		}
		long totalPageNum = commentService.countByUserId(loginUserId) / pageSize + 1;
		
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
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		mav.setViewName("profileCommentList");
		return mav;
	}
	
	/**
	 * 접속자의 본인이 작성한 게시글 목록 화면 반환하는 메소드
	 * @param mav
	 * @param page
	 * @param order 검색 결과의 정렬 조건 (최신순 또는 오래된순)
	 * @throws UserNotFoundedException 세션에 저장된 Id에 해당하는 User 없으면 
	 * @throws SearchCriteriaInvalidException 검색 정렬 조건이 적절치 못하다면
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/profile/myposts",method=RequestMethod.GET)
	@LoginIdSessionNotNull
	public ModelAndView myPostsView(ModelAndView mav, @RequestParam(value="page", required=false,defaultValue="1") Integer page
			,@RequestParam(value="order", defaultValue="desc") String order ) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findById(loginUserId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		int pageSize = PageUtil.POST_FOR_PROFILE_PAGE_SIZE;
		
		Page<ExercisePost> pageResult; 
		
		if(order.equals("asc")) {
			pageResult = exercisePostService.findAllByUserIdOrderByCreatedAtAsc(loginUserId,page,pageSize);
		} else if(order.equals("desc")) {
			pageResult = exercisePostService.findAllByUserIdOrderByCreatedAtDesc(loginUserId,page,pageSize);
		} else {
			throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getDescription());
		}
		
		long totalPageNum = exercisePostService.countWithUserId(loginUserId)/pageSize+1;
		
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
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		mav.setViewName("profilePostList");
		
		return mav;
	}
}
