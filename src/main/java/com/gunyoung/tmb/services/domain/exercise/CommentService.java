package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;

public interface CommentService {
	public Comment findById(Long id);
	public List<Comment> findAllByExercisePostId(Long postId);
	
	public Comment save(Comment comment);
	public Comment saveWithUserAndExercisePost(Comment comment,User user, ExercisePost exercisePost);
	
	public void delete(Comment comment);
	
	public List<CommentForPostViewDTO> getCommentForPostViewDTOsByExercisePostId(Long postId);
}
