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
	
	private final PostLikeRepository postLikeRepository;

	/**
	 * ID로 PostLike 찾기
	 * @param id 찾으려는 PostLike의 id
	 * @return PostLike, Null( 해당 id의 PostLike 없으면)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public PostLike findById(Long id) {
		Optional<PostLike> result = postLikeRepository.findById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}
	
	/**
	 * User Id, ExercisePost Id 로 PostLike 찾기
	 * @param userId 찾으려는 PostLike를 추가한 User Id
	 * @param exercisePostId 찾으려는 PostLike의 대상 게시물 Id
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly = true)
	public PostLike findByUserIdAndExercisePostId(Long userId, Long exercisePostId) {
		Optional<PostLike> result = postLikeRepository.findByUserIdAndExercisePostId(userId, exercisePostId);
		if(result.isEmpty())
			return null;
		
		return result.get();
	}

	/**
	 * PostLike 생성 및 수정 <br>
	 * 캐쉬 사용
	 * @param postLike 저장하려는 PostLike
	 * @return 저장된 postLike
	 * @author kimgun-yeong
	 */
	@Override
	@CacheEvict(cacheNames=CacheUtil.POST_LIKE_NAME,allEntries=true)
	public PostLike save(PostLike postLike) {
		return postLikeRepository.save(postLike);
	}
	
	/**
	 * PostLike 생성 및 Uset, ExercisePost 와 연관 관계 설정 후 저장
	 * @param user 게시글에 좋아요를 누른 사람
	 * @param exercisePost 좋아요가 추가된 게시글 
	 * @author kimgun-yeong
	 */
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

	/**
	 * PostLike 삭제 <br>
	 * {@code CacheUtil.POST_LIKE_NAME} 관련 캐쉬 삭제
	 * @param postLike 삭제하려는 PostLike
	 * @author kimgun-yeong
	 */
	@Override
	@CacheEvict(cacheNames=CacheUtil.POST_LIKE_NAME,allEntries=true)
	public void delete(PostLike postLike) {
		postLikeRepository.delete(postLike);
	}

	/**
	 * User Id, ExercisePost Id로 PostLike 존재 여부 반환 <br>
	 * {@code CacheUtil.POST_LIKE_NAME} 관련 캐쉬 삭제
	 * @param userId 찾으려는 PostLike를 추가한 User Id
	 * @param exercisePostId 찾으려는 PostLike의 대상 게시물 Id
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=CacheUtil.POST_LIKE_NAME,
		key="#root.methodName.concat(':').concat('#userId').concat(':').concat('#exercisePostId')",
		unless="#result != true")
	public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId) {
		return postLikeRepository.existsByUserIdAndExercisePostId(userId, exercisePostId);
	}
	
}
