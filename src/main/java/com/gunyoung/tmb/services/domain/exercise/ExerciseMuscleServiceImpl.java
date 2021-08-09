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

	/**
	 * ID로 ExerciseMuscle 찾기
	 * @param id 찾으려는 ExerciseMuscle의 id
	 * @return ExerciseMuscle, Null(해당 id의 ExerciseMuscle없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public ExerciseMuscle findById(Long id) {
		Optional<ExerciseMuscle> result = exerciseMuscleRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	/**
	 * ExerciseMuscle 생성 및 수정
	 * @param exerciseMuscle 저장하려는 exerciseMuscle
	 * @return 저장된 ExerciseMuscle
	 * @author kimgun-yeong
	 */
	@Override
	public ExerciseMuscle save(ExerciseMuscle exerciseMuscle) {
		return exerciseMuscleRepository.save(exerciseMuscle);
	}
	
	/**
	 * 다수의 ExerciseMuscle 생성 및 수정
	 * @param exerciseMuscles 저장하려는 ExerciseMuscles
	 * @author kimgun-yeong
	 */
	@Override
	public List<ExerciseMuscle> saveAll(Iterable<ExerciseMuscle> exerciseMuscles) {
		return exerciseMuscleRepository.saveAll(exerciseMuscles);
	}
	
	
	/**
	 * ExerciseMuscle 삭제
	 * @param exerciseMuscle 삭제하려는 ExerciseMuscle
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(ExerciseMuscle exerciseMuscle) {
		exerciseMuscleRepository.delete(exerciseMuscle);
	}

	/**
	 * Exercise, Muscle List, isMain 인자를 이용해 ExerciseMuscle List 생성 후 반환
	 * @param exercise 생성 하려는 ExerciseMuscle 들의 Exercise
	 * @param muscleList 생성 하려는 ExerciseMuscle 들의 Muscle 들
	 * @param isMain 생성 하려는 ExerciseMuscle 들의 isMain (주 자극 근육인지 여부)
	 * @return
	 * @author kimgun-yeong
	 */
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
