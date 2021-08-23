package com.gunyoung.tmb.dto.reqeust;

import com.gunyoung.tmb.domain.exercise.Feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 유저가 운동정보에 대한 피드백 추가할때 사용하는 객체
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveFeedbackDTO {
	private String title;
	private String contents;
	
	/**
	 * 이 객체를 통해 Feedback 생성 후 반환
	 * @author kimgun-yeong
	 */
	public Feedback createFeedback() {
		Feedback feedback = Feedback.builder()
				.title(this.title)
				.contents(this.contents)
				.build();
		return feedback;
	}
}
