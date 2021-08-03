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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.utils.PageUtil;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link ExercisePostController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ExercisePostControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExercisePostRepository exercisePostRepository;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
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
	
	private Exercise getExerciseInstance(String name, TargetType target) {
		Exercise exercise = Exercise.builder()
				.name(name)
				.description("description")
				.caution("caution")
				.movement("movement")
				.target(target)
				.build();
		
		return exercise;
	}
	
	private ExercisePost getExercisePostInstance() {
		ExercisePost ep = ExercisePost.builder()
				.title("title")
				.contents("contents")
				.build();
		return ep;
	}
	
	private Map<String, Object> getResponseModel(MvcResult mvcResult) {
		return mvcResult.getModelAndView().getModel();
	}
	
	private MultiValueMap<String, String> getAddExercisePostDTOMap(String exerciseName) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", "title");
		map.add("contents", "contents");
		map.add("exerciseName", exerciseName);
		
		return map;
	}
	
	private MultiValueMap<String, String> getAddCommentDTOMap() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("contents", "contents");
		map.add("isAnonymous", "false");
		return map;
	}
	
	private Long getNonExistUserId() {
		Long nonExistUserId = Long.valueOf(1);
		
		for(User u : userRepository.findAll()) {
			nonExistUserId = Math.max(nonExistUserId, u.getId());
		}
		nonExistUserId++;
		
		return nonExistUserId;
	}
	
	private Long getNonExistExercisePostId() {
		Long nonExistExercisePostId = Long.valueOf(1);
		
		for(ExercisePost u : exercisePostRepository.findAll()) {
			nonExistExercisePostId = Math.max(nonExistExercisePostId, u.getId());
		}
		nonExistExercisePostId++;
		
		return nonExistExercisePostId;
	}
	
	/**
	 *  ---------------------------- 본 테스트 코드 ---------------------------------
	 */
	
	/*
	 * @RequestMapping(value="/community",method=RequestMethod.GET)
	 * public ModelAndView exercisePostView(@RequestParam(value="page", required = false,defaultValue="1") int page,
	 *		@RequestParam(value="keyword",required=false)String keyword,ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("커뮤니티 메인 화면 반환 -> 정상")
	public void exercisePostViewTest() throws Exception {
		//Given
		TargetType forAllTarget = TargetType.ARM;
		Exercise exercise = getExerciseInstance("exercise",forAllTarget);
		
		exerciseRepository.save(exercise);
		
		int totalEpNum = 10;
		List<ExercisePost> epList = new LinkedList<>();
		
		for(int i=0; i< totalEpNum ; i++) {
			ExercisePost ep = getExercisePostInstance();
			ep.setExercise(exercise);
			ep.setUser(user);
			epList.add(ep);
		}
		exercisePostRepository.saveAll(epList);
		
		//When
		MvcResult result = mockMvc.perform(get("/community")
				.param("keyword", "title"))
		
		//Then
		
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> resultMap = getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<PostForCommunityViewDTO> listObject = (List<PostForCommunityViewDTO> )resultMap.get("listObject");
		
		assertEquals(Math.min(totalEpNum, PageUtil.COMMUNITY_PAGE_SIZE), listObject.size());
	}
	
	
	/*
	 * @RequestMapping(value="/community/{target}",method = RequestMethod.GET)
	 * public ModelAndView exercisePostViewWithTarget(@RequestParam(value="page", required = false,defaultValue="1") int page
	 *		,@RequestParam(value="keyword",required=false)String keyword,ModelAndView mav,@PathVariable("target") String targetName)
	 */
	
	@Test
	@Transactional
	@DisplayName("특정 카테고리 게시글만 반환 -> 요청 카테고리 존재하지 않을때")
	public void exercisePostViewWithTargetNonExist() throws Exception {
		//Given
		String nonExistTargetName ="nonExist";
		
		//When
		mockMvc.perform(get("/community/" + nonExistTargetName))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	
	@SuppressWarnings("unchecked") // for Type Casting of Model
	@Test
	@Transactional
	@DisplayName("특정 카테고리 게시글만 반환 -> 정상")
	public void exercisePostViewWithTargetTest() throws Exception {
		//Given
		TargetType forAllTarget = TargetType.ARM;
		TargetType forNoneTarget = TargetType.BACK;
		Exercise exercise = getExerciseInstance("exercise",forAllTarget);
		
		exerciseRepository.save(exercise);
		
		int totalEpNum = 10;
		List<ExercisePost> epList = new LinkedList<>();
		
		for(int i=0; i< totalEpNum ; i++) {
			ExercisePost ep = getExercisePostInstance();
			ep.setExercise(exercise);
			ep.setUser(user);
			epList.add(ep);
		}
		
		exercisePostRepository.saveAll(epList);
		
		
		//When 1 (for all)
		MvcResult result = mockMvc.perform(get("/community/" + forAllTarget.toString()))
		
		//Then 1 (for all)
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> resultMap = getResponseModel(result);
		
		List<PostForCommunityViewDTO> listObject = (List<PostForCommunityViewDTO>)resultMap.get("listObject");
		assertEquals(Math.min(totalEpNum, PageUtil.COMMUNITY_PAGE_SIZE), listObject.size());
		
		//When 2 (for none)
		result = mockMvc.perform(get("/community/" + forNoneTarget.toString()))
		
		//Then 2 (for none)
				.andExpect(status().isOk())
				.andReturn();
		
		resultMap = getResponseModel(result);
		
		listObject = (List<PostForCommunityViewDTO>)resultMap.get("listObject");
		assertEquals(0, listObject.size());
	}
	
	/*
	 * @RequestMapping(value="/community/post/{post_id}" ,method = RequestMethod.GET)
	 * public ModelAndView exercisePostDetailView(@PathVariable("post_id") Long postId,ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("특정 게시글 화면 -> 해당 ID의 게시글 없을 때")
	public void exercisePostDetailViewNonExist() throws Exception {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		for(ExercisePost ep : exercisePostRepository.findAll()) {
			nonExistId = Math.max(nonExistId, ep.getId());
		}
		
		nonExistId++;
		
		//When
		mockMvc.perform(get("/community/post/" + nonExistId))
		
		//Then
		
				.andExpect(status().isNoContent());
	}
	
	@Test
	@Transactional
	@DisplayName("특정 게시글 화면 -> 정상")
	public void exercisePostDetailViewTest() throws Exception {
		//Given
		Exercise exercise = getExerciseInstance("exercise",TargetType.ARM);
		
		exerciseRepository.save(exercise);
		
		ExercisePost ep = getExercisePostInstance();
		ep.setUser(user);
		ep.setExercise(exercise);
		
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(get("/community/post/" + ep.getId()))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/community/post/addpost", method = RequestMethod.GET)
	 * public ModelAndView addExercisePostView(ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("게시글 작성 화면 반환 -> 정상")
	public void addExercisePostViewTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/community/post/addpost"))
		
		//Then
		
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/community/post/addpost", method = RequestMethod.POST)
	 * @LoginIdSessionNotNull
	 * public ModelAndView addExercisePost(@ModelAttribute AddExercisePostDTO dto, ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("게시글 추가 처리 -> 세션에 저장된ID에 해당하는 User 없을 때")
	public void addExercisePostUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = getNonExistUserId();
		
		String exerciseName = "exercise";
		Exercise exercise = getExerciseInstance(exerciseName,TargetType.ARM);
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(post("/community/post/addpost")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, nonExistUserId)
				.params(getAddExercisePostDTOMap(exerciseName)))
				
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(0, exercisePostRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("게시글 추가 처리 -> param의 exerciseName에 해당하는 Exercise 없을 때")
	public void addExercisePostNonExist() throws Exception {
		//Given
		String exerciseName = "exercise";
		Exercise exercise = getExerciseInstance(exerciseName,TargetType.ARM);
		exerciseRepository.save(exercise);
		
		String nonExistExerciseName = "nonExist";
		
		//When
		mockMvc.perform(post("/community/post/addpost")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(getAddExercisePostDTOMap(nonExistExerciseName)))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(0, exercisePostRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("게시글 추가 처리 -> 정상")
	public void addExercisePostTest() throws Exception {
		//Given
		String exerciseName = "exercise";
		Exercise exercise = getExerciseInstance(exerciseName,TargetType.ARM);
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(post("/community/post/addpost")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(getAddExercisePostDTOMap(exerciseName)))
		
		//Then
				.andExpect(redirectedUrl("/community"));
		
		assertEquals(1, exercisePostRepository.count());
	}
	
	/*
	 * @RequestMapping(value="/community/post/{post_id}/addComment",method = RequestMethod.POST)
	 * @LoginIdSessionNotNull
	 * public ModelAndView addCommentToExercisePost(@PathVariable("post_id") Long postId,@ModelAttribute AddCommentDTO dto,
	 *		@RequestParam("isAnonymous") boolean isAnonymous,HttpServletRequest request)
	 */
	
	@Test
	@Transactional
	@DisplayName("게시글에 댓글 추가 처리 -> 세션에 저장된 ID의 유저 없을 때")
	public void addCommentToExercisePostuserNonExist() throws Exception {
		//Given
		Long nonExistUserId = getNonExistUserId();
		
		Exercise exercise = getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		ExercisePost ep = getExercisePostInstance();
		ep.setUser(user);
		ep.setExercise(exercise);
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(post("/community/post/" + ep.getId() + "/addComment")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, nonExistUserId)
				.params(getAddCommentDTOMap())
				.param("isAnonymous", "false"))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(0,commentRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("게시글에 댓글 추가 처리 -> 해당 ID의 ExercisePost 없을때")
	public void addCommentToExercisePostNonExist() throws Exception {
		//Given
		Exercise exercise = getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		ExercisePost ep = getExercisePostInstance();
		ep.setUser(user);
		ep.setExercise(exercise);
		exercisePostRepository.save(ep);
		
		Long nonExistExercisePostId = getNonExistExercisePostId();
		
		//When
		mockMvc.perform(post("/community/post/" + nonExistExercisePostId + "/addComment")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(getAddCommentDTOMap())
				.param("isAnonymous", "false"))
		
		//Then
				.andExpect(status().isNoContent());
		assertEquals(0,commentRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("게시글에 댓글 추가 처리 -> 정상")
	public void addCommentToExercisePostTest() throws Exception {
		//Given
		Exercise exercise = getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		ExercisePost ep = getExercisePostInstance();
		ep.setUser(user);
		ep.setExercise(exercise);
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(post("/community/post/" + ep.getId() + "/addComment")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(getAddCommentDTOMap())
				.param("isAnonymous", "false"))
		
		//Then
				.andExpect(redirectedUrl("/community/post/" + ep.getId()));
		
		assertEquals(1,commentRepository.count());
	}
}
