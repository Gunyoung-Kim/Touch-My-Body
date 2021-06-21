package com.gunyoung.tmb.services.domain.exercise;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;

public interface ExerciseMuscleService {
	public ExerciseMuscle findById(Long id);
	
	public ExerciseMuscle save(ExerciseMuscle exerciseMuscle);
	
	public void delete(ExerciseMuscle exerciseMuscle);
}
