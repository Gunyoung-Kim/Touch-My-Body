package com.gunyoung.tmb.testutil;

import com.gunyoung.tmb.enums.RoleType;

/**
 * 테스트용 RoleType 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class RoleTypeTest {
	
	/**
	 * 인자로 입력된 RoleType과 다른 종류의 임의의 RoleType 반환
	 * @return RoleType, null(인자로 입력된 RoleType과 다른 종류의 RoleType 없을때)
	 * @author kimgun-yeong
	 */
	public static RoleType getAnotherRoleType(RoleType roleType) {
		for(RoleType tt: RoleType.values()) {
			if(!tt.equals(roleType)) {
				return tt;
			}
		}
		return null;
	}
}
