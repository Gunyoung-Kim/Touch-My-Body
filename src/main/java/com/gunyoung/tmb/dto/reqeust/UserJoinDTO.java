package com.gunyoung.tmb.dto.reqeust;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
}
