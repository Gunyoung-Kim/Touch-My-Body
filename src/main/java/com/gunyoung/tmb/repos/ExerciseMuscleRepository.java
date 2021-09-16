package com.gunyoung.tmb.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;

public interface ExerciseMuscleRepository extends JpaRepository<ExerciseMuscle,Long>{
	
	/**
	 * Muscle Id를 만족하는 ExerciseMuscle들 일괄 삭제
	 * @param muscleId 삭제하려는 ExerciseMuscle들의 Muscle ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM ExerciseMuscle em "
			+ "WHERE em.muscle.id = :muscleId")
	public void deleteAllByMuscleIdInQuery(@Param("muscleId") Long muscleId);
	
	/**
	 * Exercise ID를 만족하는 ExerciseMuscle들 일괄 삭제
	 * @param exerciseId 삭제하려는 ExerciseMuscle들의 Exercise ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM ExerciseMuscle em "
			+ "WHERE em.exercise.id = :exerciseId")
	public void deleteAllByExerciseIdInQuery(@Param("exerciseId") Long exerciseId);
}
