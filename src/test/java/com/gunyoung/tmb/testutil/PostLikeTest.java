package com.gunyoung.tmb.testutil;

import org.springframework.data.jpa.repository.JpaRepository;

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
	
	/**
	 * Repository를 통해 존재하지 않는 PostLike ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistPostLikeId(JpaRepository<PostLike, Long> postLikeRepository) {
		Long nonExistPostLikeId = Long.valueOf(1);
		
		for(PostLike u : postLikeRepository.findAll()) {
			nonExistPostLikeId = Math.max(nonExistPostLikeId, u.getId());
		}
		nonExistPostLikeId++;
		
		return nonExistPostLikeId;
	}
	
}
