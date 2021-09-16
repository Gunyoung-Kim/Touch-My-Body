package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.user.User;

public interface UserRepository extends JpaRepository<User,Long>{
	
	/**
	 * email로 User 존재하는지 확인
	 * @param email 찾으려는 User의 email
	 * @return
	 * @author kimgun-yeong
	 */
	public boolean existsByEmail(String email);
	
	/**
	 * nickName으로 User 존재하는지 확인
	 * @param nickName 찾으려는 User의 nickName
	 * @return
	 * @author kimgun-yeong
	 */
	public boolean existsByNickName(String nickName);
	
	/**
	 * email 로 User 찾기 
	 * @param email 찾으려는 User의 email
	 * @return
	 * @author kimgun-yeong
	 */
	public Optional<User> findByEmail(String email);
	
	/**
	 * ID로 User 찾기 <br>
	 * UserExercise Left 페치 조인 
	 * @param id 찾으려는 User의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT u FROM User u "
			+ "LEFT JOIN FETCH u.userExercises "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithUserExercisesById(@Param("userId") Long id);
	
	/**
	 * ID로 User 찾기 <br>
	 * Feedbacks Left 페치 조인 
	 * @param id 찾으려는 User의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT u FROM User u "
			+ "LEFT JOIN FETCH u.feedbacks "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithFeedbacksById(@Param("userId") Long id);
	
	/**
	 * ID로 User 찾기 <br>
	 * PostLikes Left 페치 조인
	 * @param id 찾으려는 User의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT u FROM User u "
			+ "LEFT JOIN FETCH u.postLikes "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithPostLikesById(@Param("userId") Long id);
	
	/**
	 * ID로 User 찾기 <br>
	 * CommentLikes Left 페치 조인
	 * @param id 찾으려는 User의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT u FROM User u "
			+ "LEFT JOIN FETCH u.commentLikes "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithCommentLikesById(@Param("userId") Long id);
	
	/**
	 * ID로 User 찾기 <br>
	 * ExercisePosts Left 페치 조인
	 * @param id 찾으려는 User의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT u FROM User u "
			+ "LEFT JOIN FETCH u.exercisePosts "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithExercisePostsById(@Param("userId") Long id);
	
	/**
	 * ID로 User 찾기 <br>
	 * Comments Left 페치 조인
	 * @param id 찾으려는 User의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT u FROM User u "
			+ "LEFT JOIN FETCH u.comments "
			+ "WHERE u.id = :userId")
	public Optional<User> findWithCommentsById(@Param("userId") Long id);
	
	/**
	 * nickName, name 검색 키워드로 User들 찾기 <br>
	 * role로 정렬 <br>
	 * 페이징 처리 
	 * @param keyword User의 nickName, name 검색 키워드 
	 * @param pageable
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT u FROM User u WHERE "
			+ "(u.firstName LIKE %:keyword%) OR "
			+ "(u.lastName LIKE %:keyword%) OR "
			+ "(u.nickName LIKE %:keyword%) "
			+ "ORDER BY u.role")
	public Page<User> findAllByNickNameOrName(@Param("keyword")String keyword,Pageable pageable);
	
	@Modifying
	@Query("DELETE FROM User u "
			+ "WHERE u.id = :userId")
	public void deleteByIdInQuery(@Param("userId") Long id);
	
	/**
	 * nickName, name 검색 키워드로 만족하는 User들 개수 찾기
	 * @param keyword User의 nickName, name 검색 키워드 
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(u) FROM User u WHERE "
			+ "(u.firstName LIKE %:keyword%) OR "
			+ "(u.lastName LIKE %:keyword%) OR "
			+ "(u.nickName LIKE %:keyword%)")
	public long countAllByNickNameOrName(@Param("keyword") String keyword);
}
