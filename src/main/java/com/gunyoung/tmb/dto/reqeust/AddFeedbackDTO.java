package com.gunyoung.tmb.dto.reqeust;

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
public class AddFeedbackDTO {
	private String title;
	private String contents;
}
