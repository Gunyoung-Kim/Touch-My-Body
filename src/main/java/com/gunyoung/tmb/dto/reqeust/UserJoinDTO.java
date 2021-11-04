package com.gunyoung.tmb.dto.reqeust;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gunyoung.tmb.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 유저 회원 가입할때 사용
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinDTO {
	
	@Email
	private String email;
	
	@NotNull
	@NotEmpty
	private String password;
	
	@Size(min =1,max=40)
	private String firstName;
	
	@Size(min =1,max=40)
	private String lastName;
	
	@Size(min =1, max=10)
	private String nickName;
	
	/**
	 * UserJoinDTO를 통해 User 객체 생성 및 반환
	 * @author kimgun-yeong
	 */
	public User createUserInstance() {
		return User.builder()
				.email(this.email)
				.password(this.password)
				.firstName(this.firstName)
				.lastName(this.lastName)
				.nickName(this.nickName)
				.build();
	}
}
