package com.gunyoung.tmb.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;

/**
 *  Test 클래스 전용 Feedback 엔티티 관련 유틸리티 클래스
 *  @author kimgun-yeong
 */
public class FeedbackTest {
	
	/**
	 * 테스트용 Feedback 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static Feedback getFeedbackInstance() {
		Feedback feedback = Feedback.builder()
				.contents("contents")
				.title("title")
				.build();
		
		return feedback;
	}
	
	/**
	 * Repository를 통해 존재하지 않는 Feedback ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistFeedbackId(JpaRepository<Feedback, Long> feedbackRepository) {
		Long nonExistFeedbackId = Long.valueOf(1);
		
		for(Feedback u : feedbackRepository.findAll()) {
			nonExistFeedbackId = Math.max(nonExistFeedbackId, u.getId());
		}
		nonExistFeedbackId++;
		
		return nonExistFeedbackId;
	}
	
	/**
	 * Repository를 사용해 DB에 인자로 전해진 num 만큼 Feedback 생성 후 저장
	 * @author kimgun-yeong
	 */
	public static List<Feedback> addNewFeedbacksInDBByNum(int num, JpaRepository<Feedback, Long> feedbackRepository) {
		List<Feedback> newFeedbacks = new ArrayList<>();
		for(int i=0;i < num;i++) {
			Feedback newFeedback = getFeedbackInstance();
			newFeedbacks.add(newFeedback);
		}
		return feedbackRepository.saveAll(newFeedbacks);
	}
	
	/**
	 * 테스트용 {@link com.gunyoung.tmb.dto.response.FeedbackViewDTO} 인스턴스 반환 
	 * @author kimgun-yeong
	 */
	public static FeedbackViewDTO getFeedbackViewDTOInstance() {
		Exercise exercise = ExerciseTest.getExerciseInstance();
		User user = UserTest.getUserInstance();
		return getFeedbackViewDTOInstance(exercise, user);
	}
	
	/**
	 * 테스트용 {@link com.gunyoung.tmb.dto.response.FeedbackViewDTO} 인스턴스 반환 <br>
	 * Exercise, User를 통해 ExerciseName, UserNickName 커스터마이징 가능
	 * @author kimgun-yeong 
	 */
	public static FeedbackViewDTO getFeedbackViewDTOInstance(Exercise exercise, User user) {
		FeedbackViewDTO feedbackViewDTO = FeedbackViewDTO.builder()
				.contents("contents")
				.title("title")
				.exerciseName(exercise.getName())
				.userNickName(user.getNickName())
				.build();
		
		return feedbackViewDTO;
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.SaveFeedbackDTO} 에 바인딩 될 수 있는 MultiValueMap 반환
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String, String> getSaveFeedbackDTOMap() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", "title");
		map.add("contents", "contents");
		return map;
	}
}
