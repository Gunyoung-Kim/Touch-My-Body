package com.gunyoung.tmb.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;

/**
 * Test 클래스 전용 User 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class UserTest {
	
	public final static String DEFAULT_EMAIL = "test@test.com";
	public final static String DEFAULT_NICKNAME = "nickName";
	
	/**
	 * 테스트용 User 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static User getUserInstance() {
		return getUserInstance(RoleType.USER);
	}
	
	/**
	 * 테스트용 User 인스턴스 반환 <br>
	 * email, nickName 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static User getUserInstance(String email, String nickName) {
		User user = getUserInstance();
		user.setEmail(email);
		user.setNickName(nickName);
		
		return user;
	}
	
	/**
	 * 테스트용 User 인스턴스 반환 <br>
	 * role 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static User getUserInstance(RoleType role) {
		User user = User.builder()
				.email(DEFAULT_EMAIL)
				.password("abcd1234!")
				.firstName("first")
				.lastName("last")
				.nickName(DEFAULT_NICKNAME)
				.role(role)
				.build();
		
		return user;
	}
	
	/**
	 * Repository를 통해 존재하지 않는 User ID 반환 
	 * @author kimgun-yeong
	 */
	public static Long getNonExistUserId(JpaRepository<User, Long> userRepository) {
		Long nonExistUserId = Long.valueOf(1);
		
		for(User u : userRepository.findAll()) {
			nonExistUserId = Math.max(nonExistUserId, u.getId());
		}
		nonExistUserId++;
		
		return nonExistUserId;
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.UserJoinDTO} 에 바인딩 될 수 있는 MultiValueMap 반환 <br>
	 * email, nickName value 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String, String> getUserJoinDTOMap(String email, String nickName) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", email);
		map.add("password", "abcd1234!");
		map.add("firstName", "first");
		map.add("lastName", "lastName");
		map.add("nickName", nickName);
		return map;
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.UserProfileForManagerDTO} 에 바인딩 될 수 있는 MultiValueMap 반환 <br>
	 * role 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String,String> getUserProfileForManagerDTOMap(RoleType role) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("role", role.toString());
		return map;
	}
}
