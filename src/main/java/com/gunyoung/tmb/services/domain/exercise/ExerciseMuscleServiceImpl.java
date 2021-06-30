package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Optional;

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

	/**
	 * @param id 찾으려는 ExerciseMuscle의 id
	 * @return ExerciseMuscle, Null(해당 id의 ExerciseMuscle없을때)
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
	 * @param exerciseMuscle 저장하려는 exerciseMuscle
	 * @return 저장된 ExerciseMuscle
	 * @author kimgun-yeong
	 */
	@Override
	public ExerciseMuscle save(ExerciseMuscle exerciseMuscle) {
		return exerciseMuscleRepository.save(exerciseMuscle);
	}
	
	/**
	 * @param exerciseMuscles 저장하려는 ExerciseMuscles
	 * @author kimgun-yeong
	 */
	@Override
	public List<ExerciseMuscle> saveAll(Iterable<ExerciseMuscle> exerciseMuscles) {
		return exerciseMuscleRepository.saveAll(exerciseMuscles);
	}
	
	
	/**
	 * @param exerciseMuscle 삭제하려는 ExerciseMuscle
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(ExerciseMuscle exerciseMuscle) {
		exerciseMuscleRepository.delete(exerciseMuscle);
	}
}
