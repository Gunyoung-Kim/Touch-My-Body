package com.gunyoung.tmb.util;

import com.gunyoung.tmb.domain.like.PostLike;

/**
 * Test 클래스 전용 PostLike 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class PostLikeTest {
	
	/**
	 * 테스트용 PostLike 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static PostLike getPostLikeInstance() {
		PostLike pl = PostLike.builder()
				.build();
		return pl;
	}
}
