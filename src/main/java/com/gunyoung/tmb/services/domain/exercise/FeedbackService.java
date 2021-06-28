package com.gunyoung.tmb.services.domain.exercise;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;

public interface FeedbackService {
	public Feedback findById(Long id);
	
	public Feedback save(Feedback feedback);
	public Feedback saveWithUserAndExercise(Feedback feedback,User user, Exercise exercise);
	
	public void delete(Feedback feedback);
}
