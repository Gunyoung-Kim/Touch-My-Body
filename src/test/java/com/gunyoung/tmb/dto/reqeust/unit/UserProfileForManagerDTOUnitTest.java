package com.gunyoung.tmb.dto.reqeust.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.UserProfileForManagerDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.error.exceptions.nonexist.RoleNotFoundedException;
import com.gunyoung.tmb.util.UserTest;

/**
 * {@link UserProfileForManagerDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SaveExerciseDTO only
 * @author kimgun-yeong
 *
 */
public class UserProfileForManagerDTOUnitTest {

	/*
	 * public static User mergeUserWithUserProfileForManagerDTO(User user, UserProfileForManagerDTO dto) throws RoleNotFoundedException 
	 */
	
	@Test
	@DisplayName("UserProfileForManagerDTO 정보를 통해 User 정보 수정 -> dto에 담긴 role에 해당하는 RoleType 없을 때")
	public void mergeUserWithUserProfileForManagerDTOInvalidRole() {
		//Given
		User user = UserTest.getUserInstance();
		UserProfileForManagerDTO userProfileForManagerDTO = UserProfileForManagerDTO.builder()
				.role("invalid")
				.build();
		
		//When, Then
		assertThrows(RoleNotFoundedException.class, () -> {
			UserProfileForManagerDTO.mergeUserWithUserProfileForManagerDTO(user, userProfileForManagerDTO);
		});
	}
	
	@Test
	@DisplayName("UserProfileForManagerDTO 정보를 통해 User 정보 수정 -> 정상, 변화된 Role 확인")
	public void mergeUserWithUserProfileForManagerDTOTestCheckRole() {
		//Given
		User user = UserTest.getUserInstance();
		user.setRole(RoleType.USER);
		
		RoleType newRole = RoleType.ADMIN;
		String newRoleString = newRole.toString();
		UserProfileForManagerDTO userProfileForManagerDTO = UserProfileForManagerDTO.builder()
				.role(newRoleString)
				.build();
		
		//When
		User result = UserProfileForManagerDTO.mergeUserWithUserProfileForManagerDTO(user, userProfileForManagerDTO);
		
		//Then
		assertEquals(newRole, result.getRole());
	}
}
