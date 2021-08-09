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

	/**
	 * ID로 Feedback 찾기
	 * @param id 찾으려는 Feedback 의 id
	 * @return Feedback, Null(해당 id의 Feedback이 없으면)
	 * @since 11
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
	 * ID로 Feedback 찾고 {@link FeedbackViewDTO} 생성 후 반환
	 * @param id 찾으려는 Feedback 의 id
	 * @return {@link FeedbackViewDTO}, null (해당 ID의 Feedback 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public FeedbackViewDTO findForFeedbackViewDTOById(Long id) {
		Optional<FeedbackViewDTO> result = feedbackRepository.findForFeedbackViewDTOById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}
	
	/**
	 * Exercise ID를 만족하는 Feedback들 페이지 반환
	 * @param exerciseId 찾으려는 Feedback들의 Exercise ID
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<Feedback> findAllByExerciseIdByPage(Long exerciseId, Integer pageNum, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		return feedbackRepository.findAllByExerciseIdByPage(exerciseId, pageRequest);
	}
	
	/**
	 * Exercise ID를 만족하는 Feedback 찾고 {@link FeedbackManageListDTO}들 생성해서 페이지 반환
	 * @param exerciseId 찾으려는 Feedback들의 Exercise ID
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<FeedbackManageListDTO> findAllForFeedbackManageListDTOByExerciseIdByPage(Long exerciseId,
			Integer pageNum, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		return feedbackRepository.findAllForFeedbackManageListDTOByExerciseIdByPage(exerciseId, pageRequest);
	}

	/**
	 * Feedback 생성 및 수정
	 * @param feedback 저장하려는 Feedback
	 * @return 저장된 Feedback
	 * @author kimgun-yeong
	 */
	@Override
	public Feedback save(Feedback feedback) {
		return feedbackRepository.save(feedback);
	}
	
	/**
	 * User와 Exercise객체를 인자로 받아 Feedback 객체 생성 및 연관 관계 설정 후 저장
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
	 * Feedback 삭제
	 * @param feedback 삭제하려는 Feedback
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(Feedback feedback) {	
		feedbackRepository.delete(feedback);
	}

	/**
	 * Exercise ID를 만족하는 Feedback들 개수 반환 
	 * @param exerciseId 찾으려는 Feedback들의 Exercise ID
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countByExerciseId(Long exerciseId) {
		return feedbackRepository.countByExerciseId(exerciseId);
	}
}
