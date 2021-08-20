package com.gunyoung.tmb.services.domain.like;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.repos.PostLikeRepository;
import com.gunyoung.tmb.utils.CacheUtil;

import lombok.RequiredArgsConstructor;

/**
 * PostLikeService 구현하는 클래스
 * @author kimgun-yeong
 *
 */
@Service("postLikeService")
@Transactional
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {
	
	private final PostLikeRepository postLikeRepository;

	@Override
	@Transactional(readOnly=true)
	public PostLike findById(Long id) {
		Optional<PostLike> result = postLikeRepository.findById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly = true)
	public PostLike findByUserIdAndExercisePostId(Long userId, Long exercisePostId) {
		Optional<PostLike> result = postLikeRepository.findByUserIdAndExercisePostId(userId, exercisePostId);
		if(result.isEmpty())
			return null;
		
		return result.get();
	}

	@Override
	public PostLike save(PostLike postLike) {
		return postLikeRepository.save(postLike);
	}
	
	@Override
	public PostLike createAndSaveWithUserAndExercisePost(User user, ExercisePost exercisePost) {
		PostLike postLike = PostLike.builder()
				.user(user)
				.exercisePost(exercisePost)
				.build();
		
		user.getPostLikes().add(postLike);
		exercisePost.getPostLikes().add(postLike);
		
		return save(postLike);
	}

	@Override
	public void delete(PostLike postLike) {
		postLikeRepository.delete(postLike);
	}

	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=CacheUtil.POST_LIKE_NAME,
		key="#root.methodName.concat(':').concat('#userId').concat(':').concat('#exercisePostId')",
		unless="#result != true")
	public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId) {
		return postLikeRepository.existsByUserIdAndExercisePostId(userId, exercisePostId);
	}
	
}
