package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.like.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike,Long>{
	/**
	 * Inner Join FETCH로 user 객체와 exercisePost 객체 한번에 가져오는 메소드  
	 * @param userId
	 * @param exercisePostId
	 * @return
	 */
	@Query("SELECT pl FROM PostLike pl "
			+ "JOIN FETCH pl.user u "
			+ "JOIN FETCH pl.exercisePost ep "
			+ "WHERE (u.id = :userId) "
			+ "AND (ep.id = :exercisePostId)")
	public Optional<PostLike> findByUserIdAndExercisePostIdCustom(@Param("userId") Long userId,@Param("exercisePostId") Long exercisePostId);
	
	/**
	 * 유저 ID와 ExercisePost ID로 해당 PostLike 존재하는지 여부
	 * @param userId
	 * @param exercisePostId
	 * @return
	 */
	public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId);
}
