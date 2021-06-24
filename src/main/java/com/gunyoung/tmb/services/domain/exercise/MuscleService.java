package com.gunyoung.tmb.services.domain.exercise;

import com.gunyoung.tmb.domain.exercise.Muscle;

public interface MuscleService {
	public Muscle findById(Long id);
	public Muscle findByName(String name);
	
	public Muscle save(Muscle muscle);
	
	public void delete(Muscle muscle);
}
