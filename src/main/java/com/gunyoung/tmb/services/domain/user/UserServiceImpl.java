package com.gunyoung.tmb.services.domain.user;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.utils.CacheUtil;
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
	
	@Override
	@Transactional(readOnly=true)
	public User findById(Long id) {
		Optional<User> result = userRepository.findById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}

	@Override
	@Transactional(readOnly=true)
	public User findByEmail(String email) {
		Optional<User> result = userRepository.findByEmail(email);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithUserExerciseById(Long id) {
		Optional<User> result = userRepository.findWithUserExercisesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithFeedbacksById(Long id) {
		Optional<User> result = userRepository.findWithFeedbacksById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithPostLikesById(Long id) {
		Optional<User> result = userRepository.findWithPostLikesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	@Override
	@Transactional(readOnly=true)
	public User findWithCommentLikesById(Long id) {
		Optional<User> result = userRepository.findWithCommentLikesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithExercisePostsById(Long id) {
		Optional<User> result = userRepository.findWithExercisePostsById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithCommentsById(Long id) {
		Optional<User> result = userRepository.findWithCommentsById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<User> findAllByNickNameOrNameInPage(String keyword,Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PageUtil.BY_NICKNAME_NAME_PAGE_SIZE);
		return userRepository.findAllByNickNameOrName(keyword, pageRequest);
	}

	@Override
	@CacheEvict(cacheNames=CacheUtil.USER_NAME, allEntries=true)
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	@CacheEvict(cacheNames=CacheUtil.USER_NAME, allEntries=true)
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

	@Override
	@CacheEvict(cacheNames=CacheUtil.USER_NAME, allEntries=true)
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	@Override
	@Transactional(readOnly=true)
	public boolean existsByNickName(String nickName) {
		return userRepository.existsByNickName(nickName);
	}

	@Override
	@Transactional(readOnly=true)
	public long countAll() {
		return userRepository.count();
	}

	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=CacheUtil.USER_NAME,key="#root.methodName.concat(':').concat(#keyword)")
	public long countAllByNickNameOrName(String keyword) {
		return userRepository.countAllByNickNameOrName(keyword);
	}

	@Override
	public User addUserExercise(User user, UserExercise userExercise) {
		user.getUserExercises().add(userExercise);
		
		userExercise.setUser(user);
		userExerciseService.save(userExercise);
		return user;
	}

	@Override
	public void deleteUserExercise(User user, UserExercise userExercise) {
		user.getUserExercises().remove(userExercise);
		userExerciseService.delete(userExercise);
	}
}
