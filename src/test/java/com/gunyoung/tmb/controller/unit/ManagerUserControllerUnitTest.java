package com.gunyoung.tmb.controller.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.ManagerUserController;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.AccessDeniedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;
import com.gunyoung.tmb.security.AuthorityService;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.testutil.UserTest;

/**
 * {@link ManagerUserController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerUserController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ManagerUserControllerUnitTest {
	
	@Mock
	UserService userService;
	
	@Mock
	CommentService commentService;
	
	@Mock
	ExercisePostService exercisePostService;
	
	@Mock
	AuthorityService authorityService;
	
	@InjectMocks
	ManagerUserController managerUserController;
	
	private Integer defaultPageNum = 1;
	
	private ModelAndView mav;
	private ModelAndPageView mapv;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
		mapv = mock(ModelAndPageView.class);
	}
	
	/*
	 * ModelAndView userManageView(@RequestParam(value="page", required=false,defaultValue="1") Integer page,
	 *		@RequestParam(value="keyword",required=false) String keyword, ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("매니저들의 유저 검색 (for managing) 페이지반환 -> 정상, 키워드 없음, totalPageNum check")
	void userManageViewNoKeywordCheckTotalPageNum() {
		//Given
		
		//When
		managerUserController.userManageView(defaultPageNum, null, mapv);
		
		//Then
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, Long.valueOf(1));
	}
	
	@Test
	@DisplayName("매니저들의 유저 검색 (for managing) 페이지반환 -> 정상, 키워드 있음, userService check")
	void userManageViewYesKeywordCheckUserService() {
		//Given
		String keyword = "keyword";
		Page<User> pageResult = new PageImpl<>(new ArrayList<>());
		given(userService.findAllByNickNameOrNameInPage(keyword, defaultPageNum)).willReturn(pageResult);
		
		//When
		managerUserController.userManageView(defaultPageNum, keyword, mapv);
		
		//Then
		then(userService).should(times(1)).countAllByNickNameOrName(keyword);
	}
	
	@Test
	@DisplayName("매니저들의 유저 검색 (for managing) 페이지반환 -> 정상, 키워드 없음, ModelAndPageView Check")
	void userManageViewNoKeywordCheckMav() {
		//Given
		String keyword = "keyword";
		Page<User> pageResult = new PageImpl<>(new ArrayList<>());
		given(userService.findAllByNickNameOrNameInPage(keyword, defaultPageNum)).willReturn(pageResult);
		
		Long givenUserNum = Long.valueOf(63);
		given(userService.countAllByNickNameOrName(keyword)).willReturn(givenUserNum);
		
		//When
		managerUserController.userManageView(defaultPageNum, keyword, mapv);
		
		//Then
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenUserNum/ ManagerUserController.USER_MANAGE_VIEW_PAGE_SIZE + 1);
		then(mapv).should(times(1)).setViewName("userManage");
	}
	
	/*
	 * ModelAndView manageUserProfileView(@PathVariable("user_id") Long userId, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("특정 User 관리 화면 반환 -> 해당 Id의 User 없으면")
	void manageUserProfileViewTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(52);
		given(userService.findById(nonExistUserId)).willReturn(null);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			managerUserController.manageUserProfileView(nonExistUserId, mav);
		});
	}
	
	@Test
	@DisplayName("특정 User 관리 화면 반환 -> 접속자의 권한이 대상 User의 권한보다 낮다면")
	void manageUserProfileViewTestAccessDenied() {
		//Given
		Long userId = Long.valueOf(36);
		User user = stubbingUserServiceFindById(userId);
		
		given(authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(user)).willReturn(false);
		
		//When, Then
		assertThrows(AccessDeniedException.class, () -> {
			managerUserController.manageUserProfileView(userId, mav);
		});
	}
	
	@Test
	@DisplayName("특정 User 관리 화면 반환 -> 정상, ModelAndView check")
	void manageUserProfileViewTestCheckMav() {
		//Given
		Long userId = Long.valueOf(36);
		User user = stubbingUserServiceFindById(userId);
		
		given(authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(user)).willReturn(true);
		List<String> myReachableAuthStringList = Collections.emptyList();
		given(authorityService.getReachableAuthorityStrings(any())).willReturn(myReachableAuthStringList);
		
		//When
		managerUserController.manageUserProfileView(userId, mav);
		
		//Then
		then(mav).should(times(1)).addObject("userId", userId);
		then(mav).should(times(1)).addObject("userInfo", user);
		then(mav).should(times(1)).addObject("roleList", myReachableAuthStringList);
	}
	
	/*
	 * ModelAndView manageUserComments(@PathVariable("user_id") Long userId,@RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order , ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("특정 유저의 댓글 목록 보여주는 화면 반환 -> 해당 Id의 User 없으면")
	void manageUserCommentsTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(76);
		given(userService.findById(nonExistUserId)).willReturn(null);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			managerUserController.manageUserComments(nonExistUserId, defaultPageNum, null, mapv);
		});
	}
	
	@Test
	@DisplayName("특정 유저의 댓글 목록 보여주는 화면 반환 -> 검색 결과 정렬 방식이 올바르지 못하다면")
	void manageUserCommentsTestInvalidSort() {
		//Given
		Long userId = Long.valueOf(33);
		stubbingUserServiceFindById(userId);
		
		String inValidOrder = "invalid";
		
		//When, Then
		assertThrows(SearchCriteriaInvalidException.class, () -> {
			managerUserController.manageUserComments(userId, defaultPageNum, inValidOrder, mapv);
		});
	}
	
	@Test
	@DisplayName("특정 유저의 댓글 목록 보여주는 화면 반환 -> 정상, 정렬 오름차순(asc), commentService check")
	void manageUserCommentsTestASCCheckCommentService() {
		//Given
		Long userId = Long.valueOf(33);
		stubbingUserServiceFindById(userId);
		
		Page<Comment> pageResult = new PageImpl<>(new ArrayList<>());
		given(commentService.findAllByUserIdOrderByCreatedAtAsc(userId, defaultPageNum, ManagerUserController.USER_COMMENT_LIST_VIEW_FOR_MANAGER_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "asc";
		
		//When
		managerUserController.manageUserComments(userId, defaultPageNum, order, mapv);
		
		//Then
		then(commentService).should(times(1)).countByUserId(userId);
	}
	
	@Test
	@DisplayName("특정 유저의 댓글 목록 보여주는 화면 반환 -> 정상, 정렬 내림차순(desc), commentService check")
	void manageUserCommentsTestDescCheckCommentService() {
		//Given
		Long userId = Long.valueOf(33);
		stubbingUserServiceFindById(userId);
		
		Page<Comment> pageResult = new PageImpl<>(new ArrayList<>());
		given(commentService.findAllByUserIdOrderByCreatedAtDesc(userId, defaultPageNum, ManagerUserController.USER_COMMENT_LIST_VIEW_FOR_MANAGER_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "desc";
		
		//When
		managerUserController.manageUserComments(userId, defaultPageNum, order, mapv);
		
		//Then
		then(commentService).should(times(1)).countByUserId(userId);
	}
	
	@Test
	@DisplayName("특정 유저의 댓글 목록 보여주는 화면 반환 -> 정상, 정렬 내림차순(desc), ModelAndView check")
	void manageUserCommentsDescCheckMav() {
		//Given
		Long userId = Long.valueOf(33);
		User user = stubbingUserServiceFindById(userId);
		
		Page<Comment> pageResult = new PageImpl<>(new ArrayList<>());
		given(commentService.findAllByUserIdOrderByCreatedAtDesc(userId, defaultPageNum, ManagerUserController.USER_COMMENT_LIST_VIEW_FOR_MANAGER_PAGE_SIZE)).willReturn(pageResult);
		
		Long givenCommentNum = Long.valueOf(63);
		given(commentService.countByUserId(userId)).willReturn(givenCommentNum);
		
		String order = "desc";
		
		//When
		managerUserController.manageUserComments(userId, defaultPageNum, order, mapv);
		
		//Then
		verifyMapvForManageUserCommentsDescCheckMav(userId, user, givenCommentNum);
	}
	
	private void verifyMapvForManageUserCommentsDescCheckMav(Long userId, User user, Long givenCommentNum) {
		then(mapv).should(times(1)).addObject("userId", userId);
		then(mapv).should(times(1)).addObject("username", user.getFullName()+ " : " + user.getNickName());
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenCommentNum / ManagerUserController.USER_COMMENT_LIST_VIEW_FOR_MANAGER_PAGE_SIZE + 1);
		then(mapv).should(times(1)).setViewName("userCommentList");
	}
	
	/*
	 * ModelAndView manageUserPosts(@PathVariable("user_id") Long userId,@RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order , ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("특정 유저의 게시글 목록 보여주는 화면 반환 -> 해당 Id의 User 없으면")
	void manageUserPostsTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(76);
		given(userService.findById(nonExistUserId)).willReturn(null);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			managerUserController.managerUserPosts(nonExistUserId, defaultPageNum, null, mapv);
		});
	}
	
	@Test
	@DisplayName("특정 유저의 게시글 목록 보여주는 화면 반환 -> 검색 결과 정렬 방식이 올바르지 못하다면")
	void manageUserPostsTestInvalidSort() {
		//Given
		Long userId = Long.valueOf(33);
		stubbingUserServiceFindById(userId);
		
		String inValidOrder = "invalid";
		
		//When, Then
		assertThrows(SearchCriteriaInvalidException.class, () -> {
			managerUserController.managerUserPosts(userId, defaultPageNum, inValidOrder, mapv);
		});
	}
	
	@Test
	@DisplayName("특정 유저의 게시글 목록 보여주는 화면 반환 -> 정상, 정렬 오름차순(asc), exercisePostService check")
	void manageUserPostsTestASCCheckPostService() {
		//Given
		Long userId = Long.valueOf(33);
		stubbingUserServiceFindById(userId);
		
		Page<ExercisePost> pageResult = new PageImpl<>(new ArrayList<>());
		given(exercisePostService.findAllByUserIdOrderByCreatedAtAsc(userId, defaultPageNum, ManagerUserController.USER_POST_LIST_VIEW_FOR_MANAGER_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "asc";
		
		//When
		managerUserController.managerUserPosts(userId, defaultPageNum, order, mapv);
		
		//Then
		then(exercisePostService).should(times(1)).countWithUserId(userId);
	}
	
	@Test
	@DisplayName("특정 유저의 게시글 목록 보여주는 화면 반환 -> 정상, 정렬 내림차순(desc), exercisePostService check")
	void manageUserPostsTestDescCheckPostService() {
		//Given
		Long userId = Long.valueOf(33);
		stubbingUserServiceFindById(userId);
		
		Page<ExercisePost> pageResult = new PageImpl<>(new ArrayList<>());
		given(exercisePostService.findAllByUserIdOrderByCreatedAtDesc(userId, defaultPageNum, ManagerUserController.USER_POST_LIST_VIEW_FOR_MANAGER_PAGE_SIZE)).willReturn(pageResult);
		
		String order = "desc";
		
		//When
		managerUserController.managerUserPosts(userId, defaultPageNum, order, mapv);
		
		//Then
		then(exercisePostService).should(times(1)).countWithUserId(userId);
	}
	
	@Test
	@DisplayName("특정 유저의 게시글 목록 보여주는 화면 반환 -> 정상, 정렬 내림차순(desc), ModelAndView check")
	void manageUserPostsDescCheckMav() {
		//Given
		Long userId = Long.valueOf(33);
		User user = stubbingUserServiceFindById(userId);
		
		Page<ExercisePost> pageResult = new PageImpl<>(new ArrayList<>());
		given(exercisePostService.findAllByUserIdOrderByCreatedAtDesc(userId, defaultPageNum, ManagerUserController.USER_POST_LIST_VIEW_FOR_MANAGER_PAGE_SIZE)).willReturn(pageResult);
		
		Long givenExercisePostNum = Long.valueOf(63);
		given(exercisePostService.countWithUserId(userId)).willReturn(givenExercisePostNum);
		
		String order = "desc";
		
		//When
		managerUserController.managerUserPosts(userId, defaultPageNum, order, mapv);
		
		//Then
		verifyMapvForManageUserPostsDescCheckMav(userId, user, givenExercisePostNum);
	}
	
	private void verifyMapvForManageUserPostsDescCheckMav(Long userId, User user, Long givenPostNum) {
		then(mapv).should(times(1)).addObject("userId", userId);
		then(mapv).should(times(1)).addObject("username", user.getFullName()+": " +user.getNickName());
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenPostNum / ManagerUserController.USER_POST_LIST_VIEW_FOR_MANAGER_PAGE_SIZE + 1);
		then(mapv).should(times(1)).setViewName("userPostList");
	}
	
	private User stubbingUserServiceFindById(Long userId) {
		User user = UserTest.getUserInstance();
		given(userService.findById(userId)).willReturn(user);
		return user;
	}
}

