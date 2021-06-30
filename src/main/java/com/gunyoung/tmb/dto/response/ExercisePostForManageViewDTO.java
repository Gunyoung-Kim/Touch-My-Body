package com.gunyoung.tmb.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 매니저 화면에 노출되는 유저의 게시글 목록 구성하기 위해 사용되는 객체
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExercisePostForManageViewDTO {
	private Long postId;
	private String title;
	private String writer;
	private LocalDateTime createdAt;
	private Integer viewNum;
}
