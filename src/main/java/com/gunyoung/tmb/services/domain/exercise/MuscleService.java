package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Muscle;

public interface MuscleService {
	public Muscle findById(Long id);
	public Muscle findByName(String name);
	
	public Page<Muscle> findAllInPage(Integer pageNumber, int pageSize);
	public Page<Muscle> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize);
	
	public Map<String,List<String>> getAllMusclesWithSortingByCategory();
	
	public Muscle save(Muscle muscle);
	
	public void delete(Muscle muscle);
	public void deleteById(Long id);
	
	public long countAll();
	public long countAllWithNameKeyword(String keyword);
}
