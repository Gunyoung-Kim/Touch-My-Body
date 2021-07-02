package com.gunyoung.tmb.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 화면에 보여질 댓글들을 위해 사용되는 객체
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentForPostViewDTO {
	private Long commentId;
	private String writerIp;
	private String contents;
	private boolean isAnonymous;
	private String writerName;
	private LocalDateTime createdAt;
	private long commentLikesNum;
}
