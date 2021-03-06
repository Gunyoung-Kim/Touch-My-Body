package com.gunyoung.tmb.controller.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.ExercisePostController;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.SaveCommentDTO;
import com.gunyoung.tmb.dto.reqeust.SaveExercisePostDTO;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.ExercisePostErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExercisePostNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.testutil.CommentTest;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link ExercisePostController} ??? ?????? ????????? ????????? <br>
 * ????????? ??????: (?????? ?????????) ExercisePostController only
 * {@link org.mockito.BDDMockito}??? ????????? ???????????? ????????? ?????? ?????? ?????????
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ExercisePostControllerUnitTest {
	
	@Mock
	ExercisePostService exercisePostService;
	
	@Mock
	CommentService commentService;
	
	@Mock
	UserService userService;
	
	@Mock
	ExerciseService exerciseService;
	
	@Mock
	HttpSession session;
	
	@InjectMocks
	ExercisePostController exercisePostController;
	
	private ModelAndView mav;
	private ModelAndPageView mapv;
	
	private Integer defaultPageNum = 1;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
		mapv = mock(ModelAndPageView.class);
	}
	
	/*
	 * ModelAndView exercisePostView(@RequestParam(value="page", required = false,defaultValue="1") Integer page,
	 *		@RequestParam(value="keyword",required=false)String keyword, ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("???????????? ?????? ?????? ?????? -> ????????? ??????, exercisePost check")
	void exercisePostViewTestNoKeyword() {
		//Given
		
		//When
		exercisePostController.exercisePostView(defaultPageNum, null, mapv);
		
		//Then
		then(exercisePostService).should(times(1)).findAllForPostForCommunityViewDTOOderByCreatedAtDESCByPage(defaultPageNum, ExercisePostController.COMMUNITY_VIEW_PAGE_SIZE);
		then(exercisePostService).should(times(1)).count();
	}
	
	@Test
	@DisplayName("???????????? ?????? ?????? ?????? -> ????????? ??????, exercisePost check")
	void exercisePostViewTestWithKeyword() {
		//Given
		String keyword = "keyword";
		
		//When
		exercisePostController.exercisePostView(defaultPageNum, keyword, mapv);
		
		//Then
		then(exercisePostService).should(times(1)).findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, defaultPageNum, ExercisePostController.COMMUNITY_VIEW_PAGE_SIZE);
		then(exercisePostService).should(times(1)).countWithTitleAndContentsKeyword(keyword);
	}
	
	@Test
	@DisplayName("???????????? ?????? ?????? ?????? -> ??????, ModelAndPageView check")
	void exercisePostViewTestCheckMapv() {
		//Given
		Page<PostForCommunityViewDTO> pageResult = new PageImpl<>(new ArrayList<>());
		given(exercisePostService.findAllForPostForCommunityViewDTOOderByCreatedAtDESCByPage(defaultPageNum, ExercisePostController.COMMUNITY_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		Long givenExercisPostNum = Long.valueOf(10);
		given(exercisePostService.count()).willReturn(givenExercisPostNum);
		
		//When
		exercisePostController.exercisePostView(defaultPageNum, null, mapv);
		
		//Then
		verifyModelAndPageViewInExercisePostViewTestCheckMapv(pageResult, givenExercisPostNum);
	}
	
	private void verifyModelAndPageViewInExercisePostViewTestCheckMapv(Page<PostForCommunityViewDTO> pageResult, Long givenExercisPostNum ) {
		then(mapv).should(times(1)).addObject("listObject", pageResult);
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenExercisPostNum/ExercisePostController.COMMUNITY_VIEW_PAGE_SIZE + 1);
		then(mapv).should(times(1)).addObject("category", "??????");
		then(mapv).should(times(1)).setViewName("community");
	}
	
	/*
	 * ModelAndView exercisePostViewWithTarget(@RequestParam(value="page", required = false,defaultValue="1") Integer page
	 *		, @RequestParam(value="keyword",required=false)String keyword, ModelAndPageView mav, @PathVariable("target") String targetName)
	 */
	
	@Test
	@DisplayName("?????? ?????? ???????????? ?????? -> ?????? ??????????????? ???????????? ?????? ???")
	void exercisePostViewWithTargetTestTargetTypeNotFounded() {
		//Given
		String nonExistTargetTypeString = "nonExist";
		
		//When, Then
		assertThrows(TargetTypeNotFoundedException.class, () -> {
			exercisePostController.exercisePostViewWithTarget(defaultPageNum, null, mapv, nonExistTargetTypeString);
		});
	}
	
	@Test
	@DisplayName("?????? ?????? ???????????? ?????? -> ????????? ??????, exercisePostService check")
	void exercisePostViewWithTargetTestNoKeywordCheckExercisePostService() {
		//Given
		TargetType target = TargetType.ARM;
		String targetName = target.toString();
		
		//When
		exercisePostController.exercisePostViewWithTarget(defaultPageNum, null, mapv, targetName);
		
		//Then
		then(exercisePostService).should(times(1)).findAllForPostForCommunityViewDTOWithTargetByPage(target, defaultPageNum, ExercisePostController.COMMUNITY_VIEW_PAGE_SIZE);
		then(exercisePostService).should(times(1)).countWithTarget(target);
	}
	
	@Test
	@DisplayName("?????? ?????? ???????????? ?????? -> ????????? ??????, exercisePostService check")
	void exercisePostViewWithTargetTestYesKeywordCheckExercisePostService() {
		//Given
		TargetType target = TargetType.ARM;
		String targetName = target.toString();
		
		String keyword = "keyword";
		
		//When
		exercisePostController.exercisePostViewWithTarget(defaultPageNum, keyword, mapv, targetName);
		
		//Then
		then(exercisePostService).should(times(1)).findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(target, keyword, defaultPageNum,  ExercisePostController.COMMUNITY_VIEW_PAGE_SIZE);
		then(exercisePostService).should(times(1)).countWithTargetAndKeyword(target, keyword);
	}
	
	@Test
	@DisplayName("?????? ?????? ???????????? ?????? -> ??????, ModelAndPageView check")
	void exercisePostViewWithTargetTestCheckMapv() {
		//Given
		TargetType target = TargetType.ARM;
		String targetName = target.toString();
		
		Page<PostForCommunityViewDTO> pageResult = new PageImpl<>(new ArrayList<>());
		given(exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(target, defaultPageNum, ExercisePostController.COMMUNITY_VIEW_PAGE_SIZE)).willReturn(pageResult);
		
		Long givenExercisePostWithTargetNum = Long.valueOf(40);
		given(exercisePostService.countWithTarget(target)).willReturn(givenExercisePostWithTargetNum);
		
		//When
		exercisePostController.exercisePostViewWithTarget(defaultPageNum, null, mapv, targetName);
		
		//Then
		verifyModelAndPageViewInexercisePostViewWithTargetTestCheckMapv(pageResult, givenExercisePostWithTargetNum, target);
	}
	
	private void verifyModelAndPageViewInexercisePostViewWithTargetTestCheckMapv(Page<PostForCommunityViewDTO> pageResult, Long givenExercisePostWithTargetNum, TargetType target) {
		then(mapv).should(times(1)).addObject("listObject", pageResult);
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenExercisePostWithTargetNum/ ExercisePostController.COMMUNITY_VIEW_PAGE_SIZE + 1);
		then(mapv).should(times(1)).addObject("category", target.getKoreanName());
		then(mapv).should(times(1)).setViewName("community");
	}
	
	/*
	 * ModelAndView exercisePostDetailView(@PathVariable("post_id") Long postId, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("???????????? ???????????? ??? ?????? -> ?????? ID??? ExercisePost ?????????")
	void exercisePostDetailViewExercisePostNonExist() {
		//Given
		Long nonExistExercisePostId = Long.valueOf(24);
		given(exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(nonExistExercisePostId)).willThrow(new ExercisePostNotFoundedException(ExercisePostErrorCode.EXERCISE_POST_NOT_FOUNDED_ERROR.getDescription()));
		
		//When,Then
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostController.exercisePostDetailView(nonExistExercisePostId, mav);
		});
	}
	
	@Test
	@DisplayName("???????????? ???????????? ??? ?????? -> ??????, CommentService check")
	void exercisePostDetailViewCheckCommentService() {
		//Given
		Long exercisePostId = Long.valueOf(57);
		stubbingExercisePostServiceGetExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(exercisePostId);
		
		//When
		exercisePostController.exercisePostDetailView(exercisePostId, mav);
		
		//Then
		then(commentService).should(times(1)).getCommentForPostViewDTOsByExercisePostId(exercisePostId);
	}
	
	@Test
	@DisplayName("???????????? ???????????? ??? ?????? -> ??????, ModelAndView check")
	void exercisePostDetailViewCheckModelAndView() {
		//Given
		Long exercisePostId = Long.valueOf(57);
		ExercisePostViewDTO exercisePostViewDTO = stubbingExercisePostServiceGetExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(exercisePostId);
		
		List<CommentForPostViewDTO> commentsInPost = new ArrayList<>();
		given(commentService.getCommentForPostViewDTOsByExercisePostId(exercisePostId)).willReturn(commentsInPost);
		
		//When
		exercisePostController.exercisePostDetailView(exercisePostId, mav);
		
		//Then
		verifyModelAndViewInexercisePostDetailViewCheckModelAndView(exercisePostViewDTO ,commentsInPost);
	}
	
	private void verifyModelAndViewInexercisePostDetailViewCheckModelAndView(ExercisePostViewDTO exercisePostViewDTO , List<CommentForPostViewDTO> commentsInPost) {
		then(mav).should(times(1)).addObject("exercisePost", exercisePostViewDTO);
		then(mav).should(times(1)).addObject("comments", commentsInPost);
		then(mav).should(times(1)).setViewName("postView");
	}
	
	private ExercisePostViewDTO stubbingExercisePostServiceGetExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(Long exercisePostId) {
		ExercisePostViewDTO exercisePostViewDTO = ExercisePostTest.getExercisePostViewDTOInstance();
		given(exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(exercisePostId)).willReturn(exercisePostViewDTO);
		return exercisePostViewDTO;
	}
	
	/*
	 * ModelAndView addExercisePostView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("????????? ???????????? ?????? ?????? -> ??????, ModelAndView check")
	void addExercisePostViewTestCheckModelAndView() {
		//Given
		
		//When
		exercisePostController.addExercisePostView(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("addExercisePost");
	}
	
	/*
	 * ModelAndView addExercisePost(@ModelAttribute SaveExercisePostDTO dto, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("????????? ?????? ?????? -> ????????? ????????? ID??? ???????????? User ?????????")
	void addExercisePostTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(95);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findWithExercisePostsById(nonExistUserId)).willThrow(new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription()));
		
		String exerciseName = "exercise";
		SaveExercisePostDTO saveExercisePostDTO = ExercisePostTest.getSaveExercisePostDTOInstance(exerciseName);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			exercisePostController.addExercisePost(saveExercisePostDTO, mav);
		});
	}
	
	@Test
	@DisplayName("????????? ?????? ?????? -> exerciseName??? ???????????? Exercise ?????????")
	void addExercisePostTestExerciseNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(59);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindWithExercisePostsById(loginIdInSession);
		
		String nonExistExerciseName = "nonexist";
		SaveExercisePostDTO saveExercisePostDTO = ExercisePostTest.getSaveExercisePostDTOInstance(nonExistExerciseName);
		given(exerciseService.findWithExercisePostsByName(nonExistExerciseName)).willThrow(new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_NAME_NOT_FOUNDED_ERROR.getDescription()));
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exercisePostController.addExercisePost(saveExercisePostDTO, mav);
		});
	}
	
	@Test
	@DisplayName("????????? ?????? ?????? -> ??????, exercisePostService check")
	void addExercisePostTestCheckExercisePostService() {
		//Given
		Long loginIdInSession = Long.valueOf(59);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		stubbingUserServiceFindWithExercisePostsById(loginIdInSession);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		SaveExercisePostDTO saveExercisePostDTO = ExercisePostTest.getSaveExercisePostDTOInstance(exercise.getName());
		given(exerciseService.findWithExercisePostsByName(exercise.getName())).willReturn(exercise);
		
		//When
		exercisePostController.addExercisePost(saveExercisePostDTO, mav);
		
		//Then
		then(exercisePostService).should(times(1)).saveWithUserAndExercise(any(ExercisePost.class), any(User.class), any(Exercise.class));
	}
	
	private User stubbingUserServiceFindWithExercisePostsById(Long loginIdInSession) {
		User user = UserTest.getUserInstance();
		given(userService.findWithExercisePostsById(loginIdInSession)).willReturn(user);
		return user;
	}
	
	/*
	 * ModelAndView addCommentToExercisePost(@PathVariable("post_id") Long postId,@ModelAttribute SaveCommentDTO dto,
	 *		@RequestParam("isAnonymous") boolean isAnonymous, HttpServletRequest request)
	 */
	
	private SaveCommentDTO saveCommentDTO = CommentTest.getSaveCommentDTOInstance();
	
	@Test
	@DisplayName("????????? ???????????? ?????? ???????????? ?????? -> ????????? ????????? ID??? ???????????? User ?????????")
	void addCommentToExercisePostUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(95);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findWithCommentsById(nonExistUserId)).willThrow(new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription()));
		
		Long exercisePostId = Long.valueOf(1);
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			exercisePostController.addCommentToExercisePost(exercisePostId, saveCommentDTO, false, request);
		});
	}
	
	@Test
	@DisplayName("????????? ???????????? ?????? ???????????? ?????? -> ?????? ID??? ExercisePost ?????????")
	void addCommentToExercisePostExercisePostNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(24);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		stubbingUserServiceFindWithCommentsById(loginIdInSession);
		
		Long nonExistExercisePostId = Long.valueOf(96);
		given(exercisePostService.findWithCommentsById(nonExistExercisePostId)).willThrow(new ExercisePostNotFoundedException(ExercisePostErrorCode.EXERCISE_POST_NOT_FOUNDED_ERROR.getDescription()));
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		//When, Then
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostController.addCommentToExercisePost(nonExistExercisePostId, saveCommentDTO, false, request);
		});
	}
	
	@Test
	@DisplayName("????????? ???????????? ?????? ???????????? ?????? -> ??????, comment CommentService Test")
	void addCommentToExercisePostTestCheckCommentService() {
		//Given
		Long loginIdInSession = Long.valueOf(24);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		stubbingUserServiceFindWithCommentsById(loginIdInSession);
		
		Long exercisePostId = Long.valueOf(1);
		stubbingExercisePostServiceFindWithCommentsById(exercisePostId);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		stubbingHttpServletRequestGetRemoteHost(request);
		
		boolean isAnonymous = true;
		
		//When
		exercisePostController.addCommentToExercisePost(exercisePostId, saveCommentDTO, isAnonymous, request);
		
		//Then
		then(commentService).should(times(1)).saveWithUserAndExercisePost(any(Comment.class), any(User.class), any(ExercisePost.class));
	}
	
	private User stubbingUserServiceFindWithCommentsById(Long loginIdInSession) {
		User user = UserTest.getUserInstance();
		given(userService.findWithCommentsById(loginIdInSession)).willReturn(user);
		return user;
	}

	private ExercisePost stubbingExercisePostServiceFindWithCommentsById(Long exercisePostId) {
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		given(exercisePostService.findWithCommentsById(exercisePostId)).willReturn(exercisePost);
		return exercisePost;
	}
	
	private String stubbingHttpServletRequestGetRemoteHost(HttpServletRequest request) {
		String remoteHost = "127.0.0.1";
		given(request.getHeader("X-Real-IP")).willReturn(remoteHost);
		return remoteHost;
	}
}
