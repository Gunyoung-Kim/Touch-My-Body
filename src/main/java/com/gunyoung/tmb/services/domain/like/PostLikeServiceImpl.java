package com.gunyoung.tmb.services.domain.like;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
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
	
	public static final String EXIST_BY_USER_ID_AND_POST_ID_DEFAUALT_CACHE_KEY = "exsitByUserIdAndExercisePostId"; 
	
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
	@CacheEvict(cacheNames=CacheUtil.POST_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_POST_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#postLike.user.id).concat(':').concat(#postLike.exercisePost.id)")
	public PostLike save(PostLike postLike) {
		return postLikeRepository.save(postLike);
	}
	
	@Override
	@CacheEvict(cacheNames=CacheUtil.POST_LIKE_NAME,
	key="#root.target.EXIST_BY_USER_ID_AND_POST_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#user.id).concat(':').concat(#exercisePost.id)")
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
	@CacheEvict(cacheNames=CacheUtil.POST_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_POST_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#postLike.user.id).concat(':').concat(#postLike.exercisePost.id)")
	public void delete(PostLike postLike) {
		postLikeRepository.delete(postLike);
	}

	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=CacheUtil.POST_LIKE_NAME,
		key="#root.target.EXIST_BY_USER_ID_AND_POST_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#userId).concat(':').concat(#exercisePostId)",
		unless="#result != true")
	public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId) {
		return postLikeRepository.existsByUserIdAndExercisePostId(userId, exercisePostId);
	}
	
}
