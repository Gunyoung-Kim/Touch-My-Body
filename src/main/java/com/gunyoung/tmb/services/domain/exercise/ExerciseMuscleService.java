package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;

public interface ExerciseMuscleService {
	public ExerciseMuscle findById(Long id);
	
	public ExerciseMuscle save(ExerciseMuscle exerciseMuscle);
	public List<ExerciseMuscle> saveAll(Iterable<ExerciseMuscle> exerciseMuscles);
	
	public void delete(ExerciseMuscle exerciseMuscle);
}
