package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForManageViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostForManageViewDTO;
import com.gunyoung.tmb.dto.response.UserManageListDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.testutil.CommentTest;
import com.gunyoung.tmb.testutil.ControllerTest;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * {@link ManagerUserController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerUserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ExercisePostRepository exercisePostRepository;
	
	private User user;
	
	@BeforeEach
	void setup() {
		user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
	}
	
	@AfterEach 
	void tearDown() {
		userRepository.deleteAll();
	}

	/*
	 * @RequestMapping(value="/manager/usermanage",method= RequestMethod.GET)
	 * public ModelAndView userManageView(@RequestParam(value="page", required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("매니저의 유저 검색 페이지 반환 -> 정상, 키워드 존재")
	public void userManageViewTest() throws Exception {
		//Given
		String nameKeyword = "first";
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/usermanage")
				.param("keyword", nameKeyword))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<UserManageListDTO> resultList = (List<UserManageListDTO>) model.get("listObject");
		
		assertEquals(1,resultList.size());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("매니저의 유저 검색 페이지 반환 -> 정상, 키워드 없음")
	public void userManageViewTestNoKeyword() throws Exception {
		//Given
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/usermanage"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<UserManageListDTO> resultList = (List<UserManageListDTO>) model.get("listObject");
		
		assertEquals(0,resultList.size());
	}
	
	/*
	 * @RequestMapping(value="/manager/usermanage/{user_id}" , method = RequestMethod.GET)
	 * public ModelAndView manageUserProfileView(@PathVariable("user_id") Long userId, ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 User 관리 화면 반환 -> 해당 ID의 User 없을 때")
	public void manageUserProfileViewNonExist() throws Exception {
		//Given
		Long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		//When
		mockMvc.perform(get("/manager/usermanage/" + nonExistUserId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 User 관리 화면 반환 -> 접속자의 권한이 대상 User의 권한보다 낮을 때")
	public void manageUserProfileViewAccessDenied() throws Exception {
		//Given
		User user = UserTest.getUserInstance(RoleType.ADMIN);
		user.setEmail("second@test.com");
		userRepository.save(user);
		
		//When
		mockMvc.perform(get("/manager/usermanage/" +user.getId()))
		
		//Then
				.andExpect(status().isBadRequest());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 User 관리 화면 반환 -> 정상")
	public void manageUserProfileViewTest() throws Exception {
		//Given
		Long userId = user.getId();
		
		//When
		mockMvc.perform(get("/manager/usermanage/" +userId))
		
		//Then
		
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/manager/usermanage/{user_id}/comments", method = RequestMethod.GET) 
	 * public ModelAndView manageUserComments(@PathVariable("user_id") Long userId,@RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order ,ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 작성 댓글 화면 -> 해당 Id의 유저 없을 때")
	public void manageUserCommentsUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		//When
		mockMvc.perform(get("/manager/usermanage/" + nonExistUserId +"/comments"))
		
		//then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 작성 댓글 화면 -> 잘못된 검색 결과 정렬 방식")
	public void manageUserCommentsBadRequest() throws Exception {
		//Given
		String badOrder = "badbad";
		
		//When
		mockMvc.perform(get("/manager/usermanage/" + user.getId() +"/comments")
				.param("order", badOrder))
		//Then
				.andExpect(status().isBadRequest());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 작성 댓글 화면 -> 정상, 최신순으로 정렬")
	public void manageUserCommentsTestSortingDESC() throws Exception {
		//Given
		int givenCommentNum = 10;
		CommentTest.addCommentsInDBWithSettingUser(givenCommentNum,user,commentRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/usermanage/" + user.getId() +"/comments"))
				
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		 Map<String, Object> model = ControllerTest.getResponseModel(result);
		 
		 @SuppressWarnings("unchecked")
		List<CommentForManageViewDTO> listObject = (List<CommentForManageViewDTO>) model.get("commentList");
		assertEquals(Math.min(givenCommentNum, PageSize.COMMENT_FOR_MANAGE_PAGE_SIZE.getSize()), listObject.size());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 작성 댓글 화면 -> 정상, 오래된순으로 정렬")
	public void manageUserCommentsTestSortingASC() throws Exception {
		//Given
		int givenCommentNum = 10;
		CommentTest.addCommentsInDBWithSettingUser(givenCommentNum,user,commentRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/usermanage/" + user.getId() +"/comments")
				.param("order", "asc"))
				
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		 Map<String, Object> model = ControllerTest.getResponseModel(result);
		 
		 @SuppressWarnings("unchecked")
		List<CommentForManageViewDTO> listObject = (List<CommentForManageViewDTO>) model.get("commentList");
		assertEquals(Math.min(givenCommentNum, PageSize.COMMENT_FOR_MANAGE_PAGE_SIZE.getSize()), listObject.size());
	}
	
	/*
	 * @RequestMapping(value="/manager/usermanage/{user_id}/posts",method=RequestMethod.GET)
	 * public ModelAndView managerUserPosts(@PathVariable("user_id") Long userId, @RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order ,ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 작성 게시글 화면 -> 해당 ID의 User 없을 때")
	public void managerUserPostsUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		//When
		mockMvc.perform(get("/manager/usermanage/" + nonExistUserId +"/posts"))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 작성 게시글 화면 -> 검색 결과 정렬 방식이 올바르지 못할 떄")
	public void managerUserPostsAccessDenied() throws Exception {
		//Given
		String badOrder = "badbad";
		
		//When
		mockMvc.perform(get("/manager/usermanage/" + user.getId() +"/posts")
				.param("order", badOrder))
		//Then
				.andExpect(status().isBadRequest());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 작성 게시글 화면 -> 정상, 정렬 최신순으로")
	public void managerUserPostsTestSortDesc() throws Exception {
		//Given
		int exercisePostNum = 10;
		ExercisePostTest.addNewExercisePostsInDBWithSettingUser(exercisePostNum, user, exercisePostRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/usermanage/" + user.getId() +"/posts"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		 Map<String, Object> model = ControllerTest.getResponseModel(result);
		 
		 @SuppressWarnings("unchecked")
		List<ExercisePostForManageViewDTO> listObject = (List<ExercisePostForManageViewDTO>) model.get("postList");
		 
		 assertEquals(Math.min(exercisePostNum, PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize()),listObject.size());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 작성 게시글 화면 -> 정상, 정렬 오래된순으로")
	public void managerUserPostsTestSortAsc() throws Exception {
		//Given
		int exercisePostNum = 10;
		ExercisePostTest.addNewExercisePostsInDBWithSettingUser(exercisePostNum, user, exercisePostRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/usermanage/" + user.getId() +"/posts")
				.param("order", "asc"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		 Map<String, Object> model = ControllerTest.getResponseModel(result);
		 
		 @SuppressWarnings("unchecked")
		List<ExercisePostForManageViewDTO> listObject = (List<ExercisePostForManageViewDTO>) model.get("postList");
		 
		 assertEquals(Math.min(exercisePostNum, PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize()),listObject.size());
	}
}
