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
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.EmailDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.NickNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link UserController} ??? ?????? ????????? ????????? <br>
 * ????????? ??????: (?????? ?????????) UserController only
 * {@link org.mockito.BDDMockito}??? ????????? ???????????? ????????? ?????? ?????? ?????????
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {
	
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
	 * ModelAndView index(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("?????? ????????? ?????? -> ??????, ModelAndView check")
	void indexTestCheckMav() {
		//Given
		
		//When
		userController.index(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("index");
	}
	
	/*
	 * ModelAndView loginView(HttpServletRequest request,ModelAndView mav)
	 */
	
	@Test
	@DisplayName("????????? ????????? ?????? -> ????????? ??? ??????????????? ?????? Referer??? ????????? /login???")
	void loginViewTestRefererisLogin() {
		//Given
		HttpServletRequest request = stubbingRequestGetReferer("/login");			
		
		//When
		userController.loginView(request, mav);
		
		//Then
		then(session).should(never()).setAttribute(SessionUtil.AFTER_LOGIN_REDIRECTED_URL, "/login");
	}
	
	@Test
	@DisplayName("????????? ????????? ?????? -> ????????? ??? ??????????????? ?????? Referer??? ??????")
	void loginViewTestRefererNull() {
		//Given
		HttpServletRequest request = stubbingRequestGetReferer(null);
		
		//When
		userController.loginView(request, mav);
		
		//Then
		then(session).should(never()).setAttribute(anyString(), anyString());
	}
	
	@Test
	@DisplayName("????????? ????????? ?????? -> ????????? ??? ??????????????? ?????? Referer??? ?????? /login ?????????")
	void loginViewTestRefererNotLogin() {
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
	 * ModelAndView joinView(@ModelAttribute("formModel")UserJoinDTO formModel,ModelAndView mav)
	 */
	
	@Test
	@DisplayName("???????????? ????????? ?????? -> ??????, ModelAndView check")
	void joinViewTestCheckMav() {
		//Given
		UserJoinDTO userJoinDTO = UserJoinDTO.builder().build();
		
		//When
		userController.joinView(userJoinDTO, mav);
		
		//Then
		then(mav).should(times(1)).setViewName("join");
	}
	
	/*
	 * ModelAndView join(@ModelAttribute("formModel")UserJoinDTO formModel)
	 */
	
	@Test
	@DisplayName("??????????????? ?????? -> ????????? email??? ?????? ???????????????")
	void joinTestEmailDuplicated() {
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
	@DisplayName("??????????????? ?????? -> ????????? nickName??? ?????? ???????????????")
	void joinTestNickNameDuplicated() {
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
	@DisplayName("??????????????? ?????? -> ??????, ???????????? ????????? check")
	void joinViewTestCheckPasswordEncode() {
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
	@DisplayName("??????????????? ?????? -> ??????, userService save check")
	void joinTestCheckUserServiceSave() {
		//Given
		UserJoinDTO userJoinDTO = stubbingUserServiceForSuccessCaseInJoinTest();
		
		//When
		userController.join(userJoinDTO);
		
		//Then
		then(userService).should(times(1)).saveByJoinDTOAndRoleType(userJoinDTO, RoleType.USER);
	}
	
	@Test
	@DisplayName("??????????????? ?????? -> ??????, redirectedUrl check")
	void joinTestCheckRedirectedURL() {
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
	 * ModelAndView profileView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("???????????? ???????????? ?????? ?????? -> ????????? ????????? Id??? ???????????? User ?????????")
	void profileViewUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(76);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findById(nonExistUserId)).willThrow(new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription()));
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			userController.profileView(mav);
		});
	}
	
	@Test
	@DisplayName("???????????? ???????????? ?????? ?????? -> ??????, ModelAndView check")
	void profileViewTestCheckMav() {
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
	 * ModelAndView myCommentsView(ModelAndPageView mav,@RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order)
	 */
	
	@Test
	@DisplayName("???????????? ????????? ????????? ?????? ?????? ?????? ?????? -> ????????? ????????? Id??? ???????????? User ?????????")
	void myCommentsViewTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(76);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findById(nonExistUserId)).willThrow(new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription()));
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			userController.myCommentsView(mapv, defaultPageNum, null);
		});
	}
	
	@Test
	@DisplayName("???????????? ????????? ????????? ?????? ?????? ?????? ?????? -> ?????? ?????? ????????? ????????? ????????????")
	void myCommentsViewTestInValidSort() {
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
	@DisplayName("???????????? ????????? ????????? ?????? ?????? ?????? ?????? -> ??????, ????????????(asc), commentService check")
	void myCommentsViewTestASCCheckCommentService() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		Page<Comment> pageResult = new PageImpl<>(new ArrayList<>());
		given(commentService.findAllByUserIdOrderByCreatedAtAsc(loginIdInSession, defaultPageNum, UserController.MY_COMMENT_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "asc";
		
		//When
		userController.myCommentsView(mapv, defaultPageNum, order);
		
		//Then
		then(commentService).should(times(1)).countByUserId(loginIdInSession);
	}
	
	@Test
	@DisplayName("???????????? ????????? ????????? ?????? ?????? ?????? ?????? -> ??????, ????????????(desc), commentService check")
	void myCommentsViewTestDescCheckCommentService() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		Page<Comment> pageResult = new PageImpl<>(new ArrayList<>());
		given(commentService.findAllByUserIdOrderByCreatedAtDesc(loginIdInSession, defaultPageNum, UserController.MY_COMMENT_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "desc";
		
		//When
		userController.myCommentsView(mapv, defaultPageNum, order);
		
		//Then
		then(commentService).should(times(1)).countByUserId(loginIdInSession);
	}
	
	@Test
	@DisplayName("???????????? ????????? ????????? ?????? ?????? ?????? ?????? -> ??????, ????????????(desc), ModelAndView check")
	void myCommentsViewTestDescCheckMav() {
		//Given
		Long loginIdInSession = Long.valueOf(33);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindById(loginIdInSession);
		
		Page<Comment> pageResult = new PageImpl<>(new ArrayList<>());
		given(commentService.findAllByUserIdOrderByCreatedAtDesc(loginIdInSession, defaultPageNum, UserController.MY_COMMENT_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
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
	 * ModelAndView myPostsView(ModelAndPageView mav,@RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order)
	 */
	
	@Test
	@DisplayName("???????????? ????????? ????????? ????????? ?????? ?????? ?????? -> ????????? ????????? Id??? ???????????? User ?????????")
	void myPostsViewTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(76);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findById(nonExistUserId)).willThrow(new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription()));
		
		String order = "asc";
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			userController.myPostsView(mapv, defaultPageNum, order);
		});
	}
	
	@Test
	@DisplayName("???????????? ????????? ????????? ????????? ?????? ?????? ?????? -> ?????? ?????? ????????? ????????? ????????????")
	void myPostsViewTestInValidSort() {
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
	@DisplayName("???????????? ????????? ????????? ????????? ?????? ?????? ?????? -> ??????, ????????????(asc), exercisePostService check")
	void myPostsViewTestASCCheckPostService() {
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
	@DisplayName("???????????? ????????? ????????? ????????? ?????? ?????? ?????? -> ??????, ????????????(desc), exercisePostService check")
	void myPostsViewTestDescCheckPostService() {
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
	@DisplayName("???????????? ????????? ????????? ????????? ?????? ?????? ?????? -> ??????, ????????????(desc), ModelAndView check")
	void myPostsViewTestDescCheckMav() {
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
