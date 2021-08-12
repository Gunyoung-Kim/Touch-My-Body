package com.gunyoung.tmb.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;

/**
 * Authority 관련 처리 서비스
 * @author kimgun-yeong
 *
 */
public interface AuthorityService {
	
	/**
	 * 세션 접속자의 권한으로 인자로 전달된 유저의 권한에 접근 가능한지 여부 반환 <br>
	 * 현재 Authority의 권한은 일자 구조이기에 비교는 접근 가능 권한 개수로 비교, 추후 권한의 구조가 일자구조가 아니게 된다면 구현 로직 변경 요망 
	 * @param target 세션 접속자와 권한 비교하려는 대상 User
	 * @author kimgun-yeong
	 */
	public boolean isSessionUserAuthorityCanAccessToTargetAuthority(User target);
	
	/**
	 * SecurityContext에 저장된 세션 유저의 Authority 반환
	 * @author kimgun-yeong
	 */
	public Collection<? extends GrantedAuthority> getSessionUserAuthorities();
	
	/**
	 * 입력된 권한들로 접근 가능한 권한이 목록 (string) 반환하는 메소드
	 * @author kimgun-yeong
	 */
	public List<String> getReachableAuthorityStrings(Collection<? extends GrantedAuthority> authorities);
	
	/**
	 * Authority들을 ROLE_ 제외한 toString들 반환
	 * @author kimgun-yeong
	 */
	public List<String> getAuthorityStringsExceptROLE(Collection<? extends GrantedAuthority> authorities);
	
	/**
	 * 유저의 RoleType을 통해 Authority 반환하는 메소드
	 * @param roleType User의 RoleType
	 * @author kimgun-yeong
	 */
	public Collection<? extends GrantedAuthority> getAuthoritiesByUserRoleType(RoleType roleType);
}
