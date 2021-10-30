package com.gunyoung.tmb.services.domain.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.precondition.Preconditions;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;
import com.gunyoung.tmb.services.domain.like.CommentLikeService;
import com.gunyoung.tmb.services.domain.like.PostLikeService;

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
	
	private final PostLikeService postLikeService;
	
	private final ExercisePostService exercisePostService;
	
	private final CommentLikeService commentLikeService;
	
	private final CommentService commentService;
	
	private final FeedbackService feedbackService;
	
	@Override
	@Transactional(readOnly=true)
	public User findById(Long id) {
		Optional<User> result = userRepository.findById(id);
		return result.orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public User findByEmail(String email) {
		Optional<User> result = userRepository.findByEmail(email);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithUserExerciseById(Long id) {
		Optional<User> result = userRepository.findWithUserExercisesById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithFeedbacksById(Long id) {
		Optional<User> result = userRepository.findWithFeedbacksById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithPostLikesById(Long id) {
		Optional<User> result = userRepository.findWithPostLikesById(id);
		return result.orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public User findWithCommentLikesById(Long id) {
		Optional<User> result = userRepository.findWithCommentLikesById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithExercisePostsById(Long id) {
		Optional<User> result = userRepository.findWithExercisePostsById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public User findWithCommentsById(Long id) {
		Optional<User> result = userRepository.findWithCommentsById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<User> findAllByNickNameOrNameInPage(String keyword, Integer pageNumber) {
		Preconditions.notLessThan(pageNumber, 1, "pageNumber should be equal or greater than 1");
		
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PageSize.BY_NICKNAME_NAME_PAGE_SIZE.getSize());
		return userRepository.findAllByNickNameOrName(keyword, pageRequest);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public User saveByJoinDTOAndRoleType(UserJoinDTO dto, RoleType role) {
		Preconditions.notNull(dto, "Given UserJoinDTO must be not null");
		
		User user = dto.createUserInstance();
		user.setRole(role);
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		Preconditions.notNull(user, "Given user must be not null");
		
		deleteAllWeakEntityById(user);
		userRepository.deleteByIdInQuery(user.getId());
	}
	
	private void deleteAllWeakEntityById(User user) {
		Long userId = user.getId();
		userExerciseService.deleteAllByUserId(userId);
		commentLikeService.deleteAllByUserId(userId);
		commentService.deleteAllByUserId(userId);
		postLikeService.deleteAllByUserId(userId);
		exercisePostService.deleteAllByUserId(userId);
		feedbackService.deleteAllByUserId(userId);
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
	public long countAllByNickNameOrName(String keyword) {
		return userRepository.countAllByNickNameOrName(keyword);
	}

	@Override
	public User addUserExercise(User user, UserExercise userExercise) {
		Preconditions.notNull(user, "Given user must be not null");
		Preconditions.notNull(userExercise, "Given userExercise must be not null");
		
		setRelationBetweenUserAndUserExercise(user, userExercise);
		userExerciseService.save(userExercise);
		return user;
	}
	
	private void setRelationBetweenUserAndUserExercise(User user, UserExercise userExercise) {
		user.getUserExercises().add(userExercise);
		userExercise.setUser(user);
	}

	@Override
	public void deleteUserExercise(User user, UserExercise userExercise) {
		Preconditions.notNull(user, "Given user must be not null");
		Preconditions.notNull(userExercise, "Given userExercise must be not null");
		
		user.getUserExercises().remove(userExercise);
		userExerciseService.delete(userExercise);
	}
}
