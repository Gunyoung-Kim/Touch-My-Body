package com.gunyoung.tmb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.dto.response.CommentForManageViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostForManageViewDTO;
import com.gunyoung.tmb.dto.response.UserProfileDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.error.codes.JoinErrorCode;
import com.gunyoung.tmb.error.codes.SearchCriteriaErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.EmailDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.NickNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
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
	
	public static final int MY_COMMENT_LIST_VIEW_PAGE_SIZE = PageSize.COMMENT_FOR_PROFILE_PAGE_SIZE.getSize();
	public static final int MY_POST_LIST_VIEW_PAGE_SIZE = PageSize.POST_FOR_PROFILE_PAGE_SIZE.getSize();
	
	private final UserService userService;
	
	private final CommentService commentService;
	
	private final ExercisePostService exercisePostService;
	
	private final HttpSession session;
	
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * 메인 화면을 반환하는 메소드
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/")
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		
		return mav;
	}
	
	/**
	 * 로그인 화면을 반환하는 메소드 <br>
	 * 로그인 화면으로 들어오기전 위치 로그인 성공 후 리다이렉트 위해 세션에 저장
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/login")
	public ModelAndView loginView(HttpServletRequest request, ModelAndView mav) {
		String referer = request.getHeader("Referer");
		if(isRefererNeedToBeRegisteredForSession(referer)) {
			SessionUtil.setAfterLoginRedirectedUrl(session, referer);
		}
		
		mav.setViewName("login");
		
		return mav;
	}
	
	private boolean isRefererNeedToBeRegisteredForSession(String referer) {
		return referer != null && !referer.contains("/login");
	}
	
	/**
	 * 회원가입 화면을 반환하는 메소드
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/join")
	public ModelAndView joinView(@ModelAttribute("formModel")UserJoinDTO formModel,ModelAndView mav) {
		mav.setViewName("join");
		
		return mav;
	}
	
	/**
	 * 회원가입을 처리하는 메소드 <br>
	 * 입력된 password는 암호화를 거쳐 저장 <br>
	 * 정상 처리 시 메인화면으로 리다이렉트
	 * @throws EmailDuplicationFoundedException 입력된 email이 이미 존재한다면
	 * @throws NickNameDuplicationFoundedException 입력된 nickName이 이미 존재한다면
	 * @author kimgun-yeong
	 */
	@PostMapping(value = "/join")
	public ModelAndView join(@ModelAttribute("formModel") UserJoinDTO formModel) {
		String email = formModel.getEmail();
		if(userService.existsByEmail(email)) {
			throw new EmailDuplicationFoundedException(JoinErrorCode.EMAIL_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		String nickName = formModel.getNickName();
		if(userService.existsByNickName(nickName)) {
			throw new NickNameDuplicationFoundedException(JoinErrorCode.NICKNAME_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		encodePasswordOfFormModel(formModel);
		saveNewUserWithUserRole(formModel);
		
		return new ModelAndView("redirect:/");
	}
	
	private void encodePasswordOfFormModel(UserJoinDTO formModel) {
		formModel.setPassword(passwordEncoder.encode(formModel.getPassword()));
	}
	
	private void saveNewUserWithUserRole(UserJoinDTO formModel) {
		userService.saveByJoinDTOAndRoleType(formModel, RoleType.USER);
	}
	
	/**
	 * 접속자의 개인정보 화면 반환하는 메소드
	 * @throws UserNotFoundedException 세션에 저장된 Id에 해당하는 User 없으면
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/user/profile")
	@LoginIdSessionNotNull
	public ModelAndView profileView(ModelAndView mav) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		User user = userService.findById(loginUserId);
		UserProfileDTO userProfileDTO = UserProfileDTO.of(user);
		
		mav.addObject("profile", userProfileDTO);
		
		mav.setViewName("userProfile");
		
		return mav;
	}
	
	/**
	 * 접속자의 본인이 작성한 댓글 목록 화면 반환하는 메소드
	 * @param order 검색 결과의 정렬 조건 (최신순 또는 오래된순)
	 * @throws UserNotFoundedException 세션에 저장된 Id에 해당하는 User 없으면 
	 * @throws SearchCriteriaInvalidException 검색 정렬 조건이 적절치 못하다면 
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/user/profile/mycomments")
	@LoginIdSessionNotNull
	public ModelAndView myCommentsView(ModelAndPageView mav, @RequestParam(value = "page", required = false, defaultValue= "1") int page
			, @RequestParam(value = "order", defaultValue = "desc") String order) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		User user = userService.findById(loginUserId);
		
		Page<Comment> pageResult = getPageResultForMyCommentListView(order, loginUserId, page);
		long totalPageNum = getTotalPageNumForMyCommentListView(loginUserId);
		
		List<CommentForManageViewDTO> commentListForView = CommentForManageViewDTO.of(pageResult, user);

		mav.setPageNumbers(page, totalPageNum);
		mav.addObject("commentList", commentListForView);

		mav.setViewName("profileCommentList");
		
		return mav;
	}
	
	private Page<Comment> getPageResultForMyCommentListView(String order, Long loginUserId, int page) throws SearchCriteriaInvalidException {
		if(order.equals("asc")) {
			return commentService.findAllByUserIdOrderByCreatedAtAsc(loginUserId, page, MY_COMMENT_LIST_VIEW_PAGE_SIZE);
		} else if(order.equals("desc")) {
			return commentService.findAllByUserIdOrderByCreatedAtDesc(loginUserId, page, MY_COMMENT_LIST_VIEW_PAGE_SIZE);
		}
		throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getDescription());
	}
	
	private long getTotalPageNumForMyCommentListView(Long loginUserId) {
		return commentService.countByUserId(loginUserId) / MY_COMMENT_LIST_VIEW_PAGE_SIZE + 1;
	}
	
	/**
	 * 접속자의 본인이 작성한 게시글 목록 화면 반환하는 메소드
	 * @param order 검색 결과의 정렬 조건 (최신순 또는 오래된순)
	 * @throws UserNotFoundedException 세션에 저장된 Id에 해당하는 User 없으면 
	 * @throws SearchCriteriaInvalidException 검색 정렬 조건이 적절치 못하다면
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/user/profile/myposts")
	@LoginIdSessionNotNull
	public ModelAndView myPostsView(ModelAndPageView mav, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "order", defaultValue = "desc") String order) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		User user = userService.findById(loginUserId);
		
		Page<ExercisePost> pageResult = getPageResultForMyPostListView(order, loginUserId, page);
		long totalPageNum = getTotalPageNumForMyPostListView(loginUserId);
		
		List<ExercisePostForManageViewDTO> postListForView = ExercisePostForManageViewDTO.of(pageResult, user);
		
		mav.setPageNumbers(page, totalPageNum);
		mav.addObject("postList", postListForView);
		
		mav.setViewName("profilePostList");
		
		return mav;
	}
	
	private Page<ExercisePost> getPageResultForMyPostListView(String order, Long loginUserId, int page) throws SearchCriteriaInvalidException {
		if(order.equals("asc")) {
			return exercisePostService.findAllByUserIdOrderByCreatedAtAsc(loginUserId,page,MY_POST_LIST_VIEW_PAGE_SIZE);
		} else if(order.equals("desc")) {
			return exercisePostService.findAllByUserIdOrderByCreatedAtDesc(loginUserId,page,MY_POST_LIST_VIEW_PAGE_SIZE);
		}
		throw new SearchCriteriaInvalidException(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getDescription());
	}
	
	private long getTotalPageNumForMyPostListView(Long loginUserId) {
		return exercisePostService.countWithUserId(loginUserId)/MY_POST_LIST_VIEW_PAGE_SIZE + 1;
	}
}
