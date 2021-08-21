package com.gunyoung.tmb.dto.reqeust;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.RoleNotFoundedException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 매니저의 유저 정보 변경을 위한 DTO 객체
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileForManagerDTO {
	private String role;
	
	/**
	 * UserProfileForManagerDTO 정보를 통해 User 정보 수정
	 * @throws RoleNotFoundedException dto를 통해 입력된 사항중 role이 잘못된 입력일때, TargetType에 해당하지 않는 이름
	 * @author kimgun-yeong
	 */
	public static User mergeUserWithUserProfileForManagerDTO(User user, UserProfileForManagerDTO dto) throws RoleNotFoundedException {
		RoleType newRole = null;
		String inputRole = dto.getRole();
		for(RoleType t: RoleType.values()) {
			if(t.toString().equals(inputRole)) {
				newRole = RoleType.valueOf(inputRole);
			}
		}
		if(newRole == null) {
			throw new RoleNotFoundedException(UserErrorCode.ROLE_NOT_FOUNDED_ERROR.getDescription());
		}
		user.setRole(newRole);
		
		return user;
	}
}
