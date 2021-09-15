package com.gunyoung.tmb.services.domain.user;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.enums.RoleType;

/**
 * User 도메인 관련 처리 서비스
 * @author kimgun-yeong
 *
 */
public interface UserService {
	
	/**
	 * ID로 User 찾기
	 * @param id 찾으려는 유저의 id
	 * @return User, null(해당 id의 유저가 존재하지 않을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public User findById(Long id);
	
	/**
	 * email로 User 찾기
	 * @param email 찾으려는 유저의 email
	 * @return User, null(해당 email의 유저가 존재하지 않을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public User findByEmail(String email);
	
	/**
	 * UserExercises 페치 조인해서 가져오는 메소드 
	 * @param id User의 ID
	 * @since 11
	 * @author kimgun-yeong
	 */
	public User findWithUserExerciseById(Long id);
	
	/**
	 * Feedbacks 페치 조인해서 가져오는 메소드
	 * @param id User의 ID
	 * @since 11
	 * @author kimgun-yeong
	 */
	public User findWithFeedbacksById(Long id);
	
	/**
	 * PostLikes 페치 조인해서 가져오는 메소드
	 * @param id User의 ID
	 * @since 11
	 * @author kimgun-yeong
	 */
	public User findWithPostLikesById(Long id);
	
	/**
	 * CommentLikes 페치 조인해서 가져오는 메소드
	 * @param id User의 ID
	 * @since 11
	 * @author kimgun-yeong
	 */
	public User findWithCommentLikesById(Long id);
	
	/**
	 * ExercisePosts 페치 조인해서 가져오는 메소드
	 * @param id User의 ID
	 * @since 11
	 * @author kimgun-yeong
	 */
	public User findWithExercisePostsById(Long id);
	
	/**
	 * Comments 페치 조인해서 가져오는 메소드
	 * @param id User의 ID
	 * @since 11
	 * @author kimgun-yeong
	 */
	public User findWithCommentsById(Long id);
	
	/**
	 * User name, nickName 검색 키워드에 해당하는 User들 페이지 반환 
	 * @param keyword User name, nickName 검색 키워드 	
	 * @return
	 * @author kimgun-yeong
	 */
	public Page<User> findAllByNickNameOrNameInPage(String keyword,Integer pageNumber);
	
	/**
	 * User 생성 및 수정
	 * @param user 저장하려는 User 객체
	 * @return User
	 * @author kimgun-yeong
	 */
	public User save(User user);
	
	/**
	 * {@link UserJoinDTO} 객체에 담긴 정보를 이용하여 User 객체 생성 후 저장
	 * @param dto 저장할 User 객체 정보가 담긴 dto 객체
	 * @param role 저장할 User의 role
	 * @return 저장된 객체
	 * @author kimgun-yeong
	 */
	public User saveByJoinDTOAndRoleType(UserJoinDTO dto,RoleType role);
	
	/**
	 * User 삭제 <br>
	 * OneToMany 연관 엔티티도 모두 삭제
	 * @param user 삭제하려는 user
	 * @author kimgun-yeong
	 */
	public void delete(User user);
	
	/**
	 * email로 User 존재 여부 반환
	 * @param email 검색하려는 Email
	 * @return 해당 email의 유저가 존재하는지 여부
	 * @author kimgun-yeong
	 */
	public boolean existsByEmail(String email);
	
	/**
	 * nickName으로 User 존재 여부 반환
	 * @param nickName 검색하려는 ninkName
	 * @return 해당 nickName의 유저 존재하는지 여부
	 * @author kimgun-yeong
	 */
	public boolean existsByNickName(String nickName);
	
	/**
	 * 모든 User 수 반환
	 * @return 모든 유저의 수 반환 
	 * @author kimgun-yeong
	 */
	public long countAll();
	
	/**
	 * User name, nickName 키워드를 만족하는 User 수 반환
	 * @param keyword 검색하려는 닉네임이나 이름 키워드 
	 * @return 조건을 만족하는 User의 모든 수 
	 * @author kimgun-yeong
	 */
	public long countAllByNickNameOrName(String keyword);
	
	/**
	 * User 객체에 UserExercise 객체 추가하는 메소드
	 * @param user UserExercise를 추가하려는 User
	 * @param userExercise 추가하려는 UserExercise
	 * @author kimgun-yeong
	 */
	public User addUserExercise(User user,UserExercise userExercise);
	
	/**
	 * User 객체에서 UserExercise 삭제하는 메소드
	 * @param user UserExercise를 삭제하려는 User
	 * @param userExercise 삭제하려는 userExercise
	 * @author kimgun-yeong
	 */
	public void deleteUserExercise(User user, UserExercise userExercise);
}
