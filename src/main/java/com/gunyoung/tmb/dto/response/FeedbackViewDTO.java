package com.gunyoung.tmb.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 피드백 정보 화면에 사용되는 객체
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackViewDTO {
	private Long id;
	private String title;
	private String contents;
	private String userNickName;
	private String exerciseName;
	private LocalDateTime createdAt;
}
