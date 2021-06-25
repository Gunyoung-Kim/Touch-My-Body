package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Map;

import com.gunyoung.tmb.domain.exercise.Muscle;

public interface MuscleService {
	public Muscle findById(Long id);
	public Muscle findByName(String name);
	public Map<String,List<String>> getAllMusclesWithSortingByCategory();
	
	public Muscle save(Muscle muscle);
	
	public void delete(Muscle muscle);
}
