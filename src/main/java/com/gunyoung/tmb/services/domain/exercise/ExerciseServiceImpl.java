package com.gunyoung.tmb.services.domain.exercise;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.repos.ExerciseRepository;

/**
 * ExerciesService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("exerciseService")
@Transactional
public class ExerciseServiceImpl implements ExerciseService {
	
	@Autowired
	ExerciseRepository exerciseRepository;

	/**
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Exercise findById(Long id) {
		Optional<Exercise> result = exerciseRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * @param name 찾으려는 Exercise의 이름
	 * @return Exercise, Null(해당 name의 Exercise가 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Exercise findByName(String name) {
		Optional<Exercise> result = exerciseRepository.findByName(name);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	/**
	 * @param exercise 저장하려는 Exercise
	 * @return 저장된 Exercise
	 * @author kimgun-yeong
	 */
	@Override
	public Exercise save(Exercise exercise) {
		return exerciseRepository.save(exercise);
	}

	/**
	 * @param exercise 삭제하려는 Exercise
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(Exercise exercise) {
		exerciseRepository.delete(exercise);
	}
	
	
}
