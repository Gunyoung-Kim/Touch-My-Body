package com.gunyoung.tmb.services.domain.like;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;

public interface PostLikeService {
	
	/**
	 * ID로 PostLike 찾기
	 * @param id 찾으려는 PostLike의 id
	 * @return PostLike, Null( 해당 id의 PostLike 없으면)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public PostLike findById(Long id);
	
	/**
	 * User Id, ExercisePost Id 로 PostLike 찾기
	 * @param userId 찾으려는 PostLike를 추가한 User Id
	 * @param exercisePostId 찾으려는 PostLike의 대상 게시물 Id
	 * @since 11
	 * @author kimgun-yeong
	 */
	public PostLike findByUserIdAndExercisePostId(Long userId, Long exercisePostId);
	
	/**
	 * PostLike 생성 및 수정 <br>
	 * {@code CacheUtil.POST_LIKE_NAME} 관련 캐쉬 삭제
	 * @param postLike 저장하려는 PostLike
	 * @return 저장된 postLike
	 * @author kimgun-yeong
	 */
	public PostLike save(PostLike postLike);
	
	/**
	 * PostLike 생성 및 User, ExercisePost 와 연관 관계 설정 후 저장 <br>
	 * {@code CacheUtil.POST_LIKE_NAME} 관련 캐쉬 삭제
	 * @param user 게시글에 좋아요를 누른 사람
	 * @param exercisePost 좋아요가 추가된 게시글 
	 * @author kimgun-yeong
	 */
	public PostLike createAndSaveWithUserAndExercisePost(User user, ExercisePost exercisePost);
	
	/**
	 * PostLike 삭제 <br>
	 * {@code CacheUtil.POST_LIKE_NAME} 관련 캐쉬 삭제
	 * @param postLike 삭제하려는 PostLike
	 * @author kimgun-yeong
	 */
	public void delete(PostLike postLike);
	
	/**
	 * User Id, ExercisePost Id로 PostLike 존재 여부 반환 <br>
	 * 캐시 사용, 결과값이 true인 경우에만
	 * @param userId 찾으려는 PostLike를 추가한 User Id
	 * @param exercisePostId 찾으려는 PostLike의 대상 게시물 Id
	 * @author kimgun-yeong
	 */
	public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId);
}
