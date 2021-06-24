package com.gunyoung.tmb.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.jpa.ExerciseSortDTO;

public interface ExerciseRepository extends JpaRepository<Exercise,Long>{
	public Optional<Exercise> findByName(String name);
	
	/**
	 * 모든 운동 정보들을 ExerciesSortDTO로 매핑해서 가져오는 메소드 
	 * @return
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.jpa.ExerciseSortDTO(e.name, e.target) from Exercise e")
	public List<ExerciseSortDTO> findAllWithNameAndTarget();
}
