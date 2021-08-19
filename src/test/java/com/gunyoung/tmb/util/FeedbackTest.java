package com.gunyoung.tmb.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Feedback;

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
