package com.gunyoung.tmb.services.domain.user;

import com.gunyoung.tmb.domain.user.UserExercise;

public interface UserExerciseService {
	public UserExercise findById(Long id);
	
	public UserExercise save(UserExercise userExercise);
	
	public void delete(UserExercise userExercise);
}
