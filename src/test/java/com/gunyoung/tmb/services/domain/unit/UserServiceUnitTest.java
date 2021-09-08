package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.ExerciseServiceImpl;
import com.gunyoung.tmb.services.domain.user.UserExerciseService;
import com.gunyoung.tmb.services.domain.user.UserServiceImpl;
import com.gunyoung.tmb.util.UserExerciseTest;
import com.gunyoung.tmb.util.UserTest;

/**
 * {@link ExerciseServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExerciseServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	UserExerciseService userExerciseService;
	
	@InjectMocks
	UserServiceImpl userService;
	
	private User user;
	
	@BeforeEach
	void setup() {
		user = User.builder()
				.build();
	}
	
	/*
	 * public User findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 User 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(userRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		User result = userService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 User 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		
		given(userRepository.findById(userId)).willReturn(Optional.of(user));
		
		//When
		User result = userService.findById(userId);
		
		//Then
		assertEquals(user, result);
	}
	
	/*
	 * public User findByEmail(String email)
	 */
	
	@Test
	@DisplayName("email로 User 찾기 -> 존재하지 않음")
	public void findByEmailNonExist() {
		//Given
		String nonExistEmail = "nonExist@test.com";
		
		given(userRepository.findByEmail(nonExistEmail)).willReturn(Optional.empty());
		
		//When
		User result = userService.findByEmail(nonExistEmail);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("email로 User 찾기 -> 정상")
	public void findByEmailTest() {
		//Given
		String userEmail = "test@test.com";
		
		given(userRepository.findByEmail(userEmail)).willReturn(Optional.of(user));
		
		//When
		User result = userService.findByEmail(userEmail);
		
		//Then
		assertEquals(user, result);
	}
	
	/*
	 * public User findWithUserExerciseById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 User 찾기, UserExercises 페치 조인 -> 존재하지 않음")
	public void findWithUserExerciseByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(userRepository.findWithUserExercisesById(nonExistId)).willReturn(Optional.empty());
		
		//When
		User result = userService.findWithUserExerciseById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 User 찾기, UserExercises 페치 조인 -> 정상")
	public void findWithUserExerciseByIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		
		given(userRepository.findWithUserExercisesById(userId)).willReturn(Optional.of(user));
		
		//When
		User result = userService.findWithUserExerciseById(userId);
		
		//Then
		assertEquals(user, result);
	}
	
	/*
	 * public User findWithFeedbacksById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 User 찾기, Feedbacks 페치 조인 -> 존재하지 않음")
	public void findWithFeedbacksByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(userRepository.findWithFeedbacksById(nonExistId)).willReturn(Optional.empty());
		
		//When
		User result = userService.findWithFeedbacksById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 User 찾기, Feedbacks 페치 조인 -> 정상")
	public void findWithFeedbacksByIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		
		given(userRepository.findWithFeedbacksById(userId)).willReturn(Optional.of(user));
		
		//When
		User result = userService.findWithFeedbacksById(userId);
		
		//Then
		assertEquals(user, result);
	}
	
	/*
	 * public User findWithPostLikesById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 User 찾기, PostLikes 페치 조인 -> 존재하지 않음")
	public void findWithPostLikesByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(userRepository.findWithPostLikesById(nonExistId)).willReturn(Optional.empty());
		
		//When
		User result = userService.findWithPostLikesById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 User 찾기, PostLikes 페치 조인 -> 정상")
	public void findWithPostLikesByIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		
		given(userRepository.findWithPostLikesById(userId)).willReturn(Optional.of(user));
		
		//When
		User result = userService.findWithPostLikesById(userId);
		
		//Then
		assertEquals(user, result);
	}
	
	/*
	 * public User findWithCommentLikesById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 User 찾기, CommentLikes 페치 조인 -> 존재하지 않음")
	public void findWithCommentLikesByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(userRepository.findWithCommentLikesById(nonExistId)).willReturn(Optional.empty());
		
		//When
		User result = userService.findWithCommentLikesById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 User 찾기, CommentLikes 페치 조인 -> 정상")
	public void findWithCommentLikesByIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		
		given(userRepository.findWithCommentLikesById(userId)).willReturn(Optional.of(user));
		
		//When
		User result = userService.findWithCommentLikesById(userId);
		
		//Then
		assertEquals(user, result);
	}
	
	/*
	 * public User findfWithExercisePostsById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 User 찾기, ExercisePosts 페치 조인 -> 존재하지 않음")
	public void findWithExercisePostsByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(userRepository.findWithExercisePostsById(nonExistId)).willReturn(Optional.empty());
		
		//When
		User result = userService.findWithExercisePostsById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 User 찾기, ExercisePosts 페치 조인 -> 정상")
	public void findWithExercisePostsByIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		
		given(userRepository.findWithExercisePostsById(userId)).willReturn(Optional.of(user));
		
		//When
		User result = userService.findWithExercisePostsById(userId);
		
		//Then
		assertEquals(user, result);
	}
	
	/*
	 * public User findWithCommentsById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 User 찾기, Comments 페치 조인 -> 존재하지 않음")
	public void findWithCommentsByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(userRepository.findWithCommentsById(nonExistId)).willReturn(Optional.empty());
		
		//When
		User result = userService.findWithCommentsById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 User 찾기, Comments 페치 조인 -> 정상")
	public void findWithCommentsByIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		
		given(userRepository.findWithCommentsById(userId)).willReturn(Optional.of(user));
		
		//When
		User result = userService.findWithCommentsById(userId);
		
		//Then
		assertEquals(user, result);
	}
	
	/*
	 * public Page<User> findAllByNickNameOrNameInPage(String keyword,Integer pageNumber)
	 */
	
	@Test
	@DisplayName("User name, nickName 검색 키워드에 해당하는 User들 페이지 반환 -> 정상")
	public void findAllByNickNameOrNameTest() {
		//Given
		String keyword = "keyword";
		int pageNum = 1;
		
		//When
		userService.findAllByNickNameOrNameInPage(keyword, pageNum);
		
		//Then
		then(userRepository).should(times(1)).findAllByNickNameOrName(anyString(), any(PageRequest.class));
	}
	
	/*
	 * public User save(User user)
	 */
	
	@Test
	@DisplayName("User 생성 및 수정 -> 정상")
	public void saveTest() {
		//Given
		given(userRepository.save(user)).willReturn(user);
		
		//When
		User result = userService.save(user);
		
		//Then
		assertEquals(user, result);
	}
	
	/*
	 * public User saveByJoinDTOAndRoleType(UserJoinDTO dto,RoleType role);
	 */
	
	@Test
	@DisplayName(" UserJoinDTO 객체에 담긴 정보를 이용하여 User 객체 생성 후 저장 -> 정상, userRepository 확인")
	public void saveByJoinDTOAndRoleTypeTestCheckRepository() {
		//Given
		String email = "test@test.com";
		String nickName = "nickname";
		RoleType role = RoleType.USER;
		UserJoinDTO userJoinDTO = UserTest.getUserJoinDTOInstance(email, nickName);
		
		//When
		userService.saveByJoinDTOAndRoleType(userJoinDTO, role);
		
		//Then
		then(userRepository).should(times(1)).save(any(User.class));
	}
	
	/*
	 * public void delete(User user) 
	 */
	
	@Test
	@DisplayName("User 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		userService.delete(user);
		
		//Then
		then(userRepository).should(times(1)).delete(user);
	}
	
	/*
	 *	public boolean existsByEmail(String email)  
	 */
	
	@Test
	@DisplayName("email로 User 존재 여부 반환 -> 정상")
	public void existsByEmailTest() {
		//Given
		String email = "test@test.com";
		boolean isExist = true;
		
		given(userRepository.existsByEmail(email)).willReturn(isExist);
		
		//When
		boolean result = userService.existsByEmail(email);
		
		//Then
		assertEquals(isExist, result);
	}
	
	/*
	 *	public boolean existsByNickName(String nickName)  
	 */
	
	@Test
	@DisplayName("nickName로 User 존재 여부 반환 -> 정상")
	public void existsByNickNameTest() {
		//Given
		String nickName = "nickName";
		boolean isExist = true;
		
		given(userRepository.existsByNickName(nickName)).willReturn(isExist);
		
		//When
		boolean result = userService.existsByNickName(nickName);
		
		//Then
		assertEquals(isExist, result);
	}
	
	/*
	 * public long countAll() 
	 */
	
	@Test
	@DisplayName("모든 User 수 반환 -> 정상")
	public void countAllTest() {
		//Given
		long num = 1;
		given(userRepository.count()).willReturn(num);
		
		//When
		long result = userService.countAll();
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * public long countAllByNickNameOrName() 
	 */
	
	@Test
	@DisplayName("User name, nickName 키워드를 만족하는 User 수 반환 -> 정상")
	public void countAllByNickNameOrNameTest() {
		//Given
		long num = 1;
		String keyword = "keyword";
		given(userRepository.countAllByNickNameOrName(keyword)).willReturn(num);
		
		//When
		long result = userService.countAllByNickNameOrName(keyword);
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * public User addUserExercise(User user, UserExercise userExercise)
	 */
	
	@Test
	@DisplayName("User 객체에 UserExercise 객체 추가하는 메소드 -> 정상")
	public void addUserExerciseTest() {
		//Given
		UserExercise userExercise = UserExercise.builder().build();
		
		//When
		userService.addUserExercise(user, userExercise);
		
		//Then
		
		then(userExerciseService).should(times(1)).save(userExercise);
		
		assertEquals(userExercise, user.getUserExercises().get(0));
		assertEquals(user, userExercise.getUser());
	}
	
	/*
	 * public void deleteUserExercise(User user, UserExercise userExercise)
	 */
	
	@Test
	@DisplayName("User 객체에서 UserExercise 삭제 -> 정상, User의 UserExercise 아님")
	public void deleteUserExerciseTestNotMine() {
		//Given
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
		
		//When
		userService.deleteUserExercise(user, userExercise);
		
		//Then
		then(userExerciseService).should(times(1)).delete(userExercise);
	}
	
	@Test
	@DisplayName("User 객체에서 UserExercise 삭제 -> 정상, User의 Exercise")
	public void deleteUserExerciseTestMine() {
		//Given
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
		user.getUserExercises().add(userExercise);
		
		//When
		userService.deleteUserExercise(user, userExercise);
		
		//Then
		assertFalse(user.getUserExercises().contains(userExercise));
		then(userExerciseService).should(times(1)).delete(userExercise);
	}
}
