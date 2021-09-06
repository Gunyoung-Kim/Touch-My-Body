package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.util.UserExerciseTest;
import com.gunyoung.tmb.util.UserTest;
import com.gunyoung.tmb.util.tag.Integration;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * UserService 클래스에 대한 테스트 클래스<br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserExerciseRepository userExerciseRepository;
	
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
	 *   public User findById(Long id)
	 */
	
	@Test
	@DisplayName("Id로 유저 찾기 -> 해당하는 Id의 유저 없음")
	public void findByIdNonExist() {
		//Given
		long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		//When
		User result = userService.findById(nonExistUserId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("Id로 유저 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existId = user.getId();
		
		//When
		User result = userService.findById(existId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  public User findByEmail(String email)
	 */
	
	@Test
	@DisplayName("Email로 유저 찾기 -> 해당 email의 유저 없음")
	public void findByEmailNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		
		//When
		User result = userService.findByEmail(nonExistEmail);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("Email로 유저 찾기 -> 정상")
	public void findByEmailTest() {
		//Given
		String existEmail = user.getEmail();
		
		//When
		User result = userService.findByEmail(existEmail);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  public Page<User> findAllByNickNameOrNameInPage(String keyword)
	 */
	
	@Test
	@DisplayName("키워드로 유저 페이지 찾기 -> 정상, 모든 유저가 만족하는 키워드")
	public void findAllByNickNameOrNameInPageAll() {
		//Given
		String keywordForAllUser = UserTest.DEFAULT_NICKNAME;
		
		UserTest.addNewUsersInDBByNum(10, userRepository);
		
		long givenUserNum = userRepository.count();
				
		//When
		int result = userService.findAllByNickNameOrNameInPage(keywordForAllUser, 1).getContent().size();
		
		//Then
		assertEquals(Math.min(PageUtil.BY_NICKNAME_NAME_PAGE_SIZE, givenUserNum), result);
	}
	
	@Test
	@DisplayName("키워드로 유저 페이지 찾기 -> 정상, 오직 한 유저만 만족하는 키워드")
	public void findAllByNickNameOrNameInPageOnlyOne() {
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
	public void findAllByNickNameOrNameInPageNothing() {
		//Given
		String keywordForNothing = "noone@test.com";
		
		UserTest.addNewUsersInDBByNum(10, userRepository);
				
		//When
		int result = userService.findAllByNickNameOrNameInPage(keywordForNothing, 1).getContent().size();
		
		//Then
		assertEquals(0, result);
	}
	
	/*
	 *   public User save(User user)
	 */
	
	@Test
	@DisplayName("유저 필드 값 수정하기 -> 정상, 변화 확인")
	public void mergeTestCheckChange() {
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
	public void mergeTestCheckCount() {
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
	public void addTest() {
		//Given
		User newUser = UserTest.getUserInstance("newUser@test.com", "newby");
		
		long givenUserNum = userRepository.count();
		
		//When
		userService.save(newUser);
		
		//Then
		assertEquals(givenUserNum + 1, userRepository.count());
	}
	
	/*
	 *  public User saveByJoinDTO(UserJoinDTO dto,RoleType role)
	 */
	@Test
	@DisplayName("새로운 유저 저장 by UserJoinDTO -> 정상")
	public void saveByJoinDTOTest() {
		//Given
		String newUserEmail = "newUser@test.com";
		UserJoinDTO dto = UserTest.getUserJoinDTOInstance("newUser@test.com", "newby");
		
		//When
		userService.saveByJoinDTO(dto, RoleType.USER);
		
		//Then
		assertTrue(userRepository.existsByEmail(newUserEmail));
	}
	
	/*
	 *   public void deleteUser(User user)
	 */
	@Test
	@DisplayName("유저 삭제 -> 정상")
	public void deleteUser() {
		//Given
		Long userId = user.getId();

		//When
		userService.deleteUser(user);
		
		//Then
		assertFalse(userRepository.existsById(userId));
	}
	
	/*
	 *   public boolean existsByEmail(String email)
	 */
	@Test
	@DisplayName("Email로 유저 존재 확인 하기 -> 정상, True")
	public void existsByEmailTestTrue() {
		//Given
		String existEmail = user.getEmail();
		
		//When
		boolean result = userService.existsByEmail(existEmail);
		
		//Then
		assertTrue(result);
	}
	
	@Test
	@DisplayName("Email로 유저 존재 확인 하기 -> 정상, False")
	public void existsByEmailTestFalse() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		
		//When
		boolean result = userService.existsByEmail(nonExistEmail);
		
		//Then
		assertFalse(result);
	}
	
	/*
	 *   public boolean existsByNickName(String nickName)
	 */
	
	@Test
	@DisplayName("NickName으로 유저 존재확인하기 ->정상, True")
	public void existsByNickNameTestTrue() {
		//Given
		String existNickName = user.getNickName();
		
		//When
		boolean result = userService.existsByNickName(existNickName);
		
		//Then
		assertTrue(result);
	}
	
	@Test
	@DisplayName("NickName으로 유저 존재확인하기 ->정상, False")
	public void existsByNickNameTestFalse() {
		//Given
		String nonExitNickName = "nonExist";
		
		//When
		boolean result = userService.existsByNickName(nonExitNickName);
		
		//Then
		assertFalse(result);
	}
	
	/*
	 *   public long countAll()
	 */
	@Test
	@DisplayName("모든 유저 수 구하기 -> 정상")
	public void countAllTest() {
		//Given
		UserTest.addNewUsersInDBByNum(10, userRepository);
		
		long givenUserNum = userRepository.count();
		
		//When
		long result = userService.countAll();
		
		//Then
		assertEquals(givenUserNum, result);
	}
	
	/*
	 *  public long countAllByNickNameOrName(String keyword)
	 */
	@Test
	@DisplayName("닉네임이나 네임이 키워드를 만족하는 모든 User 수 구하기-> 정상, 모든 유저가 만족하는 키워드")
	public void countAllByNickNameOrNameTestAll() {
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
	public void countAllByNickNameOrNameTestOnlyOne() {
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
	public void countAllByNickNameOrNameTest() {
		//Given
		String nickNameForNothing = "noOne";
		
		UserTest.addNewUsersInDBByNum(10, userRepository);
		
		//When
		long result = userService.countAllByNickNameOrName(nickNameForNothing);
		
		//Then
		assertEquals(0, result);
	}
	
	
	/**
	 * public User addUserExercise(User user, UserExercise userExercise) 
	 */
	
	@Test
	@Transactional
	@DisplayName("유저의 운동 기록 추가, 기록 삭제 -> 정상, 개수 추가 확인")
	public void addUserExerciseTestCheckCount() {
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
	public void addUserExerciseTestCheckWithUser() {
		//Given		
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
		
		//When
		userService.addUserExercise(user, userExercise);
		
		//Then
		assertEquals(user.getId(), userExercise.getUser().getId());
	}
	
	/*
	 * public void deleteUserExercise(User user, UserExercise userExercise)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저의 운동 기록 삭제 -> 정상")
	public void deleteUserExerciseTest() {
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
