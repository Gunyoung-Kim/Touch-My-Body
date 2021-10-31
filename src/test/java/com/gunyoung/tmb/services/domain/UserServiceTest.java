package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.repos.CommentLikeRepository;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.FeedbackRepository;
import com.gunyoung.tmb.repos.PostLikeRepository;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.testutil.CommentLikeTest;
import com.gunyoung.tmb.testutil.CommentTest;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.FeedbackTest;
import com.gunyoung.tmb.testutil.PostLikeTest;
import com.gunyoung.tmb.testutil.UserExerciseTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * UserService 클래스에 대한 테스트 클래스<br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserExerciseRepository userExerciseRepository;
	
	@Autowired
	PostLikeRepository postLikeRepository;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	@Autowired
	CommentLikeRepository commentLikeRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	FeedbackRepository feedbackRepository;
	
	@Autowired
	EntityManager em;
	
	private User user;
	
	@BeforeEach
	void setup() {
		user = UserTest.getUserInstance();
		userRepository.save(user);
	}
	
	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
	}
	
	/*
	 *   User findById(Long id)
	 */
	
	@Test
	@DisplayName("Id로 유저 찾기 -> 해당하는 Id의 유저 없음")
	void findByIdNonExist() {
		//Given
		long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			userService.findById(nonExistUserId);
		});
	}
	
	@Test
	@DisplayName("Id로 유저 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long existId = user.getId();
		
		//When
		User result = userService.findById(existId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  User findByEmail(String email)
	 */
	
	@Test
	@DisplayName("Email로 유저 찾기 -> 해당 email의 유저 없음")
	void findByEmailNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			userService.findByEmail(nonExistEmail);
		});
	}
	
	@Test
	@DisplayName("Email로 유저 찾기 -> 정상")
	void findByEmailTest() {
		//Given
		String existEmail = user.getEmail();
		
		//When
		User result = userService.findByEmail(existEmail);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  Page<User> findAllByNickNameOrNameInPage(String keyword)
	 */
	
	@Test
	@DisplayName("키워드로 유저 페이지 찾기 -> 정상, 모든 유저가 만족하는 키워드")
	void findAllByNickNameOrNameInPageAll() {
		//Given
		String keywordForAllUser = UserTest.DEFAULT_NICKNAME;
		
		UserTest.addNewUsersInDBByNum(10, userRepository);
		
		long givenUserNum = userRepository.count();
				
		//When
		int result = userService.findAllByNickNameOrNameInPage(keywordForAllUser, 1).getContent().size();
		
		//Then
		assertEquals(Math.min(PageSize.BY_NICKNAME_NAME_PAGE_SIZE.getSize(), givenUserNum), result);
	}
	
	@Test
	@DisplayName("키워드로 유저 페이지 찾기 -> 정상, 오직 한 유저만 만족하는 키워드")
	void findAllByNickNameOrNameInPageOnlyOne() {
		//Given
		String keywordForOnlyOne = "onlyone";
		User onlyOneUser = UserTest.getUserInstance("onlyone@test.com", keywordForOnlyOne);
		userRepository.save(onlyOneUser);
		
		UserTest.addNewUsersInDBByNum(10, userRepository);
				
		//When
		int result = userService.findAllByNickNameOrNameInPage(keywordForOnlyOne, 1).getContent().size();
		
		//Then
		assertEquals(1, result);
	}
	
	@Test
	@DisplayName("키워드로 유저 페이지 찾기 -> 정상, 어떠한 유저도 만족하지 않는 키워드")
	void findAllByNickNameOrNameInPageNothing() {
		//Given
		String keywordForNothing = "noone@test.com";
		
		UserTest.addNewUsersInDBByNum(10, userRepository);
				
		//When
		int result = userService.findAllByNickNameOrNameInPage(keywordForNothing, 1).getContent().size();
		
		//Then
		assertEquals(0, result);
	}
	
	/*
	 *   User save(User user)
	 */
	
	@Test
	@DisplayName("유저 필드 값 수정하기 -> 정상, 변화 확인")
	void mergeTestCheckChange() {
		//Given
		String changeEmail = "changed@test.com";
		user.setEmail(changeEmail);
		
		//When
		userService.save(user);
		
		//Then
		User result = userRepository.findById(user.getId()).get();
		assertEquals(changeEmail, result.getEmail());
	}
	
	@Test
	@Transactional
	@DisplayName("유저 필드 값 수정하기 -> 정상, 개수 동일 확인")
	void mergeTestCheckCount() {
		//Given
		String changeEmail = "changed@test.com";
		user.setEmail(changeEmail);
		
		long givenUserNum = userRepository.count();
		
		//When
		userService.save(user);
		
		//Then
		assertEquals(givenUserNum, userRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("새로운 유저 저장 -> 정상")
	void addTest() {
		//Given
		User newUser = UserTest.getUserInstance("newUser@test.com", "newby");
		
		long givenUserNum = userRepository.count();
		
		//When
		userService.save(newUser);
		
		//Then
		assertEquals(givenUserNum + 1, userRepository.count());
	}
	
	/*
	 *  User saveByJoinDTO(UserJoinDTO dto,RoleType role)
	 */
	@Test
	@DisplayName("새로운 유저 저장 by UserJoinDTO -> 정상")
	void saveByJoinDTOTest() {
		//Given
		String newUserEmail = "newUser@test.com";
		UserJoinDTO dto = UserTest.getUserJoinDTOInstance("newUser@test.com", "newby");
		
		//When
		userService.saveByJoinDTOAndRoleType(dto, RoleType.USER);
		
		//Then
		assertTrue(userRepository.existsByEmail(newUserEmail));
	}
	
	/*
	 *   void delete(User user)
	 */
	
	@Test
	@DisplayName("유저 삭제 -> 정상, check User Delete")
	void deleteUserTestCheckUserDelete() {
		//Given
		Long userId = user.getId();

		//When
		userService.delete(user);
		
		//Then
		assertFalse(userRepository.existsById(userId));
	}
	
	@Test
	@Transactional
	@DisplayName("유저 삭제 -> 정상, Check UserExercise Delete")
	void deleteTestCheckUserExerciseDelete() {
		//Given
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
		userExercise.setUser(user);
		userExerciseRepository.save(userExercise);
		
		Long userExerciseId = userExercise.getId();
		
		//When
		userService.delete(user);
		
		//Then
		assertFalse(userExerciseRepository.existsById(userExerciseId));
	}
	
	@Test
	@Transactional
	@DisplayName("유저 삭제 -> 정상, Check CommentLike Delete")
	void deleteTestCheckCommentLikeDelete() {
		//Given
		CommentLike commentLike = CommentLikeTest.getCommentLikeInstance();
		commentLike.setUser(user);
		commentLikeRepository.save(commentLike);
		
		Long commentLikeId = commentLike.getId();
		
		//When
		userService.delete(user);
		
		//Then
		assertFalse(commentLikeRepository.existsById(commentLikeId));
	}
	
	@Test
	@Transactional
	@DisplayName("유저 삭제 -> 정상, Check Comment Delete")
	void deleteTestCheckCommentDelete() {
		//Given
		Comment comment = CommentTest.getCommentInstance();
		comment.setUser(user);
		commentRepository.save(comment);
		
		Long commentId = comment.getId();
		
		//When
		userService.delete(user);
		
		//Then
		assertFalse(commentRepository.existsById(commentId));
	}
	
	@Test
	@Transactional
	@DisplayName("유저 삭제 -> 정상, Check PostLike Delete")
	void deleteTestCheckPostLikeDelete() {
		//Given
		PostLike postLike = PostLikeTest.getPostLikeInstance();
		postLike.setUser(user);
		postLikeRepository.save(postLike);
		
		Long postLikeId = postLike.getId();
		
		//When
		userService.delete(user);
		
		//Then
		assertFalse(postLikeRepository.existsById(postLikeId));
	}
	
	@Test
	@Transactional
	@DisplayName("유저 삭제 -> 정상, Check ExercisePost Delete")
	void deleteTestCheckExercisePostDelete() {
		//Given
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePost.setUser(user);
		exercisePostRepository.save(exercisePost);
		
		Long exercisePostId = exercisePost.getId();
		
		//When
		userService.delete(user);
		
		//Then
		assertFalse(exercisePostRepository.existsById(exercisePostId));
	}
	
	@Test
	@Transactional
	@DisplayName("유저 삭제 -> 정상, Check Feedback Delete")
	void deleteTestCheckFeedbackDelete() {
		//Given
		Feedback feedback = FeedbackTest.getFeedbackInstance();
		feedback.setUser(user);
		feedbackRepository.save(feedback);
		
		Long feedbackId = feedback.getId();
		
		//When
		userService.delete(user);
		
		//Then
		assertFalse(feedbackRepository.existsById(feedbackId));
	}
	
	/*
	 *   boolean existsByEmail(String email)
	 */
	@Test
	@DisplayName("Email로 유저 존재 확인 하기 -> 정상, True")
	void existsByEmailTestTrue() {
		//Given
		String existEmail = user.getEmail();
		
		//When
		boolean result = userService.existsByEmail(existEmail);
		
		//Then
		assertTrue(result);
	}
	
	@Test
	@DisplayName("Email로 유저 존재 확인 하기 -> 정상, False")
	void existsByEmailTestFalse() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		
		//When
		boolean result = userService.existsByEmail(nonExistEmail);
		
		//Then
		assertFalse(result);
	}
	
	/*
	 *   boolean existsByNickName(String nickName)
	 */
	
	@Test
	@DisplayName("NickName으로 유저 존재확인하기 ->정상, True")
	void existsByNickNameTestTrue() {
		//Given
		String existNickName = user.getNickName();
		
		//When
		boolean result = userService.existsByNickName(existNickName);
		
		//Then
		assertTrue(result);
	}
	
	@Test
	@DisplayName("NickName으로 유저 존재확인하기 ->정상, False")
	void existsByNickNameTestFalse() {
		//Given
		String nonExitNickName = "nonExist";
		
		//When
		boolean result = userService.existsByNickName(nonExitNickName);
		
		//Then
		assertFalse(result);
	}
	
	/*
	 *   long countAll()
	 */
	@Test
	@DisplayName("모든 유저 수 구하기 -> 정상")
	void countAllTest() {
		//Given
		UserTest.addNewUsersInDBByNum(10, userRepository);
		
		long givenUserNum = userRepository.count();
		
		//When
		long result = userService.countAll();
		
		//Then
		assertEquals(givenUserNum, result);
	}
	
	/*
	 *  long countAllByNickNameOrName(String keyword)
	 */
	@Test
	@DisplayName("닉네임이나 네임이 키워드를 만족하는 모든 User 수 구하기-> 정상, 모든 유저가 만족하는 키워드")
	void countAllByNickNameOrNameTestAll() {
		//Given
		String nickNameForAllUser = UserTest.DEFAULT_NICKNAME;
		
		UserTest.addNewUsersInDBByNum(10, userRepository);
		
		long givenUserNum = userRepository.count();
		
		//When
		long result = userService.countAllByNickNameOrName(nickNameForAllUser);
		
		//Then
		assertEquals(givenUserNum, result);
	}
	
	@Test
	@DisplayName("닉네임이나 네임이 키워드를 만족하는 모든 User 수 구하기-> 정상, 오직 한 유저만 만족하는 키워드")
	void countAllByNickNameOrNameTestOnlyOne() {
		//Given
		String nickNameForOnlyOne = "onlyone";
		User onlyOneUser = UserTest.getUserInstance("onlyone@test.com", nickNameForOnlyOne);
		userRepository.save(onlyOneUser);
		
		UserTest.addNewUsersInDBByNum(10, userRepository);
		
		//When
		long result = userService.countAllByNickNameOrName(nickNameForOnlyOne);
		
		//Then
		assertEquals(1, result);
	}
	
	@Test
	@DisplayName("닉네임이나 네임이 키워드를 만족하는 모든 User 수 구하기-> 정상, 어느 유저도 만족하지 않는 키워드")
	void countAllByNickNameOrNameTest() {
		//Given
		String nickNameForNothing = "noOne";
		
		UserTest.addNewUsersInDBByNum(10, userRepository);
		
		//When
		long result = userService.countAllByNickNameOrName(nickNameForNothing);
		
		//Then
		assertEquals(0, result);
	}
	
	
	/**
	 * User addUserExercise(User user, UserExercise userExercise) 
	 */
	
	@Test
	@Transactional
	@DisplayName("유저의 운동 기록 추가, 기록 삭제 -> 정상, 개수 추가 확인")
	void addUserExerciseTestCheckCount() {
		//Given
		long givenUserExerciseNum = userExerciseRepository.count();
		
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
		
		//When
		userService.addUserExercise(user, userExercise);
		
		//Then
		assertEquals(givenUserExerciseNum + 1, userExerciseRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("유저의 운동 기록 추가, 기록 삭제 -> 정상, User와 연관 관계 확인")
	void addUserExerciseTestCheckWithUser() {
		//Given		
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
		
		//When
		userService.addUserExercise(user, userExercise);
		
		//Then
		assertEquals(user.getId(), userExercise.getUser().getId());
	}
	
	/*
	 * void deleteUserExercise(User user, UserExercise userExercise)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저의 운동 기록 삭제 -> 정상")
	void deleteUserExerciseTest() {
		//Given
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
		userExercise.setUser(user);
		userExerciseRepository.save(userExercise);
		Long userExerciseId = userExercise.getId();
		
		//When
		userService.deleteUserExercise(user, userExercise);
		
		//Then
		assertFalse(userExerciseRepository.existsById(userExerciseId));
	}
	
}
