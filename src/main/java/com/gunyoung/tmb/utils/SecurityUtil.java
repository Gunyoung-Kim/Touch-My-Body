package com.gunyoung.tmb.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.gunyoung.tmb.enums.RoleType;

public class SecurityUtil {
	
	/**
	 * Authority들을 ROLE_ 제외한 toString들 반환
	 * @param authorities
	 * @author kimgun-yeong
	 */
	public static List<String> getAuthorityStringsExceptROLE(Collection<? extends GrantedAuthority> authorities) {
		List<String> result = new ArrayList<>();
		for(GrantedAuthority a : authorities) {
			result.add(a.getAuthority().substring(5));
		}
		
		return result;
	}
	
	/**
	 * 유저의 RoleType을 통해 Authority 반환하는 메소드
	 * @param roleType
	 * @author kimgun-yeong
	 */
	public static Collection<? extends GrantedAuthority> getAuthoritiesByUserRoleType(RoleType roleType) {
		String roleString = "ROLE_" + roleType.toString();
		return Collections.singletonList(new SimpleGrantedAuthority(roleString));
	}
}
