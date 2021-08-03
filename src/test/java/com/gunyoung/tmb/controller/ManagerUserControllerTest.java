package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
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

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForManageViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostForManageViewDTO;
import com.gunyoung.tmb.dto.response.UserManageListDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * {@link ManagerUserController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
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
		user = getUserInstance(RoleType.USER);
		userRepository.save(user);
	}
	
	@AfterEach 
	void tearDown() {
		userRepository.deleteAll();
	}
	
	/**
	 *  --------------- 테스트 진행과정에 있어 필요한 리소스 반환 메소드들 --------------------- 
	 */
	
	private User getUserInstance(RoleType role) {
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234!")
				.firstName("first")
				.lastName("last")
				.nickName("nickName")
				.role(role)
				.build();
		
		return user;
	}
	
	private Comment getCommentInstance() {
		Comment comment = Comment.builder()
				.contents("contents")
				.isAnonymous(false)
				.writerIp("127.0.0.1")
				.build();
		return comment;
	}
	
	private ExercisePost getExercisePostInstance() {
		ExercisePost ep = ExercisePost.builder()
				.title("title")
				.contents("contents")
				.build();
		return ep;
	}
	
	private Long getNonExistUserId() {
		Long nonExistUserId = Long.valueOf(1);
		
		for(User u : userRepository.findAll()) {
			nonExistUserId = Math.max(nonExistUserId, u.getId());
		}
		nonExistUserId++;
		
		return nonExistUserId;
	}
	
	private Map<String, Object> getResponseModel(MvcResult mvcResult) {
		return mvcResult.getModelAndView().getModel();
	}
	
	/**
	 *  ---------------------------- 본 테스트 코드 ---------------------------------
	 */
	
	/*
	 * @RequestMapping(value="/manager/usermanage",method= RequestMethod.GET)
	 * public ModelAndView userManageView(@RequestParam(value="page", required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("매니저의 유저 검색 페이지 반환 -> 정상")
	public void userManageViewTest() throws Exception {
		//Given
		String nameKeyword = "first";
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/usermanage")
				.param("keyword", nameKeyword))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<UserManageListDTO> resultList = (List<UserManageListDTO>) model.get("listObject");
		
		assertEquals(1,resultList.size());
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
		Long nonExistUserId = getNonExistUserId();
		
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
		User user = getUserInstance(RoleType.ADMIN);
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
		Long nonExistUserId = getNonExistUserId();
		
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
	@DisplayName("특정 유저의 작성 댓글 화면 -> 정상")
	public void manageUserCommentsTest() throws Exception {
		//Given
		int commentNum = 10;
		List<Comment> commentList = new LinkedList<>();
		
		for(int i= 0 ; i<commentNum ; i++) {
			Comment comment = getCommentInstance();
			comment.setUser(user);
			commentList.add(comment);
		}
		
		commentRepository.saveAll(commentList);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/usermanage/" + user.getId() +"/comments"))
				
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		 Map<String, Object> model = getResponseModel(result);
		 
		 @SuppressWarnings("unchecked")
		List<CommentForManageViewDTO> listObject = (List<CommentForManageViewDTO>) model.get("commentList");
		assertEquals(Math.min(commentNum, PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE),listObject.size());
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
		Long nonExistUserId = getNonExistUserId();
		
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
	@DisplayName("특정 유저의 작성 게시글 화면 -> 정상")
	public void managerUserPostsTest() throws Exception {
		//Given
		int exercisePostNum = 10;
		List<ExercisePost> exercisePostList = new LinkedList<>();
		
		for(int i= 0 ; i<exercisePostNum ; i++) {
			ExercisePost exercisePost = getExercisePostInstance();
			exercisePost.setUser(user);
			exercisePostList.add(exercisePost);
		}
		
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/usermanage/" + user.getId() +"/posts"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		 Map<String, Object> model = getResponseModel(result);
		 
		 @SuppressWarnings("unchecked")
		List<ExercisePostForManageViewDTO> listObject = (List<ExercisePostForManageViewDTO>) model.get("postList");
		 
		 assertEquals(Math.min(exercisePostNum, PageUtil.POST_FOR_MANAGE_PAGE_SIZE),listObject.size());
	}
}
