package com.gunyoung.tmb.services.domain.exercise;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.repos.FeedbackRepository;

/**
 * FeedbackService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("feedbackService")
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
	
	@Autowired
	FeedbackRepository feedbackRepository;

	/**
	 * @param id 찾으려는 Feedback 의 id
	 * @return Feedback, Null(해당 id의 Feedback이 없으면)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Feedback findById(Long id) {
		Optional<Feedback> result = feedbackRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	/**
	 * @param feedback 저장하려는 Feedback
	 * @return 저장된 Feedback
	 * @author kimgun-yeong
	 */
	@Override
	public Feedback save(Feedback feedback) {
		return feedbackRepository.save(feedback);
	}

	/**
	 * @param feedback 삭제하려는 Feedback
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(Feedback feedback) {
		feedbackRepository.delete(feedback);
	}
}
