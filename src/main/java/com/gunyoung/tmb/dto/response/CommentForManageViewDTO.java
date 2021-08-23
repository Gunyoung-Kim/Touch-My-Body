package com.gunyoung.tmb.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.user.User;

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
	
	/**
	 * Comment들과 User를 통해 CommentForManageViewDTO 리스트 반환
	 * @author kimgun-yeong
	 */
	public static List<CommentForManageViewDTO> of(Iterable<Comment> comments, User user) {
		List<CommentForManageViewDTO> dtos = new ArrayList<>();
		for(Comment c: comments) {
			CommentForManageViewDTO dto = CommentForManageViewDTO.builder()
					.commentId(c.getId())
					.userName(user.getNickName())
					.writerIp(c.getWriterIp())
					.contents(c.getContents())
					.build();
			
			dtos.add(dto);
		}
		return dtos;
	}
}
