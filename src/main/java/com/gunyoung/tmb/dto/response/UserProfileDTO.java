package com.gunyoung.tmb.dto.response;

import com.gunyoung.tmb.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 유저 자신의 개인정보 화면에서 보여줄 정보를 담은 객체
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
	private Long id;
	private String email;
	private String nickName;
	private String firstName;
	private String lastName;
	
	/**
	 * User 객체를 이용해 UserProfileDTO 객체 생성 및 반환하는 메소드
	 * @author kimgun-yeong
	 */
	public static UserProfileDTO of(User user) {
		UserProfileDTO dto = UserProfileDTO.builder()
				.id(user.getId())
				.email(user.getEmail())
				.nickName(user.getNickName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.build();
		
		return dto;
	}
}
