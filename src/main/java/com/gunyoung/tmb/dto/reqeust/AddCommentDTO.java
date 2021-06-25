package com.gunyoung.tmb.dto.reqeust;

import com.gunyoung.tmb.domain.exercise.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에서 유저의 게시글에 댓글 작성 요청 파라미터 바인딩 할때 사용
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCommentDTO {
	private String contents;
	private boolean isAnonymous;
	
	public static Comment toComment(AddCommentDTO dto,String writerIp) {
		Comment comment = Comment.builder()
				.writerIp(writerIp)
				.contents(dto.getContents())
				.isAnonymous(dto.isAnonymous())
				.build();
		
		return comment;
	}
}
