package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
import com.gunyoung.tmb.utils.PageUtil;

/**
 * UserService 클래스에 대한 테스트 클래스<br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@SpringBootTest
public class UserServiceTest {
	
	private static final int INIT_USER_NUM = 30;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserExerciseRepository userExerciseRepository;
	
	@Autowired
	EntityManager em;
	
	@BeforeEach
	void setup() {
		for(int i=1;i<=INIT_USER_NUM;i++) {
			User user = User.builder()
							.email(i+"_user@test.com")
							.password("abcd1234!")
							.firstName(i+"번째")
							.lastName("사람")
							.nickName("User"+i)
							.role(RoleType.USER)
							.build();
			
			userRepository.save(user);
		}
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
		List<User> userList = userRepository.findAll();
		long maxId = -1;
		
		for(User user: userList) {
			maxId = Math.max(maxId, user.getId());
		}
		
		//When
		User result = userService.findById(maxId + 10);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@DisplayName("Id로 유저 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existId = userRepository.findAll().get(0).getId();
		
		//When
		User result = userService.findById(existId);
		
		//Then
		assertEquals(result != null, true);
		assertEquals(result.getId(),existId);
	}
	
	/*
	 *  public User findByEmail(String email)
	 */
	
	@Test
	@DisplayName("Email로 유저 찾기 -> 해당 email의 유저 없음")
	public void findByEmailNonExist() {
		//Given
		String email = "nonexist@test.com";
		
		//When
		User result = userService.findByEmail(email);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@DisplayName("Email로 유저 찾기 -> 정상")
	public void findByEmailTest() {
		//Given
		String email = "1_user@test.com";
		
		//When
		User result = userService.findByEmail(email);
		
		//Then
		assertEquals(result != null , true);
		assertEquals(result.getNickName(),"User1");
	}
	
	/*
	 *  public Page<User> findAllByNickNameOrName(String keyword)
	 */
	
	@Test
	@DisplayName("키워드로 유저 페이지 찾기 -> 정상")
	public void findAllByNickNameOrName() {
		//Given
		String nickNameKeyword = "User"+INIT_USER_NUM;
		String firstNameKeyword = INIT_USER_NUM+"번";
		String lastNameKeyword = "사람";
				
		//When
		int nickResult = userService.findAllByNickNameOrName(nickNameKeyword, 1).getContent().size();
		int firstResult = userService.findAllByNickNameOrName(firstNameKeyword, 1).getContent().size();
		int lastResult = userService.findAllByNickNameOrName(lastNameKeyword, 1).getContent().size();
		
		//Then
		assertEquals(nickResult,1);
		assertEquals(firstResult,1);
		assertEquals(lastResult,PageUtil.BY_NICKNAME_NAME_PAGE_SIZE);
	}
	
	/*
	 *   public User save(User user)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저 필드 값 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		String existEmail = "1_user@test.com";
		String changeEmail = "changed@test.com";
		User user = userRepository.findByEmail(existEmail).get();
		user.setEmail(changeEmail);
		
		//When
		User result = userService.save(user);
		
		//Then
		
		User changedUser = userRepository.findByEmail(changeEmail).get();
		assertEquals(result != null, true);
		assertEquals(changedUser.getEmail(),changeEmail);
	}
	
	@Test
	@Transactional
	@DisplayName("새로운 유저 저장 -> 정상")
	public void saveTest() {
		//Given
		User user = User.builder()
						.email("new@test.com")
						.password("abcd1234!")
						.firstName("new")
						.lastName("new")
						.nickName("new man")
						.build();
		
		//When
		userService.save(user);
		
		//Then
		User newUser = userRepository.findByEmail("new@test.com").get();
		assertEquals(newUser != null,true);
	}
	
	/*
	 *  public User saveByJoinDTO(UserJoinDTO dto,RoleType role)
	 */
	@Test
	@Transactional
	@DisplayName("새로운 유저 저장 by UserJoinDTO -> 정상")
	public void saveByJoinDTOTest() {
		//Given
		UserJoinDTO dto = UserJoinDTO.builder()
				.email("new@test.com")
				.password("abcd1234!")
				.firstName("new")
				.lastName("new")
				.nickName("new man")
				.build();
		
		//When
		userService.saveByJoinDTO(dto, RoleType.USER);
		
		//Then
		User newUser = userRepository.findByEmail("new@test.com").get();
		assertEquals(newUser != null,true);
	}
	
	/*
	 *   public void deleteUser(User user)
	 */
	@Test
	@Transactional
	@DisplayName("유저 삭제 -> 정상")
	public void deleteUser() {
		//Given
		User user = userRepository.findByEmail("1_user@test.com").get();

		//When
		userService.deleteUser(user);
		
		//Then
		Optional<User> result = userRepository.findByEmail("1_user@test.com");
		
		assertEquals(result.isEmpty(),true);
	}
	
	/*
	 *   public boolean existsByEmail(String email)
	 */
	@Test
	@DisplayName("Email로 유저 존재 확인 하기 -> 정상")
	public void existsByEmailTest() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		String existEmail = "1_user@test.com";
		
		//When
		boolean resultNonExist = userService.existsByEmail(nonExistEmail);
		boolean resultExist = userService.existsByEmail(existEmail);
		
		//Then
		assertEquals(resultNonExist,false);
		assertEquals(resultExist,true);
	}
	
	/*
	 *   public boolean existsByNickName(String nickName)
	 */
	@Test
	@DisplayName("NickName으로 유저 존재확인하기 ->정상")
	public void existsByNickNameTest() {
		//Given
		String nonExistNickName = "None";
		String existNickName = "User1";
		
		//When
		boolean resultNonExist = userService.existsByNickName(nonExistNickName);
		boolean resultExist = userService.existsByNickName(existNickName);
		
		//Then
		assertEquals(resultNonExist,false);
		assertEquals(resultExist,true);
	}
	
	/*
	 *   public boolean checkDuplicationForEmail(String email)
	 */
	@Test
	@DisplayName("Email 중복 여부 체크 -> 정상")
	public void checkDuplicationForEmailTest() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		String existEmail = "1_user@test.com";
		
		//When
		boolean resultNonExist = userService.checkDuplicationForEmail(nonExistEmail);
		boolean resultExist = userService.checkDuplicationForEmail(existEmail);
		
		//Then
		assertEquals(resultNonExist,false);
		assertEquals(resultExist,true);
	}
	
	/*
	 *   public long countAll()
	 */
	@Test
	@DisplayName("모든 유저 수 구하기 -> 정상")
	public void countAllTest() {
		//Given
		
		//When
		long result = userService.countAll();
		
		//Then
		assertEquals(result,INIT_USER_NUM);
	}
	
	/*
	 *  public long countAllByNickNameOrName(String keyword)
	 */
	@Test
	@DisplayName("닉네임이나 네임이 키워드를 만족하는 모든 User 수 구하기-> 정상")
	public void countAllByNickNameOrNameTest() {
		//Given
		String nickNameKeyword = "User"+INIT_USER_NUM;
		String firstNameKeyword = INIT_USER_NUM+"번";
		String lastNameKeyword = "사람";
		
		//When
		long nickResult = userService.countAllByNickNameOrName(nickNameKeyword);
		long firstResult = userService.countAllByNickNameOrName(firstNameKeyword);
		long lastResult = userService.countAllByNickNameOrName(lastNameKeyword);
		
		//Then
		assertEquals(nickResult,1);
		assertEquals(firstResult,1);
		assertEquals(lastResult,INIT_USER_NUM);
	}
	
	/**
	 * public User addUserExercise(User user, UserExercise userExercise)
	 * public void deleteUserExercise(User user, UserExercise userExercise) 
	 */
	
	@Test
	@Transactional
	@DisplayName("유저의 운동 기록 추가, 기록 삭제 -> 정상")
	public void addUserExerciseTest() {
		//Given
		User user = userRepository.findAll().get(0);
		Long userId = user.getId();
		int userExerciseListNum = user.getUserExercises().size();
		
		long userExerciseNum = userExerciseRepository.count();
		
		UserExercise userExercise = UserExercise.builder()
												.laps(1)
												.sets(1)
												.weight(1)
												.date(Calendar.getInstance())
												.description("test")
												.build();
		
		//When
		User result = userService.addUserExercise(user, userExercise);
		
		//Then
		assertEquals(result.getUserExercises().size(),userExerciseListNum+1);
		assertEquals(userExerciseRepository.count(),userExerciseNum+1);
		assertEquals(userExercise.getUser().getId(),userId);
		assertEquals(userRepository.getById(userId).getUserExercises().size(),userExerciseListNum+1);
		
		/*
		 * delete
		 */
		
		//Given 
		Long targetUserExerciseId = userExercise.getId();
		
		//When
		userService.deleteUserExercise(user, userExercise);
		
		//Then
		assertEquals(userRepository.findById(targetUserExerciseId).isEmpty(), true);
		assertEquals(user.getUserExercises().size(),userExerciseListNum);
	}
	
	
}
