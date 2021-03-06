package com.gunyoung.tmb.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.controller.rest.ExercisePostRestController;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.error.codes.CommentErrorCode;
import com.gunyoung.tmb.error.codes.ExercisePostErrorCode;
import com.gunyoung.tmb.error.codes.LikeErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.LikeAlreadyExistException;
import com.gunyoung.tmb.error.exceptions.nonexist.CommentNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExercisePostNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.LikeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.like.CommentLikeService;
import com.gunyoung.tmb.services.domain.like.PostLikeService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.testutil.CommentLikeTest;
import com.gunyoung.tmb.testutil.CommentTest;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.PostLikeTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link ExercisePostRestController} ??? ?????? ????????? ????????? <br>
 * ????????? ??????: (?????? ?????????) ExercisePostRestController only
 * {@link org.mockito.BDDMockito}??? ????????? ???????????? ????????? ?????? ?????? ?????????
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ExercisePostRestControllerUnitTest {
	
	@Mock
	HttpSession session;
	
	@Mock
	UserService userService;
	
	@Mock
	ExercisePostService exercisePostService;
	
	@Mock
	PostLikeService postLikeService;
	
	@Mock
	CommentService commentService;
	
	@Mock
	CommentLikeService commentLikeService;
	
	@InjectMocks
	ExercisePostRestController exercisePostRestController;
	
	/*
	 * void addLikeToExercisePost(@PathVariable("postId") Long postId)
	 */
	
	@Test
	@DisplayName("????????? ???????????? ????????? ??????????????? ?????? -> ????????? ????????? Id??? ???????????? User ??????")
	void addLikeToExercisePostNonExistUser() {
		//Given
		Long nonExistUserId = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(nonExistUserId);
		
		given(userService.findWithPostLikesById(nonExistUserId)).willThrow(new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription()));
		
		Long exercisePostId = Long.valueOf(1);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			exercisePostRestController.addLikeToExercisePost(exercisePostId);
		});
	}
	
	@Test
	@DisplayName("????????? ???????????? ????????? ??????????????? ?????? -> ?????? Id??? ExercisePost ?????????")
	void addLikeToExercisePostNonExistExercisePost() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		stubbingUserServiceFindWithPostLikesById(loginIdInSession);
		
		Long nonExistExercisePostId = Long.valueOf(1);
		given(exercisePostService.findWithPostLikesById(nonExistExercisePostId)).willThrow(new ExercisePostNotFoundedException(ExercisePostErrorCode.EXERCISE_POST_NOT_FOUNDED_ERROR.getDescription()));
		
		//When, Then
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostRestController.addLikeToExercisePost(nonExistExercisePostId);
		});
	}
	
	@Test
	@DisplayName("????????? ???????????? ????????? ??????????????? ?????? -> ?????? ExercisePost??? User??? PostLike??? ?????? ??????")
	void addLikeToExercisePostAlredyExist() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		stubbingUserServiceFindWithPostLikesById(loginIdInSession);
		
		Long exercisePostId = Long.valueOf(1);
		stubbingExercisePostServiceFindWithPostLikesById(exercisePostId);
		
		given(postLikeService.existsByUserIdAndExercisePostId(loginIdInSession, exercisePostId)).willReturn(true);
		
		//When, Then
		assertThrows(LikeAlreadyExistException.class, () -> {
			exercisePostRestController.addLikeToExercisePost(exercisePostId);
		});
	}
	
	@Test
	@DisplayName("????????? ???????????? ????????? ??????????????? ?????? -> ??????")
	void addLikeToExercisePostTest() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		User user = stubbingUserServiceFindWithPostLikesById(loginIdInSession);
		
		Long exercisePostId = Long.valueOf(1);
		ExercisePost exercisePost = stubbingExercisePostServiceFindWithPostLikesById(exercisePostId);
		
		given(postLikeService.existsByUserIdAndExercisePostId(loginIdInSession, exercisePostId)).willReturn(false);
	
		//When
		exercisePostRestController.addLikeToExercisePost(exercisePostId);
		
		//Then
		then(postLikeService).should(times(1)).createAndSaveWithUserAndExercisePost(user, exercisePost);
	}
	
	private User stubbingUserServiceFindWithPostLikesById(Long loginIdInSession) {
		User user = UserTest.getUserInstance();
		given(userService.findWithPostLikesById(loginIdInSession)).willReturn(user);
		return user;
	}
	
	private ExercisePost stubbingExercisePostServiceFindWithPostLikesById(Long exercisePostId) {
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		given(exercisePostService.findWithPostLikesById(exercisePostId)).willReturn(exercisePost);
		return exercisePost;
	}
	
	/*
	 * void removeLikeToExercisePost(@PathVariable("postId") Long postId) 
	 */
	
	@Test
	@DisplayName("????????? ???????????? ????????? ??????????????? ?????? -> ????????? ????????? UserId??? postId??? ???????????? PostLike ??????")
	void removeLikeToExercisePostPostLikeNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		
		Long exercisePostId = Long.valueOf(1);
		given(postLikeService.findByUserIdAndExercisePostId(loginIdInSession, exercisePostId)).willThrow(new LikeNotFoundedException(LikeErrorCode.LIKE_NOT_FOUNDED_ERROR.getDescription()));
		
		//When,Then
		assertThrows(LikeNotFoundedException.class, () -> {
			exercisePostRestController.removeLikeToExercisePost(exercisePostId);
		});
	}
	
	@Test
	@DisplayName("????????? ???????????? ????????? ??????????????? ?????? -> ??????")
	void removeLikeToExercisePostTest() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		
		Long exercisePostId = Long.valueOf(1);
		PostLike postLike = PostLikeTest.getPostLikeInstance();
		given(postLikeService.findByUserIdAndExercisePostId(loginIdInSession, exercisePostId)).willReturn(postLike);
		
		//When
		exercisePostRestController.removeLikeToExercisePost(exercisePostId);
		
		//Then
		then(postLikeService).should(times(1)).delete(postLike);
	}
	
	/*
	 * void addLikeToComment(@PathVariable("postId") Long postId, @RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@DisplayName("????????? ????????? ????????? ?????? ?????? ?????? -> ????????? ????????? Id??? User ??????")
	void addLikeToCommentUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(nonExistUserId);
		
		given(userService.findWithCommentLikesById(nonExistUserId)).willThrow(new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription()));
		
		Long exercisePostId = Long.valueOf(1);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			exercisePostRestController.addLikeToComment(exercisePostId, nonExistUserId);
		});
	}
	
	@Test
	@DisplayName("????????? ????????? ????????? ?????? ?????? ?????? -> ?????? Id??? Comment ?????????")
	void addLikeToCommentCommentNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		stubbingUserServiceFindWithCommentLikesById(loginIdInSession);
		
		Long nonExistCommentId = Long.valueOf(1);
		given(commentService.findWithCommentLikesById(nonExistCommentId)).willThrow(new CommentNotFoundedException(CommentErrorCode.COMMENT_NOT_FOUNDED_ERROR.getDescription()));
		
		Long exercisePostId = Long.valueOf(1);
		//When, Then
		assertThrows(CommentNotFoundedException.class, () -> {
			exercisePostRestController.addLikeToComment(exercisePostId, nonExistCommentId);
		});
	}
	
	@Test
	@DisplayName("????????? ????????? ????????? ?????? ?????? ?????? -> ?????? ????????? ????????? ?????? ????????? ???????????????")
	void addLikeToCommentAlreadyExist() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		stubbingUserServiceFindWithCommentLikesById(loginIdInSession);
		
		Long commentId = Long.valueOf(1);
		stubbingCommentServicefindWithCommentLikesById(commentId);
		
		given(commentLikeService.existsByUserIdAndCommentId(loginIdInSession, commentId)).willReturn(true);
		
		Long exercisePostId = Long.valueOf(1);
		//When, Then
		assertThrows(LikeAlreadyExistException.class, () -> {
			exercisePostRestController.addLikeToComment(exercisePostId, commentId);
		});
	}
	
	@Test
	@DisplayName("????????? ????????? ????????? ?????? ?????? ?????? -> ??????")
	void addLikeToCommentTest() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		User user = stubbingUserServiceFindWithCommentLikesById(loginIdInSession);
		
		Long commentId = Long.valueOf(1);
		Comment comment = stubbingCommentServicefindWithCommentLikesById(commentId);
		
		given(commentLikeService.existsByUserIdAndCommentId(loginIdInSession, commentId)).willReturn(false);
		
		//When
		exercisePostRestController.addLikeToComment(loginIdInSession, commentId);
		
		//Then
		then(commentLikeService).should(times(1)).createAndSaveWithUserAndComment(user, comment);
	}
	
	private User stubbingUserServiceFindWithCommentLikesById(Long loginIdInSession) {
		User user = UserTest.getUserInstance();
		given(userService.findWithCommentLikesById(loginIdInSession)).willReturn(user);
		return user;
	}
	
	private Comment stubbingCommentServicefindWithCommentLikesById(Long commentId) {
		Comment comment = CommentTest.getCommentInstance();
		given(commentService.findWithCommentLikesById(commentId)).willReturn(comment);
		return comment;
	}
	
	/*
	 * void removeLikeToComment(@PathVariable("postId") Long postId, @RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@DisplayName("????????? ????????? ????????? ?????? ?????? ?????? -> ?????? ????????? ?????? ????????? ????????? ???????????? ???????????????")
	void removeLikeToCommentCommentLikeNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		
		Long commentId = Long.valueOf(1);
		given(commentLikeService.findByUserIdAndCommentId(loginIdInSession, commentId)).willThrow(new LikeNotFoundedException(LikeErrorCode.LIKE_NOT_FOUNDED_ERROR.getDescription()));
		
		Long exercisePostId = Long.valueOf(1);
		//When, Then
		assertThrows(LikeNotFoundedException.class, () -> {
			exercisePostRestController.removeLikeToComment(exercisePostId, commentId);
		});
	}
	
	@Test
	@DisplayName("????????? ????????? ????????? ?????? ?????? ?????? -> ??????")
	void removeLikeToCommentTest() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		
		Long commentId = Long.valueOf(1);
		CommentLike commentLike = CommentLikeTest.getCommentLikeInstance();
		given(commentLikeService.findByUserIdAndCommentId(loginIdInSession, commentId)).willReturn(commentLike);
		
		Long exercisePostId = Long.valueOf(1);
		
		//When
		exercisePostRestController.removeLikeToComment(exercisePostId, commentId);
		
		//Then
		then(commentLikeService).should(times(1)).delete(commentLike);
	}
	
	/*
	 * void removeCommentToExercisePost(@PathVariable("postId") Long postId,@RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@DisplayName("????????? ???????????? ?????? ???????????? ?????? -> ??????")
	void removeCommentToExercisePostTest() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		
		Long exercisePostId = Long.valueOf(1);
		Long commentId = Long.valueOf(1);
		
		//When
		exercisePostRestController.removeCommentToExercisePost(exercisePostId, commentId);
		
		//Then
		then(commentService).should(times(1)).checkIsMineAndDelete(loginIdInSession, commentId);
	}
	
	private void stubbingSessionByLoginIdInSession(Long loginIdInSession) {
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
	}
}
