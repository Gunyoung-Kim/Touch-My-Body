package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.like.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike,Long>{
	/**
	 * Inner Join을 통한 성능 향상  
	 * @param userId
	 * @param exercisePostId
	 * @return
	 */
	@Query("select pl from PostLike pl "
			+ "inner join pl.user u on u.id = :userId "
			+ "inner join pl.exercisePost ep on ep.id = :exercisePostId")
	public Optional<PostLike> findByUserIdAndExercisePostIdCustom(@Param("userId") Long userId,@Param("exercisePostId") Long exercisePostId);
}
