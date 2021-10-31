package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.repos.ExerciseMuscleRepository;

import lombok.RequiredArgsConstructor;

/**
 * ExerciseMuscleService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("exerciseMuscleService")
@Transactional
@RequiredArgsConstructor
public class ExerciseMuscleServiceImpl implements ExerciseMuscleService {
	
	private final ExerciseMuscleRepository exerciseMuscleRepository;

	@Override
	public ExerciseMuscle save(ExerciseMuscle exerciseMuscle) {
		return exerciseMuscleRepository.save(exerciseMuscle);
	}
	
	@Override
	public List<ExerciseMuscle> saveAll(Iterable<ExerciseMuscle> exerciseMuscles) {
		return exerciseMuscleRepository.saveAll(exerciseMuscles);
	}
	
	@Override
	public void delete(ExerciseMuscle exerciseMuscle) {
		exerciseMuscleRepository.delete(exerciseMuscle);
	}
	
	@Override
	public void deleteAllByMuscleId(Long muscleId) {
		exerciseMuscleRepository.deleteAllByMuscleIdInQuery(muscleId);
	}
	
	@Override
	public void deleteAllByExerciseId(Long exerciseId) {
		exerciseMuscleRepository.deleteAllByExerciseIdInQuery(exerciseId);
	}
}
