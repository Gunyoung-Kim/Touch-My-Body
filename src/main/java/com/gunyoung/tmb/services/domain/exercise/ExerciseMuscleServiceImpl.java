package com.gunyoung.tmb.services.domain.exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.Muscle;
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
	@Transactional(readOnly=true)
	public ExerciseMuscle findById(Long id) {
		Optional<ExerciseMuscle> result = exerciseMuscleRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}

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
	public List<ExerciseMuscle> getExerciseMuscleListFromExerciseAndMuscleListAndIsMain(Exercise exercise, List<Muscle> muscleList,boolean isMain) {
		List<ExerciseMuscle> exerciseMuscleList = new ArrayList<>();
		for(Muscle muscle: muscleList) {
			ExerciseMuscle em = ExerciseMuscle.builder()
					.exercise(exercise)
					.muscleName(muscle.getName())
					.muscle(muscle)
					.isMain(isMain)
					.build();
			
			exerciseMuscleList.add(em);
		}
		
		return exerciseMuscleList; 
	}

}
