package com.gunyoung.tmb.services.domain.like;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.repos.ExercisePostRepository;

/**
 * ExercisePostService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("exercisePostService")
@Transactional
public class ExercisePostServiceImpl implements ExercisePostService {

	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	/**
	 * @param id 찾으려는 ExerciePost id 값
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public ExercisePost findById(Long id) {
		Optional<ExercisePost> result = exercisePostRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	/**
	 * @param exercisePost 저장하려는 ExercisePost
	 * @return 저장된 ExercisePost
	 * @author kimgun-yeong
	 */
	@Override
	public ExercisePost save(ExercisePost exercisePost) {
		return exercisePostRepository.save(exercisePost);
	}

	/**
	 * @param exercisePost 삭제하려는 ExercisePost
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(ExercisePost exercisePost) {
		exercisePostRepository.delete(exercisePost);
	}
	
}
