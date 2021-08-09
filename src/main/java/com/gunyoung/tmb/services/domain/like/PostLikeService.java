package com.gunyoung.tmb.services.domain.like;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;

public interface PostLikeService {
	public PostLike findById(Long id);
	public PostLike findByUserIdAndExercisePostId(Long userId, Long exercisePostId);
	
	public PostLike save(PostLike postLike);
	public PostLike createAndSaveWithUserAndExercisePost(User user, ExercisePost exercisePost);
	
	public void delete(PostLike postLike);
	
	public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId);
}
