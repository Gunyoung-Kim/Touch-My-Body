package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.Muscle;

public interface ExerciseMuscleService {
	
	/**
	 * ID로 ExerciseMuscle 찾기
	 * @param id 찾으려는 ExerciseMuscle의 id
	 * @return ExerciseMuscle, Null(해당 id의 ExerciseMuscle없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public ExerciseMuscle findById(Long id);
	
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
	 * @param muscleId 삭제하려는 ExerciseMuscle들의 연관 Muscle의 ID
	 * @author kimgun-yeong
	 */
	public void deleteAllByMuscleId(Long muscleId);
	
	/**
	 * Exercise, Muscle List, isMain 인자를 이용해 ExerciseMuscle List 생성 후 반환
	 * @param exercise 생성 하려는 ExerciseMuscle 들의 Exercise
	 * @param muscleList 생성 하려는 ExerciseMuscle 들의 Muscle 들
	 * @param isMain 생성 하려는 ExerciseMuscle 들의 isMain (주 자극 근육인지 여부)
	 * @author kimgun-yeong
	 */
	public List<ExerciseMuscle> getExerciseMuscleListFromExerciseAndMuscleListAndIsMain(Exercise exercise, List<Muscle> muscleList,boolean isMain);
}
