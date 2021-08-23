package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
		verifyUserManageListDTOWithUser(user, result);
	}
	
	private void verifyUserManageListDTOWithUser(User user, UserManageListDTO result) {
		assertEquals(user.getId(), result.getUserId());
		assertEquals(user.getFullName(), result.getName());
		assertEquals(user.getEmail(), result.getEmail());
		assertEquals(user.getNickName(), result.getNickName());
		assertEquals(user.getRole().getKoreanName(), result.getRole());
	}
	
	/*
	 * public static List<UserManageListDTO> of(Iterable<User> users)
	 */
	
	@Test
	@DisplayName("User 컬렉션을 통해 UserManageListDTO 리스트 반환 -> 정상")
	public void ofListTest() {
		//Given
		List<User> users = new ArrayList<>();
		int givenUserNum = 29;
		for(int i=0; i < givenUserNum ; i++) {
			User user = UserTest.getUserInstance();
			users.add(user);
		}
		
		//When
		List<UserManageListDTO> result = UserManageListDTO.of(users);
		
		//Then
		assertEquals(givenUserNum, result.size());
	}
}
