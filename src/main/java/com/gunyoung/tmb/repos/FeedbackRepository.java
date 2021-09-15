package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.dto.response.FeedbackManageListDTO;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;

public interface FeedbackRepository extends JpaRepository<Feedback,Long>{
	
	/**
	 * ID를 만족하는 Feedback 필드 Select 후 {@link FeedbackViewDTO} 매핑하는 쿼리 
	 * @param id 찾으려는 Feedback의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.FeedbackViewDTO (f.id, f.title, f.contents, u.nickName, e.name, f.createdAt) FROM Feedback f "
			+ "INNER JOIN f.user u "
			+ "INNER JOIN f.exercise e "
			+ "WHERE f.id = :feedbackId")
	public Optional<FeedbackViewDTO> findForFeedbackViewDTOById(@Param("feedbackId") Long id);
	
	/**
	 * Exercise ID를 만족하는 모든 Feedback 찾기 <br>
	 * Feedback 생성 오래된 순으로 정렬 <br>
	 * 페이징 처리 
	 * @param exerciseId 찾으려는 Feedback의 Exercise ID
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
	 * Exercise ID를 만족하는 Feedback들의 필드로 {@link FeedbackManageListDTO} 매핑해서 가져오는 쿼리 <br>
	 * 피드백 반영 되지 않은것만 가져옴 <br>
	 * Feedback 생성 오래된 순으로 정렬 <br>
	 * 페이징 처리
	 * @param exericseId 찾으려는 Feedback의 Exercise ID
	 * @param pageable
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.FeedbackManageListDTO (f.id, f.title, u.nickName, e.name, f.createdAt) FROM Feedback f "
			+ "INNER JOIN f.user u "
			+ "INNER JOIN f.exercise e "
			+ "WHERE (e.id = :exerciseId) "
			+ "AND (f.isReflected = false) "
			+ "ORDER BY f.createdAt ASC")
	public Page<FeedbackManageListDTO> findAllForFeedbackManageListDTOByExerciseIdByPage(@Param("exerciseId") Long exericseId, Pageable pageable);
	
	/**
	 * Exercise ID를 만족하는 모든 Feedback 개수 찾기 
	 * @param exerciseId 찾으려는 Feedback의 Exercise ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(f) FROM Feedback f "
			+ "INNER JOIN f.exercise e "
			+ "WHERE e.id = :exerciseId")
	public long countByExerciseId(@Param("exerciseId") Long exerciseId);
	
	/**
	 * User Id를 만족하는 Feedback들 일괄 삭제
	 * @param userId 삭제하려는 Feedback들의 User ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically= true, flushAutomatically = true)
	@Query("DELETE FROM Feedback f "
			+ "WHERE f.user.id = :userId")
	public void deleteAllByUserIdInQuery(@Param("userId") Long userId);
	
	/**
	 * Exercise Id를 만족하는 Feedback들 일괄 삭
	 * @param exerciseId 삭제하려는 Feedback들의 Exercise ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically= true, flushAutomatically = true)
	@Query("DELETE FROM Feedback f "
			+ "WHERE f.exercise.id = :exerciseId")
	public void deleteAllByExerciseIdInQuery(@Param("exerciseId") Long exerciseId);
}
