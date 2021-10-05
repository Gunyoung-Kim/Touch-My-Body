package com.gunyoung.tmb.services.domain.exercise;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.FeedbackManageListDTO;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;

public interface FeedbackService {
	
	/**
	 * ID로 Feedback 찾기
	 * @param id 찾으려는 Feedback 의 id
	 * @return Feedback, Null(해당 id의 Feedback이 없으면)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public Feedback findById(Long id);
	
	/**
	 * ID로 Feedback 찾고 {@link FeedbackViewDTO} 생성 후 반환
	 * @param id 찾으려는 Feedback 의 id
	 * @return {@link FeedbackViewDTO}, null (해당 ID의 Feedback 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public FeedbackViewDTO findForFeedbackViewDTOById(Long id);
	
	/**
	 * Exercise ID를 만족하는 Feedback들 페이지 반환
	 * @param exerciseId 찾으려는 Feedback들의 Exercise ID
	 * @author kimgun-yeong
	 */
	public Page<Feedback> findAllByExerciseIdByPage(Long exerciseId, Integer pageNum, int pageSize);
	
	/**
	 * Exercise ID를 만족하는 Feedback 찾고 {@link FeedbackManageListDTO}들 생성해서 페이지 반환
	 * @param exerciseId 찾으려는 Feedback들의 Exercise ID
	 * @author kimgun-yeong
	 */
	public Page<FeedbackManageListDTO> findAllForFeedbackManageListDTOByExerciseIdByPage(Long exerciseId, Integer pageNum, int pageSize);
	
	/**
	 * Feedback 생성 및 수정
	 * @param feedback 저장하려는 Feedback
	 * @return 저장된 Feedback
	 * @author kimgun-yeong
	 */
	public Feedback save(Feedback feedback);
	
	/**
	 * User와 Exercise객체를 인자로 받아 Feedback 객체 생성 및 연관 관계 설정 후 저장
	 * @author kimgun-yeong
	 */
	public Feedback saveWithUserAndExercise(Feedback feedback, User user, Exercise exercise);
	
	/**
	 * Feedback 삭제
	 * @param feedback 삭제하려는 Feedback
	 * @author kimgun-yeong
	 */
	public void delete(Feedback feedback);
	
	/**
	 * User Id를 만족하는 Feedback들 일괄 삭제
	 * @param userId 삭제하려는 UserExercise들의 User ID
	 * @author kimgun-yeong
	 */
	public void deleteAllByUserId(Long userId);
	
	/**
	 * Exercise Id를 만족하는 Feedback들 일괄 삭
	 * @param exerciseId 삭제하려는 Feedback들의 Exercise ID
	 * @author kimgun-yeong
	 */
	public void deleteAllByExerciseId(Long exerciseId);
	
	/**
	 * Exercise ID를 만족하는 Feedback들 개수 반환 
	 * @param exerciseId 찾으려는 Feedback들의 Exercise ID
	 * @author kimgun-yeong
	 */
	public long countByExerciseId(Long exerciseId);
}
