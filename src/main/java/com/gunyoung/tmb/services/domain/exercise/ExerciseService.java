package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Map;

import com.gunyoung.tmb.domain.exercise.Exercise;

public interface ExerciseService {
	public Exercise findById(Long id);
	public Exercise findByName(String name);
	public Map<String, List<String>> getAllExercisesNamewithSorting();
	
	public Exercise save(Exercise exercise);
	
	public void delete(Exercise exercise);
}
