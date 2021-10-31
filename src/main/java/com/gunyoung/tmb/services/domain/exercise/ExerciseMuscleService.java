package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;

public interface ExerciseMuscleService {
	
	/**
	 * ExerciseMuscle 생성 및 수정
	 * @param exerciseMuscle 저장하려는 exerciseMuscle
	 * @return 저장된 ExerciseMuscle
	 * @author kimgun-yeong
	 */
	public ExerciseMuscle save(ExerciseMuscle exerciseMuscle);
	
	/**
	 * 다수의 ExerciseMuscle 생성 및 수정
	 * @param exerciseMuscles 저장하려는 ExerciseMuscles
	 * @author kimgun-yeong
	 */
	public List<ExerciseMuscle> saveAll(Iterable<ExerciseMuscle> exerciseMuscles);
	
	/**
	 * ExerciseMuscle 삭제
	 * @param exerciseMuscle 삭제하려는 ExerciseMuscle
	 * @author kimgun-yeong
	 */
	public void delete(ExerciseMuscle exerciseMuscle);
	
	/**
	 * Muscle ID를 통한 ExerciseMuscle 모두 삭제
	 * @param muscleId 삭제하려는 ExerciseMuscle들의 Muscle의 ID
	 * @author kimgun-yeong
	 */
	public void deleteAllByMuscleId(Long muscleId);
	
	/**
	 * Exercise ID를 통한 ExerciseMuscle 모두 삭제
	 * @param exerciseId 삭제하려는 ExerciseMuscle들이 Exercise ID
	 * @author kimgun-yeong
	 */
	public void deleteAllByExerciseId(Long exerciseId);
}
