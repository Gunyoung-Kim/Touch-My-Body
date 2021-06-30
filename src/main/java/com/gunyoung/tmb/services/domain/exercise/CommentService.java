package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;

public interface CommentService {
	public Comment findById(Long id);
	public List<Comment> findAllByExercisePostId(Long postId);
	public Page<Comment> findAllByUserIdOrderByCreatedAtASC(Long userId,Integer pageNum, int pageSize);
	public Page<Comment> findAllByUserIdOrderByCreatedAtDESC(Long userId,Integer pageNum, int pageSize);
	
	public Comment save(Comment comment);
	public Comment saveWithUserAndExercisePost(Comment comment,User user, ExercisePost exercisePost);
	
	public void delete(Comment comment);
	
	public long countByUserId(Long userId);
	
	public List<CommentForPostViewDTO> getCommentForPostViewDTOsByExercisePostId(Long postId);
	
}
