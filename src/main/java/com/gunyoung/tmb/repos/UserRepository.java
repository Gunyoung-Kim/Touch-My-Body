package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.user.User;

public interface UserRepository extends JpaRepository<User,Long>{
	public Optional<User> findByEmail(String email);
	
	public boolean existsByEmail(String email);
}
