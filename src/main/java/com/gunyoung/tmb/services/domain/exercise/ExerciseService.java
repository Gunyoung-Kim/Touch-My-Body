package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Map;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.AddExerciseDTO;

public interface ExerciseService {
	public Exercise findById(Long id);
	public Exercise findByName(String name);
	public Map<String, List<String>> getAllExercisesNamewithSorting();
	
	public Exercise save(Exercise exercise);
	public Exercise saveWithAddExerciseDTO(AddExerciseDTO dto);
	
	public void delete(Exercise exercise);
	
	
}
