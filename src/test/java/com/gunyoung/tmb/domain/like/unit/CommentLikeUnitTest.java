package com.gunyoung.tmb.domain.like.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.testutil.CommentLikeTest;

/**
* {@link CommentLike} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) CommentLike only
* @author kimgun-yeong
*
*/
class CommentLikeUnitTest {
	/*
	 *  String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상")
	void toStringTest() {
		//Given
		Long commentLikeId = Long.valueOf(42);
		CommentLike commentLike = CommentLikeTest.getCommentLikeInstance();
		commentLike.setId(commentLikeId);
		
		//When
		String result = commentLike.toString();
		
		//Then
		verifyString_for_toStringTest(commentLike, result);
	}
	
	private void verifyString_for_toStringTest(CommentLike commentLike, String result) {
		assertEquals("[ id = " + commentLike.getId() + " ]", result);
	}
}
