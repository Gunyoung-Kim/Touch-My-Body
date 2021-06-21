package com.gunyoung.tmb.services.domain.like;

import com.gunyoung.tmb.domain.like.CommentLike;

public interface CommentLikeService {
	public CommentLike findById(Long id);
	
	public CommentLike save(CommentLike commentLike);
	
	public void delete(CommentLike commentLike);
}
