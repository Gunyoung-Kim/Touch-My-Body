package com.gunyoung.tmb.services.domain.exercise;

import com.gunyoung.tmb.domain.exercise.ExerciseSpace;

public interface ExerciseSpaceService {
	public ExerciseSpace findById(Long id);
	
	public ExerciseSpace save(ExerciseSpace exerciseSpace);
	
	public void delete(ExerciseSpace exerciseSpace);
}
