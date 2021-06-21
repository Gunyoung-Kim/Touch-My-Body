package com.gunyoung.tmb.services.domain.exercise;

import com.gunyoung.tmb.domain.exercise.Feedback;

public interface FeedbackService {
	public Feedback findById(Long id);
	
	public Feedback save(Feedback feedback);
	
	public void delete(Feedback feedback);
}
