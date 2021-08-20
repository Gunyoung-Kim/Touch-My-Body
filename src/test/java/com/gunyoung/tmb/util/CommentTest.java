package com.gunyoung.tmb.util;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.user.User;

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
	 * Repository를 통해 존재하지 않는 Comment ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistCommentId(JpaRepository<Comment, Long> commentRepository) {
		Long nonExistId = Long.valueOf(1);
		
		for(Comment c: commentRepository.findAll()) {
			nonExistId = Math.max(nonExistId, c.getId());
		}
		nonExistId++;
		
		return nonExistId;
	}
	
	/**
	 * 주어진 개수만큼 Comment 생성 후 User와 연관 관계 설정 후 Repository를 이용하여 저장
	 * @author kimgun-yeong
	 */
	public static void addCommentsInDBWithSettingUser(int commentNum, User user, JpaRepository<Comment, Long> commentRepository) {
		List<Comment> commentList = new LinkedList<>();
		for(int i= 0 ; i<commentNum ; i++) {
			Comment comment = getCommentInstance();
			comment.setUser(user);
			commentList.add(comment);
		}
		commentRepository.saveAll(commentList);
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
