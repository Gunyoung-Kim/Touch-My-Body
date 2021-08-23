package com.gunyoung.tmb.dto.reqeust.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.dto.reqeust.SaveFeedbackDTO;

/**
 * {@link SaveFeedbackDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SaveFeedbackDTO only
 * @author kimgun-yeong
 *
 */
public class SaveFeedbackDTOUnitTest {
	
	/*
	 * public Feedback createFeedback()
	 */
	
	@Test
	@DisplayName("이 객체를 통해 Feedback 생성 후 반환 -> 정상, Feedback field check")
	public void createFeedbackTestCheckField() {
		//Given
		SaveFeedbackDTO saveFeedbackDTO = SaveFeedbackDTO.builder()
				.title("title")
				.contents("contents")
				.build();
		
		//When
		Feedback result = saveFeedbackDTO.createFeedback();
		
		//Then
		verifyFeedbackField(saveFeedbackDTO, result);
	}
	
	private void verifyFeedbackField(SaveFeedbackDTO saveFeedbackDTO, Feedback feedback) {
		assertEquals(saveFeedbackDTO.getTitle(), feedback.getTitle());
		assertEquals(saveFeedbackDTO.getContents(), feedback.getContents());
	}
}
