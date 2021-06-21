package com.gunyoung.tmb.services.domain.exercise;

import com.gunyoung.tmb.domain.exercise.Exercise;

public interface ExerciseService {
	public Exercise findById(Long id);
	
	public Exercise save(Exercise exercise);
	
	public void delete(Exercise exercise);
}
