package com.gunyoung.tmb.services.domain.exercise;

import com.gunyoung.tmb.domain.exercise.Comment;

public interface CommentService {
	public Comment findById(Long id);
	
	public Comment save(Comment comment);
	
	public void delete(Comment comment);
}
