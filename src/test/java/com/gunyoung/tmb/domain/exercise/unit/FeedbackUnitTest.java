package com.gunyoung.tmb.domain.exercise.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.testutil.FeedbackTest;

/**
* {@link Feedback} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) Feedback only
* @author kimgun-yeong
*
*/
class FeedbackUnitTest {
	
	/*
	 *  String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상") 
	void toStringTest() {
		//Given
		Long feedbackId = Long.valueOf(81);
		Feedback feedback = FeedbackTest.getFeedbackInstance();
		feedback.setId(feedbackId);
		
		//When
		String result = feedback.toString();
		
		//Then
		verifyString_for_toStringTest(feedback, result);
	}
	
	private void verifyString_for_toStringTest(Feedback feedback, String result) {
		assertEquals("[ id = " + feedback.getId() + ", title = " + feedback.getTitle() + ", contents = " + feedback.getContents() + ", isReflected = " + feedback.isReflected() +" ]", result);
	}
}
