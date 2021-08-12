package com.gunyoung.tmb.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;

import lombok.RequiredArgsConstructor;

/**
 * {@link AuthorityService} 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("authorityService")
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
	
	private final RoleHierarchy roleHierarchy;
	
	@Override
	public boolean isSessionUserAuthorityCanAccessToTargetAuthority(User target) {
		List<String> sessionUserAuthStringList = getReachableAuthorityStrings(getSessionUserAuthorities());
		List<String> targetReacheableStringList = getReachableAuthorityStrings(getAuthoritiesByUserRoleType(target.getRole()));
		
		if(targetReacheableStringList.size() > sessionUserAuthStringList.size()) {
			return false;
		}
		
		return true;
	}
	
	public Collection<? extends GrantedAuthority> getSessionUserAuthorities() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication sessionUserAuthentication = securityContext.getAuthentication();
		
		return sessionUserAuthentication.getAuthorities();
	}

	@Override
	public List<String> getReachableAuthorityStrings(Collection<? extends GrantedAuthority> authorities) {
		return getAuthorityStringsExceptROLE(roleHierarchy.getReachableGrantedAuthorities(authorities));
	}

	@Override
	public List<String> getAuthorityStringsExceptROLE(Collection<? extends GrantedAuthority> authorities) {
		List<String> result = new ArrayList<>();
		for(GrantedAuthority a : authorities) {
			result.add(a.getAuthority().substring(5));
		}
		
		return result;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthoritiesByUserRoleType(RoleType roleType) {
		String roleString = "ROLE_" + roleType.toString();
		return Collections.singletonList(new SimpleGrantedAuthority(roleString));
	}
}
