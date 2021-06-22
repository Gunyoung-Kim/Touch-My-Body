package com.gunyoung.tmb.dto;

import com.gunyoung.tmb.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserManageListDTO {
	private String name;
	private String email;
	private String nickName;
	private String role;
	
	/**
	 * User을 UserManagerListDTO객체로 변환하는 메소드
	 * @param user
	 * @return
	 */
	public static UserManageListDTO of(User user) {
		UserManageListDTO dto = UserManageListDTO.builder()
												.email(user.getEmail())
												.name(user.getFullName())
												.nickName(user.getNickName())
												.role(user.getRole().getKoreanName())
												.build();
		return dto;
	}
}
