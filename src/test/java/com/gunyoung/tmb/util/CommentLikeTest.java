package com.gunyoung.tmb.util;

import org.springframework.data.jpa.repository.JpaRepository;

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
	
	/**
	 * Repository를 통해 존재하지 않는 CommentLike ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistCommentLikeId(JpaRepository<CommentLike, Long> commentLikeRepository) {
		Long nonExistId = Long.valueOf(1);
		
		for(CommentLike cl : commentLikeRepository.findAll()) {
			nonExistId = Math.max(nonExistId, cl.getId());
		}
		
		nonExistId++;
		return nonExistId;
	}
}