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
import com.gunyoung.tmb.error.codes.FeedbackErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.FeedbackNotFoundedException;
import com.gunyoung.tmb.precondition.Preconditions;
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
		return result.orElseThrow(() -> new FeedbackNotFoundedException(FeedbackErrorCode.FEEDBACK_NOT_FOUNDED_ERROR.getDescription()));
	}
	
	@Override
	@Transactional(readOnly=true)
	public FeedbackViewDTO findForFeedbackViewDTOById(Long id) {
		Optional<FeedbackViewDTO> result = feedbackRepository.findForFeedbackViewDTOById(id);
		return result.orElseThrow(() -> new FeedbackNotFoundedException(FeedbackErrorCode.FEEDBACK_NOT_FOUNDED_ERROR.getDescription()));
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Feedback> findAllByExerciseIdByPage(Long exerciseId, Integer pageNum, int pageSize) {
		checkParameterForPageRequest(pageNum, pageSize);
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		return feedbackRepository.findAllByExerciseIdByPage(exerciseId, pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<FeedbackManageListDTO> findAllForFeedbackManageListDTOByExerciseIdByPage(Long exerciseId,
			Integer pageNum, int pageSize) {
		checkParameterForPageRequest(pageNum, pageSize);
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		return feedbackRepository.findAllForFeedbackManageListDTOByExerciseIdByPage(exerciseId, pageRequest);
	}
	
	private void checkParameterForPageRequest(Integer pageNumber, int pageSize) {
		Preconditions.notLessThan(pageNumber, 1, "pageNumber should be equal or greater than 1");
		Preconditions.notLessThanInt(pageSize, 1, "pageSize should be equal or greater than 1");
	}

	@Override
	public Feedback save(Feedback feedback) {
		return feedbackRepository.save(feedback);
	}
	
	@Override
	public Feedback saveWithUserAndExercise(Feedback feedback, User user, Exercise exercise) {
		Preconditions.notNull(feedback, "Given feedback must be not null");
		Preconditions.notNull(user, "Given user must be not null");
		Preconditions.notNull(exercise, "Given exercise must be not null");
		
		addRelationBetweenFeedbackAndUser(feedback, user);
		addRelationBetweenFeedbackAndExercise(feedback, exercise);
		return save(feedback);
	}
	
	private void addRelationBetweenFeedbackAndUser(Feedback feedback, User user) {
		feedback.setUser(user);
		user.getFeedbacks().add(feedback);
	}
	
	private void addRelationBetweenFeedbackAndExercise(Feedback feedback, Exercise exercise) {
		feedback.setExercise(exercise);
		exercise.getFeedbacks().add(feedback);
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
