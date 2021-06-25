package com.gunyoung.tmb.controller.rest;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.error.codes.ExercisePostErrorCode;
import com.gunyoung.tmb.error.codes.LikeErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExercisePostNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.LikeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.like.PostLikeService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.SessionUtil;

@RestController
public class ExercisePostRestController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ExercisePostService exercisePostService;
	
	@Autowired
	PostLikeService postLikeService;
	
	
	@RequestMapping(value="/community/post/{post_id}/addLike",method = RequestMethod.POST)
	public void addLikeToExercisePost(@PathVariable("post_id") Long postId) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findById(userId);
		
		if(user == null)
			throw new UserNotFoundedException(UserErrorCode.UserNotFoundedError.getDescription());
		
		ExercisePost exercisePost = exercisePostService.findById(postId);
		
		if(exercisePost == null) 
			throw new ExercisePostNotFoundedException(ExercisePostErrorCode.ExercisePostNotFoundedError.getDescription());
		
		postLikeService.saveWithUserAndExercisePost(user, exercisePost);
	}
	
	@RequestMapping(value="/community/post/{post_id}/removeLike",method = RequestMethod.POST)
	public void removeLikeToExercisePost(@PathVariable("post_id") Long postId) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		PostLike postLike = postLikeService.findByUserIdAndExercisePostId(userId, postId);
		
		if(postLike == null) 
			throw new LikeNotFoundedException(LikeErrorCode.LikeNotFoundedError.getDescription());
		
		postLikeService.delete(postLike);
	}
}
