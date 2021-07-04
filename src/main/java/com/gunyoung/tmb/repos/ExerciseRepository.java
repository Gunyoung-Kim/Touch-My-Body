package com.gunyoung.tmb.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.jpa.ExerciseNameAndTargetDTO;

public interface ExerciseRepository extends JpaRepository<Exercise,Long>{
	public Optional<Exercise> findByName(String name);
	
	@Query("SELECT e FROM Exercise e "
			+ "LEFT JOIN FETCH e.feedbacks f "
			+ "WHERE e.id = :exerciseId")
	public Optional<Exercise> findWithFeedbacksById(@Param("exerciseId") Long exerciseId);
	
	public Page<Exercise> findAll(Pageable pageable);
	
	/**
	 * 이름에 키워드를 포함하는 Exercise들 Page로 가져오기
	 * @param keyword
	 * @param pageable
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT e FROM Exercise e "
			+ "WHERE e.name LIKE %:keyword%")
	public Page<Exercise> findAllWithNameKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	/**
	 * 모든 운동 정보들을 ExerciesSortDTO로 매핑해서 가져오는 메소드 
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.jpa.ExerciseNameAndTargetDTO(e.name, e.target) from Exercise e")
	public List<ExerciseNameAndTargetDTO> findAllWithNameAndTarget();
	
	/**
	 * 이름에 키워드를 포함하는 Exercise들 개수 가져오기 
	 * @param keyword
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(e) FROM Exercise e "
			+ "WHERE e.name LIKE %:keyword%")
	public long countAllWithNameKeyword(@Param("keyword") String keyword);
	
	/**
	 * 
	 * @param name
	 * @return
	 * @author kimgun-yeong
	 */
	public boolean existsByName(String name);
}
