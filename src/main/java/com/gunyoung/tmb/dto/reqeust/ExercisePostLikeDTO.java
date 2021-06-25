package com.gunyoung.tmb.dto.reqeust;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에서 운동 게시글에 좋아요 추가 또는 삭제하기 위해 요청 보낼때 사용
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExercisePostLikeDTO {
	private Long exercisePostId;
}
