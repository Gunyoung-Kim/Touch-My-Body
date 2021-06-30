package com.gunyoung.tmb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 매니저 화면에 노출되는 유저의 댓글 목록 구성하기 위해 사용되는 객체
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentForManageViewDTO {
	private Long commentId;
	private String userName;
	private String writerIp;
	private String contents;
}
