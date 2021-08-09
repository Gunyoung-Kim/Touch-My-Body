package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.like.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike,Long>{
	
	/**
	 * User Id, ExercisePost Id 를 만족하는 PostLike 찾기   
	 * @param userId 찾으려는 PostLike의 User ID
	 * @param exercisePostId 찾으려는 ExercisePost의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT pl FROM PostLike pl "
			+ "INNER JOIN pl.user u "
			+ "INNER JOIN pl.exercisePost ep "
			+ "WHERE (u.id = :userId) "
			+ "AND (ep.id = :exercisePostId)")
	public Optional<PostLike> findByUserIdAndExercisePostId(@Param("userId") Long userId, @Param("exercisePostId") Long exercisePostId);
	
	/**
	 * User Id, ExercisePost Id 를 만족하는 PostLike 찾기 <br>
	 * User, ExercisePost Inner 페치 조인   
	 * @param userId 찾으려는 PostLike의 User ID
	 * @param exercisePostId 찾으려는 ExercisePost의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT pl FROM PostLike pl "
			+ "JOIN FETCH pl.user u "
			+ "JOIN FETCH pl.exercisePost ep "
			+ "WHERE (u.id = :userId) "
			+ "AND (ep.id = :exercisePostId)")
	public Optional<PostLike> findByUserIdAndExercisePostIdFetch(@Param("userId") Long userId, @Param("exercisePostId") Long exercisePostId);
	
	/**
	 * User ID와 ExercisePost ID로 해당 PostLike 존재하는지 여부 확인
	 * @param userId 찾으려는 PostLike의 User ID
	 * @param exercisePostId 찾으려는 ExercisePost의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId);
}
