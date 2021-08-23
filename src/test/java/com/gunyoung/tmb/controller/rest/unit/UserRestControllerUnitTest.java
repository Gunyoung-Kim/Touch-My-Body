package com.gunyoung.tmb.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.controller.rest.UserRestController;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link UserRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserRestControllerUnitTest {
	
	@Mock
	UserService userService;
	
	@Mock
	CommentService commentService;
	
	@Mock
	ExercisePostService exercisePostService;
	
	@Mock
	HttpSession session;
	
	@InjectMocks 
	UserRestController userRestController;
	
	/*
	 * public boolean emailVerification(@RequestParam("email") String email)
	 */
	
	@Test
	@DisplayName("email 중복여부 반환 -> 정상, True")
	public void emailVerificationTestTrue() {
		//Given
		String existEmail = "test@test.com";
		boolean isExist = true;
		given(userService.existsByEmail(existEmail)).willReturn(isExist);
		
		//When
		boolean result = userRestController.emailVerification(existEmail);
		
		//Then
		assertEquals(isExist, result);
	}
	
	@Test
	@DisplayName("email 중복여부 반환 -> 정상, False")
	public void emailVerificationTestFalse() {
		//Given
		String nonExistEmail = "nonExist@test.com";
		boolean isExist = false;
		given(userService.existsByEmail(nonExistEmail)).willReturn(isExist);
		
		//When
		boolean result = userRestController.emailVerification(nonExistEmail);
		
		//Then
		assertEquals(isExist, result);
	}
	
	/*
	 * public boolean nickNameVerification(@RequestParam("nickName") String nickName)
	 */
	
	@Test
	@DisplayName("nickName 중복여부 반환 -> 정상, True")
	public void nickNameVerificationTestTrue() {
		//Given
		String existNickName = "exist";
		boolean isExist = true;
		given(userService.existsByNickName(existNickName)).willReturn(isExist);
		
		//When
		boolean result = userRestController.nickNameVerification(existNickName);
		
		//Then
		assertEquals(isExist, result);
	}
	
	@Test
	@DisplayName("nickName 중복여부 반환 -> 정상, False")
	public void nickNameVerificationTestFalse() {
		//Given
		String nonExistNickName = "nonExist";
		boolean isExist = false;
		given(userService.existsByNickName(nonExistNickName)).willReturn(isExist);
		
		//When
		boolean result = userRestController.nickNameVerification(nonExistNickName);
		
		//Then
		assertEquals(isExist, result);
	}
	
	/*
	 * public void removeMyComments(@RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@DisplayName("접속자가 자신이 작성한 특정 댓글 삭제 요청 처리 -> 정상")
	public void removeMyCommentsTest() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		Long commentId = Long.valueOf(1);
		
		//When
		userRestController.removeMyComments(commentId);
		
		//Then
		then(commentService).should(times(1)).checkIsMineAndDelete(loginIdInSession, commentId);
	}
	
	/*
	 * public void removeMyPosts(@RequestParam("postId") Long postId)
	 */
	
	@Test
	@DisplayName("접속자가 자신이 작성한 특정 게시글 삭제 요청 -> 정상")
	public void removeMyPostsTest() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		Long exercisePostId = Long.valueOf(1);
		//When
		userRestController.removeMyPosts(exercisePostId);
		
		//Then
		then(exercisePostService).should(times(1)).checkIsMineAndDelete(loginIdInSession, exercisePostId);
	}
}
