package com.gunyoung.tmb.controller.rest;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull;
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
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

/**
 * ExercisePost 관련 요청 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ExercisePostRestController {
	
	private final HttpSession session;
	
	private final UserService userService;
	
	private final ExercisePostService exercisePostService;
	
	private final PostLikeService postLikeService;
	
	private final CommentService commentService;
	
	private final CommentLikeService commentLikeService;
	
	/**
	 * 유저가 게시글에 좋아요 추가했을때 처리하는 메소드
	 * @param postId 게시글 추가하려는 대상 ExercisePost의 Id
	 * @throws UserNotFoundedException 세션에 저장된 Id에 해당하는 User 없으면
	 * @throws ExercisePostNotFoundedException 해당 Id의 ExercisePost 없으면
	 * @throws LikeAlreadyExistException 해당 ExercisePost에 User의 PostLike가 이미 존재한다면
	 * @author kimgun-yeong
	 */
	@PostMapping(value = "/community/post/{postId}/addLike")
	@LoginIdSessionNotNull
	public void addLikeToExercisePost(@PathVariable("postId") Long postId) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		User user = userService.findWithPostLikesById(loginUserId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		ExercisePost exercisePost = exercisePostService.findWithPostLikesById(postId);
		if(exercisePost == null) { 
			throw new ExercisePostNotFoundedException(ExercisePostErrorCode.EXERCISE_POST_NOT_FOUNDED_ERROR.getDescription());
		}
		
		if(postLikeService.existsByUserIdAndExercisePostId(loginUserId, postId)) { 
			throw new LikeAlreadyExistException(LikeErrorCode.LIKE_ALREADY_EXIST_ERROR.getDescription());
		}
		
		postLikeService.createAndSaveWithUserAndExercisePost(user, exercisePost);
	}
	
	/**
	 * 유저가 게시글에 좋아요 취소했을때 처리하는 메소드
	 * @param postId 게시글 취소하려는 ExercisePost의 Id
	 * @throws LikeNotFoundedException 세션의 저장된 UserId와 postId를 만족하는 PostLike 없으면
	 * @author kimgun-yeong
	 */
	@DeleteMapping(value = "/community/post/{postId}/removelike")
	@LoginIdSessionNotNull
	public void removeLikeToExercisePost(@PathVariable("postId") Long postId) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		PostLike postLike = postLikeService.findByUserIdAndExercisePostId(loginUserId, postId);
		if(postLike == null) {
			throw new LikeNotFoundedException(LikeErrorCode.LIKE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		postLikeService.delete(postLike);
	}
	
	/**
	 * 유저가 댓글에 좋아요 추가 요청 처리하는 메소드
	 * @param postId 댓글이 속해있는 ExercisePost의 Id
	 * @param commentId 좋아요 추가하려는 대상 Comment의 Id
	 * @throws UserNotFoundedException 세션에 저장된 Id의 User 없으면
	 * @throws CommentNotFoundedException 해당 Id의 Comment 없으면
	 * @throws LikeAlreadyExistException 해당 유저가 댓글에 이미 좋아요 추가했으면
	 * @author kimgun-yeong
	 */
	@PostMapping(value = "/community/post/{postId}/comment/addlike")
	@LoginIdSessionNotNull
	public void addLikeToComment(@PathVariable("postId") Long postId, @RequestParam("commentId") Long commentId) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		User user = userService.findWithCommentLikesById(loginUserId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Comment comment = commentService.findWithCommentLikesById(commentId);
		if(comment == null) { 
			throw new CommentNotFoundedException(CommentErrorCode.COMMENT_NOT_FOUNDED_ERROR.getDescription());
		}
		
		if(commentLikeService.existsByUserIdAndCommentId(loginUserId, commentId)) { 
			throw new LikeAlreadyExistException(LikeErrorCode.LIKE_ALREADY_EXIST_ERROR.getDescription());
		}
		
		commentLikeService.createAndSaveWithUserAndComment(user, comment);
	}
	
	/**
	 * 유저가 댓글에 좋아요 취소 요청 처리하는 메소드
	 * @param postId 댓글이 소속된 ExercisePost id
	 * @param commentId 좋아요를 취소하고자 하는 댓글
	 * @throws LikeNotFoundedException 해당 유저가 해당 댓글에 좋아요 추가하지 않았었으면
	 * @author kimgun-yeong
	 */
	@DeleteMapping(value = "/community/post/{postId}/comment/removelike")
	@LoginIdSessionNotNull
	public void removeLikeToComment(@PathVariable("postId") Long postId, @RequestParam("commentId") Long commentId) { 
		Long loginUserId = SessionUtil.getLoginUserId(session);	
		CommentLike commentLike = commentLikeService.findByUserIdAndCommentId(loginUserId, commentId);
		if(commentLike == null) {
			throw new LikeNotFoundedException(LikeErrorCode.LIKE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		commentLikeService.delete(commentLike);
	}
	
	/**
	 * 유저가 게시글에 댓글 삭제할때 처리하는 메소드
	 * @param postId 댓글이 작성된 ExercisePost의 ID
	 * @param commentId 삭제하려는 대상 Comment의 ID
	 * @author kimgun-yeong
	 */
	@DeleteMapping(value = "/community/post/{postId}/removeComment")
	@LoginIdSessionNotNull
	public void removeCommentToExercisePost(@PathVariable("postId") Long postId, @RequestParam("commentId") Long commentId) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		
		commentService.checkIsMineAndDelete(loginUserId, commentId);
	}
	
}
