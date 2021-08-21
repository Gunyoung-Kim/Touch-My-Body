package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.UserManageListDTO;
import com.gunyoung.tmb.util.UserTest;

/**
 * {@link UserManageListDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserManageListDTO only
 * @author kimgun-yeong
 *
 */
public class UserManageListDTOUnitTest {
	
	/*
	 * public static UserManageListDTO of(User user)
	 */
	
	@Test
	@DisplayName("User을 UserManagerListDTO객체로 변환 -> 정상")
	public void ofTest() {
		//Given
		Long userId = Long.valueOf(24);
		User user = UserTest.getUserInstance();
		user.setId(userId);
		
		//When
		UserManageListDTO result = UserManageListDTO.of(user);
		
		//Then
		assertEquals(userId, result.getUserId());
	}
}
