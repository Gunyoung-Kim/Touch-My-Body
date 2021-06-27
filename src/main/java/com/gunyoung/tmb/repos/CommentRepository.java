package com.gunyoung.tmb.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
	
	/**
	 * ExercisePost ID로 만족하는 Comment들 가져오는 쿼리 <br>
	 * INNER JOIN을 통한 성능 향상
	 * @param postId
	 * @return
	 */
	@Query("SELECT c FROM Comment c "
			+ "INNER JOIN c.exercisePost ep "
			+ "ON ep.id = :postId "
			+ "ORDER BY c.createdAt ASC")
	public List<Comment> findAllByExercisePostIdCustom(@Param("postId")Long postId);
}
