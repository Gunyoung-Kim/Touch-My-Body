package com.gunyoung.tmb.services.domain.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.utils.PageUtil;

import lombok.RequiredArgsConstructor;

/**
 * UserService Interface 구현 클래스 
 * @author kimgun-yeong
 *
 */
@Service("userService")
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	
	private final UserExerciseService userExerciseService;
	
	/**
	 * @param id 찾으려는 유저의 id
	 * @return User, null(해당 id의 유저가 존재하지 않을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public User findById(Long id) {
		Optional<User> result = userRepository.findById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}

	/**
	 * @param email 찾으려는 유저의 email
	 * @return User, null(해당 email의 유저가 존재하지 않을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public User findByEmail(String email) {
		Optional<User> result = userRepository.findByEmail(email);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * 유저 운동 기록이랑 페치 조인해서 가져오는 메소드 
	 * @param
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public User findWithUserExerciseById(Long id) {
		Optional<User> result = userRepository.findWithUserExercisesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * 유저 피드백이랑 페치 조인해서 가져오는 메소드 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public User findWithFeedbacksById(Long id) {
		Optional<User> result = userRepository.findWithFeedbacksById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * 게시글 좋아요랑 페치 조인해서 가져오는 메소드
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public User findWithPostLikesById(Long id) {
		Optional<User> result = userRepository.findWithPostLikesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	/**
	 * 댓글 좋아요랑 페치 조인해서 가져오는 메소드
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public User findWithCommentLikesById(Long id) {
		Optional<User> result = userRepository.findWithCommentLikesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	
	/**
	 * @param keyword 검색하려는 키워드	
	 * @return
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<User> findAllByNickNameOrName(String keyword,Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PageUtil.BY_NICKNAME_NAME_PAGE_SIZE);
		return userRepository.findAllByNickNameOrName(keyword, pageRequest);
	}

	/**
	 * @param user 저장하려는 User 객체
	 * @return User
	 * @author kimgun-yeong
	 */
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	
	/**
	 * @param dto 저장할 User 객체 정보가 담긴 dto 객체
	 * @return 저장된 객체
	 * @author kimgun-yeong
	 */
	@Override
	public User saveByJoinDTO(UserJoinDTO dto,RoleType role) {
		User user = User.builder()
						.email(dto.getEmail())
						.password(dto.getPassword())
						.firstName(dto.getFirstName())
						.lastName(dto.getLastName())
						.nickName(dto.getNickName())
						.role(role)
						.build();
		return userRepository.save(user);
	}

	/**
	 * @param user 삭제하려는 user
	 * @author kimgun-yeong
	 */
	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	/**
	 * @param email 검색하려는 Email
	 * @return 해당 email의 유저가 존재하는지 여부
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	/**
	 * @param nickName 검색하려는 ninkName
	 * @return 해당 nickName의 유저 존재하는지 여부
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean existsByNickName(String nickName) {
		return userRepository.existsByNickName(nickName);
	}

	/**
	 * @param email 검색하려는 Email
	 * @return 해당 email이 중복되는지 여부, 중복 시 true
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean checkDuplicationForEmail(String email) {
		return existsByEmail(email);
	}

	/**
	 * @return 모든 유저의 수 반환 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countAll() {
		return userRepository.count();
	}

	/**
	 * @param keyword 검색하려는 닉네임이나 이름 키워드 
	 * @return 조건을 만족하는 User의 모든 수 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countAllByNickNameOrName(String keyword) {
		return userRepository.countAllByNickNameOrName(keyword);
	}

	/**
	 * User 객체에 UserExercise 객체 추가하는 메소드
	 * @param user UserExercise를 추가하려는 User
	 * @param userExercise 추가하려는 UserExercise
	 * @author kimgun-yeong
	 */
	@Override
	public User addUserExercise(User user, UserExercise userExercise) {
		user.getUserExercises().add(userExercise);
		
		userExercise.setUser(user);
		userExerciseService.save(userExercise);
		return user;
	}

	/**
	 * User 객체에서 UserExercise 삭제하는 메소드
	 * @param user UserExercise를 삭제하려는 User
	 * @param userExercise 삭제하려는 userExercise
	 * @author kimgun-yeong
	 */
	@Override
	public void deleteUserExercise(User user, UserExercise userExercise) {
		user.getUserExercises().remove(userExercise);
		userExerciseService.delete(userExercise);
	}

}
