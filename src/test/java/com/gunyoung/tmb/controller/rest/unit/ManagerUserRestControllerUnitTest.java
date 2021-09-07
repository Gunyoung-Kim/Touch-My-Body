package com.gunyoung.tmb.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.controller.rest.ManagerUserRestController;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.UserProfileForManagerDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.error.exceptions.nonexist.RoleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.AccessDeniedException;
import com.gunyoung.tmb.security.AuthorityService;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.util.RoleTypeTest;
import com.gunyoung.tmb.util.UserTest;

/**
 * {@link ManagerUserRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerUserRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ManagerUserRestControllerUnitTest {
	
	@Mock
	UserService userService;
	
	@Mock
	CommentService commentService;
	
	@Mock
	ExercisePostService exercisePostService;
	
	@Mock
	AuthorityService authorityService;
	
	@InjectMocks
	ManagerUserRestController managerUserRestController;
	
	/*
	 * public void manageUserProfile(@PathVariable("userId") Long userId,@ModelAttribute UserProfileForManagerDTO dto)
	 */
	
	@Test
	@DisplayName("User의 정보 수정 요청 처리 -> 해당 Id의 유저 없을 때")
	public void manageUserProfileTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(52);
		given(userService.findById(nonExistUserId)).willReturn(null);
		
		UserProfileForManagerDTO userProfileForManagerDTO = UserTest.getUserProfileForManagerDTOInstance(RoleType.USER.toString()); 
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			managerUserRestController.manageUserProfile(nonExistUserId, userProfileForManagerDTO);
		});
	}
	
	@Test
	@DisplayName("User의 정보 수정 요청 처리 -> 접속자가 대상 User보다 권한 낮음")
	public void manageUserProfileTestAccessDenied() {
		//Given
		Long userId = Long.valueOf(73);
		User user = mockingUserServiceFindById(userId);
		
		given(authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(user)).willReturn(false);
		
		UserProfileForManagerDTO userProfileForManagerDTO = UserTest.getUserProfileForManagerDTOInstance(RoleType.MANAGER.toString());
		
		//When, Then
		assertThrows(AccessDeniedException.class, () -> {
			managerUserRestController.manageUserProfile(userId, userProfileForManagerDTO);
		});
	}
	
	@Test
	@DisplayName("User의 정보 수정 요청 처리 -> dto를 통해 유저 정보 처리하는 과정에서 예외 밣생")
	public void manageUserProfileTestMergeError() {
		//Given
		Long userId = Long.valueOf(73);
		User user = mockingUserServiceFindById(userId);
		
		given(authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(user)).willReturn(true);
		
		UserProfileForManagerDTO userProfileForManagerDTO = UserTest.getUserProfileForManagerDTOInstance("invalidRole");
		
		//When, Then
		assertThrows(RoleNotFoundedException.class, () -> {
			managerUserRestController.manageUserProfile(userId, userProfileForManagerDTO);
		});
	}
	
	@Test
	@DisplayName("User의 정보 수정 요청 처리 -> 정상, UserService check")
	public void manageUserProfileTestCheckUserService() {
		//Given
		Long userId = Long.valueOf(36);
		User user = mockingUserServiceFindById(userId);
		
		given(authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(user)).willReturn(true);
		
		RoleType changeRole = RoleTypeTest.getAnotherRoleType(user.getRole());
		UserProfileForManagerDTO userProfileForManagerDTO = UserTest.getUserProfileForManagerDTOInstance(changeRole.toString());
		
		//When
		managerUserRestController.manageUserProfile(userId, userProfileForManagerDTO);
		
		//Then
		then(userService).should(times(1)).save(user);
	}
	
	@Test
	@DisplayName("User의 정보 수정 요청 처리 -> 정상, Role 변화 확인")
	public void manageUserProfileTestCheckChangedRole() {
		//Given
		Long userId = Long.valueOf(72);
		User user = mockingUserServiceFindById(userId);
		
		given(authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(user)).willReturn(true);
		
		RoleType changeRole = RoleTypeTest.getAnotherRoleType(user.getRole());
		UserProfileForManagerDTO userProfileForManagerDTO = UserTest.getUserProfileForManagerDTOInstance(changeRole.toString());
		
		//When
		managerUserRestController.manageUserProfile(userId, userProfileForManagerDTO);
		
		//Then
		assertEquals(changeRole, user.getRole());
	}
	
	private User mockingUserServiceFindById(Long userId) {
		User user = UserTest.getUserInstance();
		user.setId(userId);
		given(userService.findById(userId)).willReturn(user);
		return user;
	}
	
	/*
	 * public void removeCommentByManager(@PathVariable("user_id") Long userId,@RequestParam("comment_id") Long commentId) {
	 */
	
	@Test
	@DisplayName("특정 유저가 작성한 댓글 삭제 요청 처리 -> 정상, CommentService check")
	public void removeCommentByManagerTestCheckCommentService() {
		//Given
		Long userId = Long.valueOf(74);
		Long commentId = Long.valueOf(66);
		
		//When
		managerUserRestController.removeCommentByManager(userId, commentId);
		
		//Then
		then(commentService).should(times(1)).deleteById(commentId);
	}
	
	/*
	 * public void removePostByManager(@PathVariable("user_id") Long userId,@RequestParam("comment_id") Long commentId) {
	 */
	
	@Test
	@DisplayName("특정 유저가 작성한 댓글 삭제 요청 처리 -> 정상, PostService check")
	public void removePostByManagerTestCheckPostService() {
		//Given
		Long userId = Long.valueOf(25);
		Long exercisePostId = Long.valueOf(47);
		
		//When
		managerUserRestController.removePostByManager(userId, exercisePostId);
		
		//Then
		then(exercisePostService).should(times(1)).deleteById(exercisePostId);
	}
}
