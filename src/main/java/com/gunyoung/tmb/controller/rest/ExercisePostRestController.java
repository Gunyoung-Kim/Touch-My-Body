package com.gunyoung.tmb.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.AddCommentDTO;
import com.gunyoung.tmb.error.codes.CommentErrorCode;
import com.gunyoung.tmb.error.codes.ExercisePostErrorCode;
import com.gunyoung.tmb.error.codes.LikeErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
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
 * 
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
	 * @param postId
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/addLike",method = RequestMethod.POST)
	public void addLikeToExercisePost(@PathVariable("post_id") Long postId) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findWithPostLikesById(userId);
		
		if(user == null)
			throw new UserNotFoundedException(UserErrorCode.UserNotFoundedError.getDescription());
		
		ExercisePost exercisePost = exercisePostService.findWithPostLikesById(postId);
		
		if(exercisePost == null) 
			throw new ExercisePostNotFoundedException(ExercisePostErrorCode.ExercisePostNotFoundedError.getDescription());
		
		postLikeService.saveWithUserAndExercisePost(user, exercisePost);
	}
	
	/**
	 * 유저가 게시글에 좋아요 취소했을때 처리하는 메소드
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/removeLike",method = RequestMethod.POST)
	public void removeLikeToExercisePost(@PathVariable("post_id") Long postId) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		PostLike postLike = postLikeService.findByUserIdAndExercisePostId(userId, postId);
		
		if(postLike == null) 
			throw new LikeNotFoundedException(LikeErrorCode.LikeNotFoundedError.getDescription());
		
		postLikeService.delete(postLike);
	}
	
	/**
	 * 유저가 게시글에 댓글 추가할때 처리하는 메소드 
	 * @param postId
	 * @param dto
	 * @param request
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/addComment",method = RequestMethod.POST)
	public void addCommentToExercisePost(@PathVariable("post_id") Long postId,@ModelAttribute AddCommentDTO dto,HttpServletRequest request) {
		// 세션으로 가져온 유저 id로 유저 객체 찾기
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findWithCommentsById(userId);
		
		if(user == null)
			throw new UserNotFoundedException(UserErrorCode.UserNotFoundedError.getDescription());
		
		// post_id 로 해당 ExercisePost 가져오기 
		ExercisePost exercisePost = exercisePostService.findWithCommentsById(postId);
		
		if(exercisePost == null) 
			throw new ExercisePostNotFoundedException(ExercisePostErrorCode.ExercisePostNotFoundedError.getDescription());
		
		// request IP 가져오기 
		String writerIp = request.getRemoteHost();
		
		Comment comment = AddCommentDTO.toComment(dto, writerIp);
		
		commentService.saveWithUserAndExercisePost(comment, user, exercisePost);
	}
	
	/**
	 * 유저가 게시글에 댓글 삭제할때 처리하는 메소드
	 * @param postId
	 * @param commentId
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/removeComment",method = RequestMethod.POST)
	public void removeCommentToExercisePost(@PathVariable("post_id") Long postId,@RequestParam("commentId") Long commentId) {
		Comment comment = commentService.findWithUserAndExercisePostById(commentId);
		
		if(comment == null) {
			throw new CommentNotFoundedException(CommentErrorCode.CommentNotFoundedError.getDescription());
		}
		
		User commentUser = comment.getUser();
		
		if(SessionUtil.getLoginUserId(session) != commentUser.getId()) {
			throw new UserNotMatchException(UserErrorCode.UserNotMatchError.getDescription());
			
			// 추후에 매니저나 관리자는 통과되게 구현할지 고민 
		}
		
		commentService.delete(comment);
	}
	
	/**
	 * 유저가 댓글에 좋아요 추가 요청 처리하는 메소드 
	 * @param postId
	 * @param commentId
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/comment/addlike",method = RequestMethod.POST)
	public void addLikeToComment(@PathVariable("post_id") Long postId, @RequestParam("commentId") Long commentId) {
		// 세션에 저장된 현재 접속자의 id 가져와서 이를 통해 User 객체 가져옴
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findWithCommentLikesById(userId);
		
		if(user == null) 
			throw new UserNotFoundedException(UserErrorCode.UserNotFoundedError.getDescription());
		
		// commentId로 comment 가져옴
		Comment comment = commentService.findWithCommentLikesById(commentId);
		
		if(comment == null) 
			throw new CommentNotFoundedException(CommentErrorCode.CommentNotFoundedError.getDescription());
		
		commentLikeService.saveWithUserAndComment(user, comment);
	}
	
	/**
	 * 유저가 댓글에 좋아요 취소 요청 처리하는 메소드
	 * @param postId 댓글이 소속된 게시글 id
	 * @param commentId 좋아요를 취소하고자 하는 댓글
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/comment/removelike",method = RequestMethod.POST)
	public void removeLikeToComment(@PathVariable("post_id") Long postId, @RequestParam("commentId") Long commentId) { 
		Long userId = SessionUtil.getLoginUserId(session);	
		
		CommentLike commentLike = commentLikeService.findByUserIdAndCommentId(userId, commentId);
		
		if(commentLike == null) 
			throw new LikeNotFoundedException(LikeErrorCode.LikeNotFoundedError.getDescription());
		
		commentLikeService.delete(commentLike);
	}
	
}
