package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForManageViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostForManageViewDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.util.CommentTest;
import com.gunyoung.tmb.util.ControllerTest;
import com.gunyoung.tmb.util.ExercisePostTest;
import com.gunyoung.tmb.util.UserTest;
import com.gunyoung.tmb.utils.PageUtil;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link UserController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

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
	 * @RequestMapping(value="/",method=RequestMethod.GET)
	 * public ModelAndView index(ModelAndView mav)
	 */
	@Test
	@Transactional
	@DisplayName("메인 화면 반환 -> 정상")
	public void indexTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/"))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/login",method=RequestMethod.GET)
	 * public ModelAndView loginView(HttpServletRequest request,ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("로그인 화면 반환 -> 정상")
	public void loginTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/login"))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/join",method=RequestMethod.GET)
	 * public ModelAndView joinView(@ModelAttribute("formModel")UserJoinDTO formModel,ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("회원 가입 화면 -> 정상")
	public void joinViewTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/join"))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/join",method=RequestMethod.POST)
	 * public ModelAndView join(@ModelAttribute("formModel")UserJoinDTO formModel)
	 */
	
	@Test
	@Transactional
	@DisplayName("회원 가입 처리 -> email 중복")
	public void joinEmailDuplication() throws Exception {
		//Given
		String newNickName = "nonExistNickName";
		MultiValueMap<String, String> paramMap = UserTest.getUserJoinDTOMap(user.getEmail(),newNickName);
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(status().isConflict());
		
		assertEquals(1,userRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("회원 가입 처리 -> nickName 중복")
	public void joinNickNameDuplication() throws Exception {
		//Given
		String newEmail = "nonexist@test.net";
		MultiValueMap<String, String> paramMap = UserTest.getUserJoinDTOMap(newEmail,user.getNickName());
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(status().isConflict());
		
		assertEquals(1,userRepository.count());	
	}
	
	@Test
	@Transactional
	@DisplayName("회원 가입 처리 -> 정상")
	public void joinTest() throws Exception {
		//Given
		String newNickName = "second";
		String newEmail = "nonexist@test.net";
		MultiValueMap<String, String> paramMap = UserTest.getUserJoinDTOMap(newEmail,newNickName);
		
		//When
		mockMvc.perform(post("/join")
				.params(paramMap))
		
		//Then
				.andExpect(redirectedUrl("/"));
		
		assertEquals(2,userRepository.count());
	}
	
	/*
	 * @RequestMapping(value="/user/profile",method=RequestMethod.GET)
	 * @LoginIdSessionNotNull
	 * public ModelAndView profileView(ModelAndView mav)
	 */
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("내 프로필 화면 반환하기 -> 세션에 저장된 ID의 User 없을 때")
	public void profileViewNonExist() throws Exception {
		//Given
		Long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		//When
		mockMvc.perform(get("/user/profile")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, nonExistUserId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("내 프로필 화면 반환하기 -> 정상")
	public void profileViewTest() throws Exception {
		//Given
		Long userId = user.getId();
		
		//When
		mockMvc.perform(get("/user/profile")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, userId))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/user/profile/mycomments", method=RequestMethod.GET)
	 * @LoginIdSessionNotNull
	 * public ModelAndView myCommentsView(ModelAndView mav,@RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order)
	 */
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("접속자 본인이 작성한 댓글 목록 화면 반환 -> 세션에 저장된 Id에 해당하는 User 없을때")
	public void myCommentsViewUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		//When
		mockMvc.perform(get("/user/profile/mycomments")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, nonExistUserId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("접속자 본인이 작성한 댓글 목록 화면 반환 -> 잘못된 검색 정렬 조건")
	public void myCommentsViewBadCriteria() throws Exception {
		//Given
		String badOrder = "badbad";
		
		//When
		mockMvc.perform(get("/user/profile/mycomments")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.param("order", badOrder))
		//Then
				.andExpect(status().isBadRequest());
	}
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("접속자 본인이 작성한 댓글 목록 화면 반환 -> 정상")
	public void myCommentsViewTest() throws Exception {
		//Given
		int commentNum = 10;
		List<Comment> commentList = new LinkedList<>();
		
		for(int i= 0 ; i<commentNum ; i++) {
			Comment comment = CommentTest.getCommentInstance();
			comment.setUser(user);
			commentList.add(comment);
		}
		
		commentRepository.saveAll(commentList);
		
		//When
		MvcResult result = mockMvc.perform(get("/user/profile/mycomments")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId()))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<CommentForManageViewDTO> listObject = (List<CommentForManageViewDTO>) model.get("commentList");
		assertEquals(Math.min(commentNum, PageUtil.COMMENT_FOR_PROFILE_PAGE_SIZE),listObject.size());
	}
	
	/*
	 * @RequestMapping(value="/user/profile/myposts",method=RequestMethod.GET)
	 * @LoginIdSessionNotNull
	 * public ModelAndView myPostsView(ModelAndView mav, @RequestParam(value="page", required=false,defaultValue="1") Integer page
	 *		,@RequestParam(value="order", defaultValue="desc") String order )
	 */
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("접속자 본인이 작성한 게시글 목록 화면 반환 -> 세션에 저장된 ID의 User 없을 때")
	public void myPostsViewUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		//When
		mockMvc.perform(get("/user/profile/myposts")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, nonExistUserId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("접속자 본인이 작성한 게시글 목록 화면 반환 -> 잘못된 검색 정렬 조건")
	public void myPostsViewBadCriteria() throws Exception {
		//Given
		String badOrder = "badbad";
		
		//When
		mockMvc.perform(get("/user/profile/myposts")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.param("order", badOrder))
		//Then
				.andExpect(status().isBadRequest());
	}
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("접속자 본인이 작성한 게시글 목록 화면 반환 -> 정상")
	public void myPostsViewTest() throws Exception {
		//Given
		int exercisePostNum = 10;
		List<ExercisePost> exercisePostList = new LinkedList<>();
		
		for(int i= 0 ; i<exercisePostNum ; i++) {
			ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
			exercisePost.setUser(user);
			exercisePostList.add(exercisePost);
		}
		
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		MvcResult result = mockMvc.perform(get("/user/profile/myposts")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId()))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		 
		 @SuppressWarnings("unchecked")
		List<ExercisePostForManageViewDTO> listObject = (List<ExercisePostForManageViewDTO>) model.get("postList");
		 
		 assertEquals(Math.min(exercisePostNum, PageUtil.POST_FOR_PROFILE_PAGE_SIZE),listObject.size());
	}
}
