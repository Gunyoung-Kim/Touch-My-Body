package com.gunyoung.tmb.services.domain.exercise;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.FeedbackManageListDTO;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;
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

	@Override
	@Transactional(readOnly=true)
	public Feedback findById(Long id) {
		Optional<Feedback> result = feedbackRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public FeedbackViewDTO findForFeedbackViewDTOById(Long id) {
		Optional<FeedbackViewDTO> result = feedbackRepository.findForFeedbackViewDTOById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Feedback> findAllByExerciseIdByPage(Long exerciseId, Integer pageNum, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		return feedbackRepository.findAllByExerciseIdByPage(exerciseId, pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<FeedbackManageListDTO> findAllForFeedbackManageListDTOByExerciseIdByPage(Long exerciseId,
			Integer pageNum, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		return feedbackRepository.findAllForFeedbackManageListDTOByExerciseIdByPage(exerciseId, pageRequest);
	}

	@Override
	public Feedback save(Feedback feedback) {
		return feedbackRepository.save(feedback);
	}
	
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

	@Override
	public void delete(Feedback feedback) {	
		feedbackRepository.delete(feedback);
	}
	
	@Override
	public void deleteAllByUserId(Long userId) {
		feedbackRepository.deleteAllByUserIdInQuery(userId);
	}
	
	@Override
	public void deleteAllByExerciseId(Long exerciseId) {
		feedbackRepository.deleteAllByExerciseIdInQuery(exerciseId);
	}
	@Override
	@Transactional(readOnly=true)
	public long countByExerciseId(Long exerciseId) {
		return feedbackRepository.countByExerciseId(exerciseId);
	}
}
