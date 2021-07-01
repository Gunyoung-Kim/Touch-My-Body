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
	
	/**
	 * 지연로딩 되어있는 UserExercises를 비즈니스 요구사항에 따라 즉시 로딩이 필요할떄 사용 
	 * @param id
	 * @return
	 */
	@Query("SELECT u FROM User u "
			+ "JOIN FETCH u.userExercises "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithUserExercisesById(@Param("userId") Long id);
	
	/**
	 * 지연로딩 되어있는 Feedbacks를 비즈니스 요구사항에 따라 즉시 로딩이 필요할 때 사용
	 * @param id
	 * @return
	 */
	@Query("SELECT u FROM User u "
			+ "JOIN FETCH u.feedbacks "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithFeedbacksById(@Param("userId") Long id);
	
	@Query("SELECT u FROM User u "
			+ "JOIN FETCH u.postLikes "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithPostLikesById(@Param("userId") Long id);
	
	@Query("SELECT u FROM User u "
			+ "JOIN FETCH u.commentLikes "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithCommentLikesById(@Param("userId") Long id);
	
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
