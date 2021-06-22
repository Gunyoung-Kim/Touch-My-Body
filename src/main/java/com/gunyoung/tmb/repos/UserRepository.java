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
	
	@Query("select u from User u where "
			+ "(u.firstName like %:keyword%) or "
			+ "(u.lastName like %:keyword%) or "
			+ "(u.nickName like %:keyword%) "
			+ "order by u.role")
	public Page<User> findAllByNickNameOrName(@Param("keyword")String keyword,Pageable pageable);
	
	public boolean existsByEmail(String email);
	public boolean existsByNickName(String nickName);
	
	@Query("select count(u) from User u where "
			+ "(u.firstName like %:keyword%) or "
			+ "(u.lastName like %:keyword%) or "
			+ "(u.nickName like %:keyword%) "
			+ "order by u.role")
	public long countAllByNickNameOrName(@Param("keyword") String keyword);
}
