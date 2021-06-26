package com.gunyoung.tmb.dto.response;

import java.time.LocalDateTime;

import com.gunyoung.tmb.enums.TargetType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 커뮤니티에서 게시글 리스트 보여주기 위해 클라이언트에게 응답보낼때 사용하는 객체
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostForCommunityViewDTO {
	private Long postId;
	private TargetType category;
	private String title;
	private String writer;
	private LocalDateTime createdAt;
	private Integer viewNum;
}
