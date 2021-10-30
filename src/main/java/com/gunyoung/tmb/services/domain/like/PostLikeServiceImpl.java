package com.gunyoung.tmb.services.domain.like;

import static com.gunyoung.tmb.utils.CacheConstants.*;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.precondition.Preconditions;
import com.gunyoung.tmb.repos.PostLikeRepository;

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
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public PostLike findByUserIdAndExercisePostId(Long userId, Long exercisePostId) {
		Optional<PostLike> result = postLikeRepository.findByUserIdAndExercisePostId(userId, exercisePostId);
		return result.orElse(null);
	}

	@Override
	@CacheEvict(cacheNames=POST_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_POST_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#postLike.user.id).concat(':').concat(#postLike.exercisePost.id)")
	public PostLike save(PostLike postLike) {
		return postLikeRepository.save(postLike);
	}
	
	@Override
	@CacheEvict(cacheNames=POST_LIKE_NAME,
	key="#root.target.EXIST_BY_USER_ID_AND_POST_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#user.id).concat(':').concat(#exercisePost.id)")
	public PostLike createAndSaveWithUserAndExercisePost(User user, ExercisePost exercisePost) {
		Preconditions.notNull(user, "Given user must be not null");
		Preconditions.notNull(exercisePost, "Given exercisePost must be not null");
		
		PostLike newPostLike = PostLike.of(user, exercisePost);
		user.getPostLikes().add(newPostLike);
		exercisePost.getPostLikes().add(newPostLike);
		
		return save(newPostLike);
	}

	@Override
	@CacheEvict(cacheNames=POST_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_POST_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#postLike.user.id).concat(':').concat(#postLike.exercisePost.id)")
	public void delete(PostLike postLike) {
		postLikeRepository.delete(postLike);
	}
	
	@Override
	public void deleteAllByUserId(Long userId) {
		postLikeRepository.deleteAllByUserIdInQuery(userId);
	}
	
	@Override
	public void deleteAllByExercisePostId(Long exercisePostId) {
		postLikeRepository.deleteAllByExercisePostIdInQuery(exercisePostId);
	}
	
	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=POST_LIKE_NAME,
		key="#root.target.EXIST_BY_USER_ID_AND_POST_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#userId).concat(':').concat(#exercisePostId)",
		unless="#result != true")
	public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId) {
		return postLikeRepository.existsByUserIdAndExercisePostId(userId, exercisePostId);
	}
}
