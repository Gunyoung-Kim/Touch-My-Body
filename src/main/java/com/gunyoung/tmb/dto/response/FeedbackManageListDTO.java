package com.gunyoung.tmb.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 피드백 리스트 화면 구성하기 위해 사용되는 객체
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackManageListDTO {
	private Long id;
	private String title;
	private String userNickName;
	private String exerciseName;
	private LocalDateTime createdAt;
}
