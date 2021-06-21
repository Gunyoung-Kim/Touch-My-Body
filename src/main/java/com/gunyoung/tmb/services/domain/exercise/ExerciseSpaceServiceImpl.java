package com.gunyoung.tmb.services.domain.exercise;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExerciseSpace;
import com.gunyoung.tmb.repos.ExerciseSpaceRepository;

/**
 * ExerciseSpaceService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("exerciseSpaceService")
@Transactional
public class ExerciseSpaceServiceImpl implements ExerciseSpaceService {

	@Autowired
	ExerciseSpaceRepository exerciseSpaceRepository;
	
	/**
	 * @param id 찾으려는 ExerciseSpace의 id
	 * @return ExerciseSpace, Null(해당 id의 ExerciseSpace 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public ExerciseSpace findById(Long id) {
		Optional<ExerciseSpace> result = exerciseSpaceRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	/**
	 * @param exerciseSpace 저장하려는 ExerciseSpace
	 * @return 저장된 ExerciseSpace
	 * @author kimgun-yeong
	 */
	@Override
	public ExerciseSpace save(ExerciseSpace exerciseSpace) {
		return exerciseSpaceRepository.save(exerciseSpace);
	}

	/**
	 * @param exerciseSpace 삭제하려는 ExerciseSpace
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(ExerciseSpace exerciseSpace) {
		exerciseSpaceRepository.delete(exerciseSpace);	
	}

}
