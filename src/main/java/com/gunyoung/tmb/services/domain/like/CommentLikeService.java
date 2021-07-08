package com.gunyoung.tmb.services.domain.like;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;

public interface CommentLikeService {
	public CommentLike findById(Long id);
	public CommentLike findByUserIdAndCommentId(Long userId, Long commentId);
	
	public CommentLike save(CommentLike commentLike);
	public CommentLike saveWithUserAndComment(User user, Comment comment);
	
	public void delete(CommentLike commentLike);
	
	public boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}
