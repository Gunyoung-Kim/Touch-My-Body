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
 * {@link ExercisePostRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExercisePostRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ExercisePostRestControllerUnitTest {
	
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
	 * public void addLikeToExercisePost(@PathVariable("postId") Long postId)
	 */
	
	@Test
	@DisplayName("유저가 게시글에 좋아요 추가했을때 처리 -> 세션에 저장된 Id에 해당하는 User 없음")
	public void addLikeToExercisePostNonExistUser() {
		//Given
		Long nonExistUserId = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(nonExistUserId);
		
		given(userService.findWithPostLikesById(nonExistUserId)).willReturn(null);
		
		Long exercisePostId = Long.valueOf(1);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			exercisePostRestController.addLikeToExercisePost(exercisePostId);
		});
	}
	
	@Test
	@DisplayName("유저가 게시글에 좋아요 추가했을때 처리 -> 해당 Id의 ExercisePost 없으면")
	public void addLikeToExercisePostNonExistExercisePost() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		stubbingUserServiceFindWithPostLikesById(loginIdInSession);
		
		Long nonExistExercisePostId = Long.valueOf(1);
		given(exercisePostService.findWithPostLikesById(nonExistExercisePostId)).willReturn(null);
		
		//When, Then
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostRestController.addLikeToExercisePost(nonExistExercisePostId);
		});
	}
	
	@Test
	@DisplayName("유저가 게시글에 좋아요 추가했을때 처리 -> 해당 ExercisePost에 User의 PostLike가 이미 존재")
	public void addLikeToExercisePostAlredyExist() {
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
	@DisplayName("유저가 게시글에 좋아요 추가했을때 처리 -> 정상")
	public void addLikeToExercisePostTest() {
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
	 * public void removeLikeToExercisePost(@PathVariable("postId") Long postId) 
	 */
	
	@Test
	@DisplayName("유저가 게시글에 좋아요 취소했을때 처리 -> 세션의 저장된 UserId와 postId를 만족하는 PostLike 없음")
	public void removeLikeToExercisePostPostLikeNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		
		Long exercisePostId = Long.valueOf(1);
		given(postLikeService.findByUserIdAndExercisePostId(loginIdInSession, exercisePostId)).willReturn(null);
		
		//When,Then
		assertThrows(LikeNotFoundedException.class, () -> {
			exercisePostRestController.removeLikeToExercisePost(exercisePostId);
		});
	}
	
	@Test
	@DisplayName("유저가 게시글에 좋아요 취소했을때 처리 -> 정상")
	public void removeLikeToExercisePostTest() {
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
	 * public void addLikeToComment(@PathVariable("postId") Long postId, @RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@DisplayName("유저가 댓글에 좋아요 추가 요청 처리 -> 세션에 저장된 Id의 User 없음")
	public void addLikeToCommentUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(nonExistUserId);
		
		given(userService.findWithCommentLikesById(nonExistUserId)).willReturn(null);
		
		Long exercisePostId = Long.valueOf(1);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			exercisePostRestController.addLikeToComment(exercisePostId, nonExistUserId);
		});
	}
	
	@Test
	@DisplayName("유저가 댓글에 좋아요 추가 요청 처리 -> 해당 Id의 Comment 없으면")
	public void addLikeToCommentCommentNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		stubbingUserServiceFindWithCommentLikesById(loginIdInSession);
		
		Long nonExistCommentId = Long.valueOf(1);
		given(commentService.findWithCommentLikesById(nonExistCommentId)).willReturn(null);
		
		Long exercisePostId = Long.valueOf(1);
		//When, Then
		assertThrows(CommentNotFoundedException.class, () -> {
			exercisePostRestController.addLikeToComment(exercisePostId, nonExistCommentId);
		});
	}
	
	@Test
	@DisplayName("유저가 댓글에 좋아요 추가 요청 처리 -> 해당 유저가 댓글에 이미 좋아요 추가했으면")
	public void addLikeToCommentAlreadyExist() {
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
	@DisplayName("유저가 댓글에 좋아요 추가 요청 처리 -> 정상")
	public void addLikeToCommentTest() {
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
	 * public void removeLikeToComment(@PathVariable("postId") Long postId, @RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@DisplayName("유저가 댓글에 좋아요 취소 요청 처리 -> 해당 유저가 해당 댓글에 좋아요 추가하지 않았었으면")
	public void removeLikeToCommentCommentLikeNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionByLoginIdInSession(loginIdInSession);
		
		Long commentId = Long.valueOf(1);
		given(commentLikeService.findByUserIdAndCommentId(loginIdInSession, commentId)).willReturn(null);
		
		Long exercisePostId = Long.valueOf(1);
		//When, Then
		assertThrows(LikeNotFoundedException.class, () -> {
			exercisePostRestController.removeLikeToComment(exercisePostId, commentId);
		});
	}
	
	@Test
	@DisplayName("유저가 댓글에 좋아요 취소 요청 처리 -> 정상")
	public void removeLikeToCommentTest() {
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
	 * public void removeCommentToExercisePost(@PathVariable("postId") Long postId,@RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@DisplayName("유저가 게시글에 댓글 삭제할때 처리 -> 정상")
	public void removeCommentToExercisePostTest() {
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
