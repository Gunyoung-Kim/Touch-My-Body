package com.gunyoung.tmb.util;

import com.gunyoung.tmb.domain.like.CommentLike;

/**
 * Test 클래스 전용 CommentLike 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class CommentLikeTest {
	
	/**
	 * 테스트용 CommentLike 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static CommentLike getCommentLikeInstance() {
		CommentLike pl = CommentLike.builder()
				.build();
		return pl;
	}
}