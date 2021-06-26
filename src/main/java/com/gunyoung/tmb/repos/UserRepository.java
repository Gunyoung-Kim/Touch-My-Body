package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.user.User;

public interface UserRepository extends JpaRepository<User,Long>{
	public Optional<User> findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE "
			+ "(u.firstName LIKE %:keyword%) OR "
			+ "(u.lastName LIKE %:keyword%) OR "
			+ "(u.nickName LIKE %:keyword%) "
			+ "ORDER BY u.role")
	public Page<User> findAllByNickNameOrName(@Param("keyword")String keyword,Pageable pageable);
	
	public boolean existsByEmail(String email);
	public boolean existsByNickName(String nickName);
	
	@Query("SELECT COUNT(u) FROM User u WHERE "
			+ "(u.firstName LIKE %:keyword%) OR "
			+ "(u.lastName LIKE %:keyword%) OR "
			+ "(u.nickName LIKE %:keyword%) "
			+ "ORDER BY u.role")
	public long countAllByNickNameOrName(@Param("keyword") String keyword);
}
