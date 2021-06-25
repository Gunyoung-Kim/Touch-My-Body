package com.gunyoung.tmb.services.domain.exercise;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;

public interface CommentService {
	public Comment findById(Long id);
	
	public Comment save(Comment comment);
	public Comment saveWithUserAndExercisePost(Comment comment,User user, ExercisePost exercisePost);
	
	public void delete(Comment comment);
}
