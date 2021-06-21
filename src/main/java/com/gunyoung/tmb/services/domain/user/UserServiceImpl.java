package com.gunyoung.tmb.services.domain.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.UserJoinDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.UserRepository;

/**
 * UserService Interface 구현 클래스 
 * @author kimgun-yeong
 *
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserExerciseService userExerciseService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
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
						.password(passwordEncoder.encode(dto.getPassword()))
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

}
