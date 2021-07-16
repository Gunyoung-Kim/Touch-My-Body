package com.gunyoung.tmb.services.domain.user;

import java.util.Calendar;
import java.util.List;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.response.UserExerciseIsDoneDTO;

public interface UserExerciseService {
	public UserExercise findById(Long id);
	
	public List<UserExercise> findByUserIdAndDate(Long userId,Calendar date);
	public List<UserExerciseIsDoneDTO> findIsDoneDTOByUserIdAndYearAndMonth(Long userId, int year, int month);
	
	public UserExercise save(UserExercise userExercise);
	
	public void delete(UserExercise userExercise);
}
