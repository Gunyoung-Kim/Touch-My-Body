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
	public User findById(Long id);
	public User findByEmail(String email);
	
	public User findWithUserExerciseById(Long id);
	public User findWithFeedbacksById(Long id);
	public User findWithPostLikesById(Long id);
	public User findWithCommentLikesById(Long id);
	public User findWithExercisePostsById(Long id);
	public User findWithCommentsById(Long id);
	
	public Page<User> findAllByNickNameOrName(String keyword,Integer pageNumber);
	
	public User save(User user);
	public User saveByJoinDTO(UserJoinDTO dto,RoleType role);
	
	public void deleteUser(User user);
	
	public boolean existsByEmail(String email);
	public boolean existsByNickName(String nickName);
	
	public long countAll();
	public long countAllByNickNameOrName(String keyword);
	
	public User addUserExercise(User user,UserExercise userExercise);
	public void deleteUserExercise(User user, UserExercise userExercise);
}
