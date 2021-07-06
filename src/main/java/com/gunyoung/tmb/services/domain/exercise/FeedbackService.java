package com.gunyoung.tmb.services.domain.exercise;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.FeedbackManageListDTO;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;

public interface FeedbackService {
	public Feedback findById(Long id);
	public FeedbackViewDTO findForFeedbackViewDTOById(Long id);
	
	public Page<Feedback> findAllByExerciseIdByPage(Long exerciseId, Integer pageNum, int pageSize);
	public Page<FeedbackManageListDTO> findAllForFeedbackManageListDTOByExerciseIdByPage(Long exerciseId,Integer pageNum, int pageSize);
	
	public Feedback save(Feedback feedback);
	public Feedback saveWithUserAndExercise(Feedback feedback,User user, Exercise exercise);
	
	public void delete(Feedback feedback);
	
	public long countByExerciseId(Long exerciseId);
}
