package com.gunyoung.tmb.domain.like.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.testutil.PostLikeTest;

/**
* {@link PostLike} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) PostLike only
* @author kimgun-yeong
*
*/
class PostLikeUnitTest {
	
	/*
	 *  String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상")
	void toStringTest() {
		//Given
		Long postLikeId = Long.valueOf(42);
		PostLike postLike = PostLikeTest.getPostLikeInstance();
		postLike.setId(postLikeId);
		
		//When
		String result = postLike.toString();
		
		//Then
		verifyString_for_toStringTest(postLike, result);
	}
	
	private void verifyString_for_toStringTest(PostLike postLike, String result) {
		assertEquals("[ id = " + postLike.getId() + " ]", result);
	}
}
