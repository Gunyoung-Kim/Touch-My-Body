package com.gunyoung.tmb.services.domain.like;

import com.gunyoung.tmb.domain.exercise.ExercisePost;

public interface ExercisePostService {
	public ExercisePost findById(Long id);
	
	public ExercisePost save(ExercisePost exercisePost);
	
	public void delete(ExercisePost exercisePost);
}
