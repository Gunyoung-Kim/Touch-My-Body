package com.gunyoung.tmb.services.domain.user;

import com.gunyoung.tmb.domain.user.User;

/**
 * User 도메인 관련 처리 서비스
 * @author kimgun-yeong
 *
 */
public interface UserService {
	public User findById(Long id);
	public User findByEmail(String email);
	
	public User save(User user);
	
	public void deleteUser(User user);
	
	public boolean existsByEmail(String email);
	
	public boolean checkDuplicationForEmail(String email);
	
	public long countAll();
}
