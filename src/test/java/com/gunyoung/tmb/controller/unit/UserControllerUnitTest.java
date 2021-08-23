package com.gunyoung.tmb.controller.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.UserController;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.error.exceptions.duplication.EmailDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.NickNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.util.UserTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link UserController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
	
	@Mock
	UserService userService;
	
	@Mock
	CommentService commentService;
	
	@Mock
	ExercisePostService exercisePostService;
	
	@Mock
	HttpSession session;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@InjectMocks
	UserController userController;
	
	private Integer defaultPageNum = 1;
	
	private ModelAndView mav;
	private ModelAndPageView mapv;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
		mapv = mock(ModelAndPageView.class);
	}
	
	/*
	 * public ModelAndView index(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("메인 화면을 반환 -> 정상, ModelAndView check")
	public void indexTestCheckMav() {
		//Given
		
		//When
		userController.index(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("index");
	}
	
	/*
	 * public ModelAndView loginView(HttpServletRequest request,ModelAndView mav)
	 */
	
	@Test
	@DisplayName("로그인 화면을 반환 -> 로그인 후 리다일렉트 주소 Referer에 있지만 /login임")
	public void loginViewTestRefererisLogin() {
		//Given
		HttpServletRequest request = stubbingRequestGetReferer("/login");			
		
		//When
		userController.loginView(request, mav);
		
		//Then
		then(session).should(never()).setAttribute(SessionUtil.AFTER_LOGIN_REDIRECTED_URL, "/login");
	}
	
	@Test
	@DisplayName("로그인 화면을 반환 -> 로그인 후 리다일렉트 주소 Referer에 없어")
	public void loginViewTestRefererNull() {
		//Given
		HttpServletRequest request = stubbingRequestGetReferer(null);
		
		//When
		userController.loginView(request, mav);
		
		//Then
		then(session).should(never()).setAttribute(anyString(), anyString());
	}
	
	@Test
	@DisplayName("로그인 화면을 반환 -> 로그인 후 리다일렉트 주소 Referer에 있고 /login 아니야")
	public void loginViewTestRefererNotLogin() {
		//Given
		String redirectedUrl = "/redir";
		HttpServletRequest request = stubbingRequestGetReferer(redirectedUrl);
		
		//When
		userController.loginView(request, mav);
		
		//Then
		then(session).should(times(1)).setAttribute(SessionUtil.AFTER_LOGIN_REDIRECTED_URL, redirectedUrl);
	}
	
	private HttpServletRequest stubbingRequestGetReferer(String referer) {
		HttpServletRequest request = mock(HttpServletRequest.class);
		given(request.getHeader("Referer")).willReturn(referer);
		return request;
	}
	
	/*
	 * public ModelAndView joinView(@ModelAttribute("formModel")UserJoinDTO formModel,ModelAndView mav)
	 */
	
	@Test
	@DisplayName("회원가입 화면을 반환 -> 정상, ModelAndView check")
	public void joinViewTestCheckMav() {
		//Given
		UserJoinDTO userJoinDTO = UserJoinDTO.builder().build();
		
		//When
		userController.joinView(userJoinDTO, mav);
		
		//Then
		then(mav).should(times(1)).setViewName("join");
	}
	
	/*
	 * public ModelAndView join(@ModelAttribute("formModel")UserJoinDTO formModel)
	 */
	
	@Test
	@DisplayName("회원가입을 처리 -> 입력된 email이 이미 존재한다면")
	public void joinTestEmailDuplicated() {
		//Given
		String existEmail = "exist@test.com";
		UserJoinDTO userJoinDTO = UserTest.getUserJoinDTOInstance(existEmail, "nickName");
		
		given(userService.existsByEmail(existEmail)).willReturn(true);
		
		//When, Then
		assertThrows(EmailDuplicationFoundedException.class, () -> {
			userController.join(userJoinDTO);
		});
	}
	
	@Test
	@DisplayName("회원가입을 처리 -> 입력된 nickName이 이미 존재한다면")
	public void joinTestNickNameDuplicated() {
		//Given
		String newEmail = "test@test.com";
		String existNickName = "exist";
		UserJoinDTO userJoinDTO = UserTest.getUserJoinDTOInstance(newEmail, existNickName);
		
		given(userService.existsByEmail(newEmail)).willReturn(false);
		given(userService.existsByNickName(existNickName)).willReturn(true);
		
		//When, Then
		assertThrows(NickNameDuplicationFoundedException.class, () -> {
			userController.join(userJoinDTO);
		});
	}
	
	@Test
	@DisplayName("회원가입을 처리 -> 정상, 비밀번호 암호화 check")
	public void joinViewTestCheckPasswordEncode() {
		//Given
		UserJoinDTO userJoinDTO = stubbingUserServiceForSuccessCaseInJoinTest();
		
		String encodedPassword = "encoded1234";
		given(passwordEncoder.encode(userJoinDTO.getPassword())).willReturn(encodedPassword);
		
		//When
		userController.join(userJoinDTO);
		
		//Then
		assertEquals(encodedPassword, userJoinDTO.getPassword());
	}
	
	@Test
	@DisplayName("회원가입을 처리 -> 정상, userService save check")
	public void joinTestCheckUserServiceSave() {
		//Given
		UserJoinDTO userJoinDTO = stubbingUserServiceForSuccessCaseInJoinTest();
		
		//When
		userController.join(userJoinDTO);
		
		//Then
		then(userService).should(times(1)).saveByJoinDTO(userJoinDTO, RoleType.USER);
	}
	
	@Test
	@DisplayName("회원가입을 처리 -> 정상, redirectedUrl check")
	public void joinTestCheckRedirectedURL() {
		//Given
		UserJoinDTO userJoinDTO = stubbingUserServiceForSuccessCaseInJoinTest();
				
		//When
		ModelAndView result = userController.join(userJoinDTO);
		
		//Then
		assertEquals("redirect:/", result.getViewName());
	}
	
	private UserJoinDTO stubbingUserServiceForSuccessCaseInJoinTest() {
		String newEmail = "test@test.com";
		String newNickName = "test";
		UserJoinDTO userJoinDTO = UserTest.getUserJoinDTOInstance(newEmail, newNickName);
		
		given(userService.existsByEmail(newEmail)).willReturn(false);
		given(userService.existsByNickName(newNickName)).willReturn(false);
		return userJoinDTO;
	}
	
	/*
	 * public ModelAndView profileView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("접속자의 개인정보 화면 반환 -> 세션에 저장된 Id에 해당하는 User 없으면")
	public void profileViewUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(76);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findById(nonExistUserId)).willReturn(null);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			userController.profileView(mav);
		});
	}
	
	@Test
	@DisplayName("접속자의 개인정보 화면 반환 -> 정상, ModelAndView check")
	public void profileViewTestCheckMav() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		//When
		userController.profileView(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("userProfile");
	}
	
	/*
	 * public ModelAndView myCommentsView(ModelAndPageView mav,@RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order)
	 */
	
	@Test
	@DisplayName("접속자의 본인이 작성한 댓글 목록 화면 반환 -> 세션에 저장된 Id에 해당하는 User 없으면")
	public void myCommentsViewTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(76);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findById(nonExistUserId)).willReturn(null);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			userController.myCommentsView(mapv, defaultPageNum, null);
		});
	}
	
	@Test
	@DisplayName("접속자의 본인이 작성한 댓글 목록 화면 반환 -> 검색 정렬 조건이 적절치 못하다면")
	public void myCommentsViewTestInValidSort() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		String inValidOrder = "invalid";
		
		//When, Then
		assertThrows(SearchCriteriaInvalidException.class, () -> {
			userController.myCommentsView(mapv, defaultPageNum, inValidOrder);
		});
	}
	
	@Test
	@DisplayName("접속자의 본인이 작성한 댓글 목록 화면 반환 -> 정상, 오름차순(asc), commentService check")
	public void myCommentsViewTestASCCheckCommentService() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		Page<Comment> pageResult = new PageImpl<>(new ArrayList<>());
		given(commentService.findAllByUserIdOrderByCreatedAtASC(loginIdInSession, defaultPageNum, UserController.MY_COMMENT_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "asc";
		
		//When
		userController.myCommentsView(mapv, defaultPageNum, order);
		
		//Then
		then(commentService).should(times(1)).countByUserId(loginIdInSession);
	}
	
	@Test
	@DisplayName("접속자의 본인이 작성한 댓글 목록 화면 반환 -> 정상, 내림차순(desc), commentService check")
	public void myCommentsViewTestDescCheckCommentService() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		Page<Comment> pageResult = new PageImpl<>(new ArrayList<>());
		given(commentService.findAllByUserIdOrderByCreatedAtDESC(loginIdInSession, defaultPageNum, UserController.MY_COMMENT_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "desc";
		
		//When
		userController.myCommentsView(mapv, defaultPageNum, order);
		
		//Then
		then(commentService).should(times(1)).countByUserId(loginIdInSession);
	}
	
	@Test
	@DisplayName("접속자의 본인이 작성한 댓글 목록 화면 반환 -> 정상, 내림차순(desc), ModelAndView check")
	public void myCommentsViewTestDescCheckMav() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		Page<Comment> pageResult = new PageImpl<>(new ArrayList<>());
		given(commentService.findAllByUserIdOrderByCreatedAtDESC(loginIdInSession, defaultPageNum, UserController.MY_COMMENT_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		Long givenCommentNum = Long.valueOf(63);
		given(commentService.countByUserId(loginIdInSession)).willReturn(givenCommentNum);
		
		String order = "desc";
		
		//When
		userController.myCommentsView(mapv, defaultPageNum, order);
		
		//Then
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenCommentNum / UserController.MY_COMMENT_LIST_VIEW_PAGE_SIZE + 1);
		then(mapv).should(times(1)).setViewName("profileCommentList");
	}
	
	/*
	 * public ModelAndView myPostsView(ModelAndPageView mav,@RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order)
	 */
	
	@Test
	@DisplayName("접속자의 본인이 작성한 게시글 목록 화면 반환 -> 세션에 저장된 Id에 해당하는 User 없으면")
	public void myPostsViewTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(76);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findById(nonExistUserId)).willReturn(null);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			userController.myPostsView(mapv, defaultPageNum, null);
		});
	}
	
	@Test
	@DisplayName("접속자의 본인이 작성한 게시글 목록 화면 반환 -> 검색 정렬 조건이 적절치 못하다면")
	public void myPostsViewTestInValidSort() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		String inValidOrder = "invalid";
		
		//When, Then
		assertThrows(SearchCriteriaInvalidException.class, () -> {
			userController.myPostsView(mapv, defaultPageNum, inValidOrder);
		});
	}
	
	@Test
	@DisplayName("접속자의 본인이 작성한 게시글 목록 화면 반환 -> 정상, 오름차순(asc), exercisePostService check")
	public void myPostsViewTestASCCheckPostService() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		Page<ExercisePost> pageResult = new PageImpl<>(new ArrayList<>());
		given(exercisePostService.findAllByUserIdOrderByCreatedAtAsc(loginIdInSession, defaultPageNum, UserController.MY_POST_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "asc";
		
		//When
		userController.myPostsView(mapv, defaultPageNum, order);
		
		//Then
		then(exercisePostService).should(times(1)).countWithUserId(loginIdInSession);
	}
	
	@Test
	@DisplayName("접속자의 본인이 작성한 게시글 목록 화면 반환 -> 정상, 내림차순(desc), exercisePostService check")
	public void myPostsViewTestDescCheckPostService() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		Page<ExercisePost> pageResult = new PageImpl<>(new ArrayList<>());
		given(exercisePostService.findAllByUserIdOrderByCreatedAtDesc(loginIdInSession, defaultPageNum, UserController.MY_POST_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "desc";
		
		//When
		userController.myPostsView(mapv, defaultPageNum, order);
		
		//Then
		then(exercisePostService).should(times(1)).countWithUserId(loginIdInSession);
	}
	
	@Test
	@DisplayName("접속자의 본인이 작성한 게시글 목록 화면 반환 -> 정상, 내림차순(desc), ModelAndView check")
	public void myPostsViewTestDescCheckMav() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		Page<ExercisePost> pageResult = new PageImpl<>(new ArrayList<>());
		given(exercisePostService.findAllByUserIdOrderByCreatedAtDesc(loginIdInSession, defaultPageNum, UserController.MY_POST_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		Long givenExercisePostNum = Long.valueOf(63);
		given(exercisePostService.countWithUserId(loginIdInSession)).willReturn(givenExercisePostNum);
		
		String order = "desc";
		
		//When
		userController.myPostsView(mapv, defaultPageNum, order);
		
		//Then
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenExercisePostNum / UserController.MY_POST_LIST_VIEW_PAGE_SIZE + 1);
		then(mapv).should(times(1)).setViewName("profilePostList");
	}
	
	private User stubbingUserServiceFindById(Long loginIdInSession) {
		User user = UserTest.getUserInstance();
		given(userService.findById(loginIdInSession)).willReturn(user);
		return user;
	}
}
