package com.gunyoung.tmb.services.domain.like;

import com.gunyoung.tmb.domain.like.PostLike;

public interface PostLikeService {
	public PostLike findById(Long id);
	
	public PostLike save(PostLike postLike);
	
	public void delete(PostLike postLike);
}
