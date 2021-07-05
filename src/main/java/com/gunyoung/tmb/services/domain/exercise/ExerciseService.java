package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.AddExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;

public interface ExerciseService {
	public Exercise findById(Long id);
	public Exercise findByName(String name);
	public Exercise findWithFeedbacksById(Long id);
	public Exercise findWithExercisePostsByName(String name);
	
	public Page<Exercise> findAllInPage(Integer pageNumber,int page_size);
	public Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber,int page_size);
	
	public Map<String, List<String>> getAllExercisesNamewithSorting();
	
	public Exercise save(Exercise exercise);
	public Exercise saveWithAddExerciseDTO(AddExerciseDTO dto);
	
	public void delete(Exercise exercise);
	public void deleteById(Long id);
	
	public long countAll();
	public long countAllWithNameKeyword(String nameKeyword);
	
	public ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId);
}
