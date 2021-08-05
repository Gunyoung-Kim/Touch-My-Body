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
	
	/**
	 * 모든 Exercise 찾기 <br>
	 * 페이징 처리 
	 * @author kimgun-yeong
	 */
	public Page<Exercise> findAll(Pageable pageable);
	
	/**
	 * name 필드로 Exercise 찾기
	 * @param name 찾으려는 Exercise의 name
	 * @return
	 * @author kimgun-yeong
	 */
	public Optional<Exercise> findByName(String name);
	
	/**
	 * ID로 Exercise 찾기 <br>
	 * Feedbacks Left 페치 조인
	 * @param exerciseId 찾으려는 Exercise의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT e FROM Exercise e "
			+ "LEFT JOIN FETCH e.feedbacks f "
			+ "WHERE e.id = :exerciseId")
	public Optional<Exercise> findWithFeedbacksById(@Param("exerciseId") Long exerciseId);
	
	/**
	 * name 필드로 Exercise 찾기 <br>
	 * ExercisePosts Left 페치 조인
	 * @param name 찾으려는 Exercise의 name
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT e FROM Exercise e "
			+ "LEFT JOIN FETCH e.exercisePosts ep "
			+ "WHERE e.name = :exerciseName")
	public Optional<Exercise> findWithExercisePostsByName(@Param("exerciseName")String name);
	
	/**
	 * ID로 Exercise 찾기 <br>
	 * ExerciseMuscles Left 페치 조인
	 * @param exerciseId 찾으려는 Exercise의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT e FROM Exercise e "
			+ "LEFT JOIN FETCH e.exerciseMuscles em "
			+ "WHERE e.id = :exerciseId")
	public Optional<Exercise> findWithExerciseMusclesById(@Param("exerciseId")Long exerciseId);
	
	/**
	 * 이름에 키워드로 Exercise들 찾기 <br>
	 * 페이징 처리
	 * @param keyword 찾으려는 Exercise들의 이름 검색 키워드
	 * @param pageable
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT e FROM Exercise e "
			+ "WHERE e.name LIKE %:keyword%")
	public Page<Exercise> findAllWithNameKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	/**
	 * 모든 Exercise들의 필드이용해서 {@link ExerciseNameAndTargetDTO} 로 매핑해서 가져오는 쿼리
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.jpa.ExerciseNameAndTargetDTO(e.name, e.target) from Exercise e")
	public List<ExerciseNameAndTargetDTO> findAllWithNameAndTarget();
	
	/**
	 * 이름에 키워드를 포함하는 Exercise들 개수 가져오기 
	 * @param keyword 찾으려는 Exercise들의 이름 검색 키워드
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(e) FROM Exercise e "
			+ "WHERE e.name LIKE %:keyword%")
	public long countAllWithNameKeyword(@Param("keyword") String keyword);
	
	/**
	 * name 필드로 Exercise 존재하는지 확인하는 쿼리
	 * @param name 찾으려는 Exercise의 name
	 * @return
	 * @author kimgun-yeong
	 */
	public boolean existsByName(String name);
}
