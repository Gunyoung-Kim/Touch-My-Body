package com.gunyoung.tmb.services.domain.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.repos.UserExerciseRepository;

/**
 * UserExerciseService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("userExerciseService")
@Transactional
public class UserExerciseServiceImpl implements UserExerciseService {

	@Autowired
	UserExerciseRepository userExerciseRepository;
	
	/**
	 * @param id 찾으려는 UserExercise의 id값
	 * @return UserExercise, null(해당 id의 UserExercise 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public UserExercise findById(Long id) {
		Optional<UserExercise> result = userExerciseRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * @param userExercise 저장하려는 UserExercise
	 * @return 저장된 결과
	 * @author kimgun-yeong
	 */
	@Override
	public UserExercise save(UserExercise userExercise) {
		return userExerciseRepository.save(userExercise);
	}

	/**
	 * @param userExercise 삭제하려는 UserExercise
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(UserExercise userExercise) {
		userExerciseRepository.delete(userExercise);
	}
	
}
