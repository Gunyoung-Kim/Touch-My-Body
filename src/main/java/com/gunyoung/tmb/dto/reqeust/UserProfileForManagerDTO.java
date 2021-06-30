package com.gunyoung.tmb.dto.reqeust;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.BusinessException;
import com.gunyoung.tmb.error.exceptions.nonexist.RoleNotFoundedException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
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
	 * 
	 * @param user
	 * @param dto
	 * @return
	 * @throws BusinessException dto를 통해 입력된 사항이 잘못된 내용일때
	 * @author kimgun-yeong
	 */
	public static User mergeUserWithUserProfileForManagerDTO(User user, UserProfileForManagerDTO dto) throws BusinessException {
		RoleType newRole = null;
		String inputRole = dto.getRole();
		
		for(RoleType t: RoleType.values()) {
			if(t.toString().equals(inputRole)) {
				newRole = RoleType.valueOf(inputRole);
			}
		}
		
		if(newRole == null) {
			throw new RoleNotFoundedException(UserErrorCode.RoleNotFoundedError.getDescription());
		}
		
		user.setRole(newRole);
		return user;
	}
	
	
}
