package com.gunyoung.tmb.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Comment;

/**
 * Test 클래스 전용 Comment 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 */
public class CommentTest {
	
	/**
	 * 테스트용 Comment 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static Comment getCommentInstance() {
		Comment comment = Comment.builder()
				.contents("contents")
				.isAnonymous(false)
				.writerIp("127.0.0.1")
				.build();
		return comment;
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.SaveCommentDTO} 에 바인딩 될 수 있는 MultiValueMap 반환
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String, String> getSaveCommentDTOMap() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("contents", "contents");
		map.add("isAnonymous", "false");
		return map;
	}
}
