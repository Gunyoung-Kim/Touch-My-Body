package com.gunyoung.tmb.services.domain.exercise;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.repos.FeedbackRepository;

import lombok.RequiredArgsConstructor;

/**
 * FeedbackService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("feedbackService")
@Transactional
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
	
	private final FeedbackRepository feedbackRepository;

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
	 * User와 Exercise객체를 인자로 받아 Feedback 객체 생성 및 객체적 관계 연결 후 저장
	 * @author kimgun-yeong
	 */
	@Override
	public Feedback saveWithUserAndExercise(Feedback feedback,User user, Exercise exercise) {
		// Feedback의 객체 연관 관계 추가
		feedback.setExercise(exercise);
		feedback.setUser(user);
		
		//User와 Exercise의 객체 연관 관계 추가
		user.getFeedbacks().add(feedback);
		exercise.getFeedbacks().add(feedback);
		
		//Feedback의 DB연관관계 추가 및 객체 저장 
		return save(feedback);
	}

	/**
	 * @param feedback 삭제하려는 Feedback
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(Feedback feedback) {
		User user = feedback.getUser();
		if(user != null) {
			user.getFeedbacks().remove(feedback);
		}
		
		Exercise exercise = feedback.getExercise();
		if(exercise != null) {
			exercise.getFeedbacks().remove(feedback);
		}
		
		feedbackRepository.delete(feedback);
	}
}
