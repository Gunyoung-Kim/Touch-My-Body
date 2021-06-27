package com.gunyoung.tmb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 운동 게시글 화면을 구성하기 위해 클라이언트에게 응답할때 사용하는 객체
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExercisePostViewDTO {
	private Long postId;
	private String title;
	private String exerciseName;
	private String writerName;
	private String contents;
	private Integer viewNum;
	private Integer likeNum;
	private Integer commentNum;
	private String createdAt;
}
