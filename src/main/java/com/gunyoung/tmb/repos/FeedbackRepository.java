package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.dto.response.FeedbackManageListDTO;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;

public interface FeedbackRepository extends JpaRepository<Feedback,Long>{
	
	/**
	 * 
	 * @param id
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.FeedbackViewDTO (f.id, f.title, f.content, u.nickName, e.name, f.createdAt) FROM Feedback f "
			+ "INNER JOIN f.user u "
			+ "INNER JOIN f.exercise e "
			+ "WHERE f.id = :feedbackId")
	public Optional<FeedbackViewDTO> findForFeedbackViewDTOById(@Param("feedbackId") Long id);
	
	/**
	 * 
	 * @param id
	 * @param pageable
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT f FROM Feedback f "
			+ "INNER JOIN f.exercise e "
			+ "WHERE e.id = :exerciseId "
			+ "ORDER BY f.createdAt ASC")
	public Page<Feedback> findAllByExerciseIdByPage(@Param("exerciseId") Long exericseId, Pageable pageable);
	
	/**
	 * <br>
	 * 피드백 반영 되지 않은것만 가져옴
	 * @param exericseId
	 * @param pageable
	 * @return
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.FeedbackManageListDTO (f.id, f.title, u.nickName, e.name, f.createdAt) FROM Feedback f "
			+ "INNER JOIN f.user u "
			+ "INNER JOIN f.exercise e "
			+ "WHERE (e.id = :exerciseId) "
			+ "AND (f.isReflected = false) "
			+ "ORDER BY f.createdAt ASC")
	public Page<FeedbackManageListDTO> findAllForFeedbackManageListDTOByExerciseIdByPage(@Param("exerciseId") Long exericseId, Pageable pageable);
	
	/**
	 * 
	 * @param exerciseId
	 * @return
	 */
	@Query("SELECT COUNT(f) FROM Feedback f "
			+ "INNER JOIN f.exercise e "
			+ "WHERE e.id = :exerciseId")
	public long countByExerciseId(@Param("exerciseId") Long exerciseId);
}
