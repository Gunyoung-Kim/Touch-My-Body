package com.gunyoung.tmb.domain.user.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.testutil.UserTest;

/**
* {@link User} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) User only
* @author kimgun-yeong
*
*/
class UserUnitTest {
	
	/*
	 *  String toString()
	 */
	
	@Test
	@DisplayName("toString test -> 정상")
	void toStringTest() {
		//Given
		Long userId = Long.valueOf(35);
		User user = UserTest.getUserInstance();
		user.setId(userId);
		
		//When
		String result = user.toString();
		
		//Then
		verifyString_for_toStringTest(user, result);
	}
	
	private void verifyString_for_toStringTest(User user, String result) {
		assertEquals("[ id = " + user.getId() + ", email = " + user.getEmail() + ", firstName = " + user.getFirstName() + ", lastName = " + user.getLastName() 
				+ ", nickName = " + user.getNickName() + ", role = " + user.getRole() + " ]", result);
	}
}
