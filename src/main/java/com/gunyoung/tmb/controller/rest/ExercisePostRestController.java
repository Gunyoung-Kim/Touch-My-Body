package com.gunyoung.tmb.controller.rest;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.gunyoung.tmb.error.exceptions.notmatch.UserNotMatchException;
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
	@RequestMapping(value="/community/post/{post_id}/addLike",method = RequestMethod.POST)
	@LoginIdSessionNotNull
	public void addLikeToExercisePost(@PathVariable("post_id") Long postId) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findWithPostLikesById(userId);
		
		if(user == null)
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		
		ExercisePost exercisePost = exercisePostService.findWithPostLikesById(postId);
		
		if(exercisePost == null) 
			throw new ExercisePostNotFoundedException(ExercisePostErrorCode.EXERCISE_POST_NOT_FOUNDED_ERROR.getDescription());
		
		if(postLikeService.existsByUserIdAndExercisePostId(userId, postId)) 
			throw new LikeAlreadyExistException(LikeErrorCode.LIKE_ALREADY_EXIST_ERROR.getDescription());
		
		postLikeService.saveWithUserAndExercisePost(user, exercisePost);
	}
	
	/**
	 * 유저가 게시글에 좋아요 취소했을때 처리하는 메소드
	 * @param postId 게시글 취소하려는 ExercisePost의 Id
	 * @throws LikeNotFoundedException 세션의 저장된 UserId와 postId를 만족하는 PostLike 없으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/removeLike",method = RequestMethod.POST)
	@LoginIdSessionNotNull
	public void removeLikeToExercisePost(@PathVariable("post_id") Long postId) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		PostLike postLike = postLikeService.findByUserIdAndExercisePostId(userId, postId);
		
		if(postLike == null) 
			throw new LikeNotFoundedException(LikeErrorCode.LIKE_NOT_FOUNDED_ERROR.getDescription());
		
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
	@RequestMapping(value="/community/post/{post_id}/comment/addlike",method = RequestMethod.POST)
	@LoginIdSessionNotNull
	public void addLikeToComment(@PathVariable("post_id") Long postId, @RequestParam("commentId") Long commentId) {
		// 세션에 저장된 현재 접속자의 id 가져와서 이를 통해 User 객체 가져옴
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findWithCommentLikesById(userId);
		
		if(user == null) 
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		
		// commentId로 comment 가져옴
		Comment comment = commentService.findWithCommentLikesById(commentId);
		
		if(comment == null) 
			throw new CommentNotFoundedException(CommentErrorCode.COMMENT_NOT_FOUNDED_ERROR.getDescription());
		
		if(commentLikeService.existsByUserIdAndCommentId(userId, commentId)) 
			throw new LikeAlreadyExistException(LikeErrorCode.LIKE_ALREADY_EXIST_ERROR.getDescription());
		
		commentLikeService.saveWithUserAndComment(user, comment);
	}
	
	/**
	 * 유저가 댓글에 좋아요 취소 요청 처리하는 메소드
	 * @param postId 댓글이 소속된 ExercisePost id
	 * @param commentId 좋아요를 취소하고자 하는 댓글
	 * @throws LikeNotFoundedException 해당 유저가 해당 댓글에 좋아요 추가하지 않았었으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/comment/removelike",method = RequestMethod.POST)
	@LoginIdSessionNotNull
	public void removeLikeToComment(@PathVariable("post_id") Long postId, @RequestParam("commentId") Long commentId) { 
		Long userId = SessionUtil.getLoginUserId(session);	
		
		CommentLike commentLike = commentLikeService.findByUserIdAndCommentId(userId, commentId);
		
		if(commentLike == null) 
			throw new LikeNotFoundedException(LikeErrorCode.LIKE_NOT_FOUNDED_ERROR.getDescription());
		
		commentLikeService.delete(commentLike);
	}
	
	/**
	 * 유저가 게시글에 댓글 삭제할때 처리하는 메소드
	 * @param postId 댓글이 작성된 ExercisePost의 ID
	 * @param commentId 삭제하려는 대상 Comment의 ID
	 * @throws CommentNotFoundedException 해당 ID의 Comment 없으면
	 * @throws UserNotMatchException 세션에 저장된 ID와 댓글 작성자의 ID 일치하지 않으면 
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/removeComment",method = RequestMethod.DELETE)
	@LoginIdSessionNotNull
	public void removeCommentToExercisePost(@PathVariable("post_id") Long postId,@RequestParam("commentId") Long commentId) {
		Comment comment = commentService.findWithUserAndExercisePostById(commentId);
		
		if(comment == null) {
			throw new CommentNotFoundedException(CommentErrorCode.COMMENT_NOT_FOUNDED_ERROR.getDescription());
		}
		
		User commentUser = comment.getUser();
		
		if(SessionUtil.getLoginUserId(session) != commentUser.getId()) {
			throw new UserNotMatchException(UserErrorCode.USER_NOT_MATCH_ERROR.getDescription());
		}
		
		commentService.delete(comment);
	}
	
}
