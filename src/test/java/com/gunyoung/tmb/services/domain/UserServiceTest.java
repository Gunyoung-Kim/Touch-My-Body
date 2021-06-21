package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.user.UserService;

/**
 * UserService 클래스에 대한 테스트 클래스<br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UserServiceTest {
	
	private static final int INIT_USER_NUM = 30;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
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
}
