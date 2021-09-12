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
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.util.CommentTest;
import com.gunyoung.tmb.util.ControllerTest;
import com.gunyoung.tmb.util.ExercisePostTest;
import com.gunyoung.tmb.util.ExerciseTest;
import com.gunyoung.tmb.util.UserTest;
import com.gunyoung.tmb.util.tag.Integration;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link ExercisePostController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
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
		user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
	}
	
	@AfterEach 
	void tearDown() {
		userRepository.deleteAll();
	}
	
	/*
	 * @RequestMapping(value="/community",method=RequestMethod.GET)
	 * public ModelAndView exercisePostView(@RequestParam(value="page", required = false,defaultValue="1") int page,
	 *		@RequestParam(value="keyword",required=false)String keyword,ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("커뮤니티 메인 화면 반환 -> 정상, 모든 ExercisePost 만족하는 키워드 포함")
	public void exercisePostViewTesKeywordForAllExercisePost() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		int givenExercisePostNum = 10;
		List<ExercisePost> exercisePostList = new LinkedList<>();
		
		for(int i=0; i< givenExercisePostNum ; i++) {
			ExercisePost ep = ExercisePostTest.getExercisePostInstance();
			ep.setExercise(exercise);
			ep.setUser(user);
			exercisePostList.add(ep);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		String keywordForallExercisePost = ExercisePostTest.getExercisePostInstance().getTitle();
		
		//When
		MvcResult result = mockMvc.perform(get("/community")
				.param("keyword", keywordForallExercisePost))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> resultMap = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> listObject = (Page<PostForCommunityViewDTO> )resultMap.get("listObject");
		
		assertEquals(Math.min(givenExercisePostNum, PageSize.COMMUNITY_PAGE_SIZE.getSize()), listObject.getNumberOfElements());
	}
	
	@Test
	@Transactional
	@DisplayName("커뮤니티 메인 화면 반환 -> 정상, 키워드 없음")
	public void exercisePostViewTestKeywordNull() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		int givenExercisePostNum = 10;
		List<ExercisePost> exercisePostList = new LinkedList<>();
		
		for(int i=0; i< givenExercisePostNum ; i++) {
			ExercisePost ep = ExercisePostTest.getExercisePostInstance();
			ep.setExercise(exercise);
			ep.setUser(user);
			exercisePostList.add(ep);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		MvcResult result = mockMvc.perform(get("/community"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> resultMap = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> listObject = (Page<PostForCommunityViewDTO> )resultMap.get("listObject");
		
		assertEquals(Math.min(givenExercisePostNum, PageSize.COMMUNITY_PAGE_SIZE.getSize()), listObject.getNumberOfElements());
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
	
	@Test
	@Transactional
	@DisplayName("특정 카테고리 게시글만 반환 -> 정상, 키워드는 없음, 모든 ExercisePost가 만족하는 TargetType")
	public void exercisePostViewWithTargetTestNoKeywordTargetTypeForAll() throws Exception {
		//Given
		TargetType forAllTarget = TargetType.ARM;
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",forAllTarget);
		exerciseRepository.save(exercise);
		
		int givenExercisePostNum = 10;
		saveExercisePostListWithSettingUserAndExercise(givenExercisePostNum, user, exercise);
		
		//When
		MvcResult result = mockMvc.perform(get("/community/" + forAllTarget.toString()))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> resultMap = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> listObject = (Page<PostForCommunityViewDTO>)resultMap.get("listObject");
		assertEquals(Math.min(givenExercisePostNum, PageSize.COMMUNITY_PAGE_SIZE.getSize()), listObject.getNumberOfElements());
	}
	
	@Test
	@Transactional
	@DisplayName("특정 카테고리 게시글만 반환 -> 정상, 키워드는 없음, 어떤 ExercisePost도 만족하지 않는 TargetType") 
	public void exercisePostViewWithTargetTestNoKeywordTargetTypeForNothing() throws Exception {
		//Given
		TargetType forNoneTarget = TargetType.BACK;
		
		TargetType forAllTarget = TargetType.ARM;
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",forAllTarget);
		exerciseRepository.save(exercise);
		
		int givenExercisePostNum = 10;
		saveExercisePostListWithSettingUserAndExercise(givenExercisePostNum, user, exercise);
		
		//When
		MvcResult result = mockMvc.perform(get("/community/" + forNoneTarget.toString()))
				
		//Then 2 (for none)
				.andExpect(status().isOk())
				.andReturn();
				
		Map<String, Object> resultMap = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> listObject = (Page<PostForCommunityViewDTO>)resultMap.get("listObject");
		assertEquals(0, listObject.getNumberOfElements());
	}
	
	@Test
	@Transactional
	@DisplayName("특정 카테고리 게시글만 반환 -> 정상, 모든 ExercisePost가 만족하는 키워드, 모든 ExercisePost가 만족하는 TargetType")
	public void exercisePostViewWithTargetTestKeywordForAllTargetTypeForAll() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		int givenExercisePostNum = 10;
		saveExercisePostListWithSettingUserAndExercise(givenExercisePostNum, user, exercise);
		
		TargetType targetTypeForAll = exercise.getTarget();
		String keywordForAllExercisePost = ExercisePostTest.getExercisePostInstance().getTitle();
		
		//When
		MvcResult result = mockMvc.perform(get("/community/" + targetTypeForAll.toString())
				.param("keyword", keywordForAllExercisePost))
				.andExpect(status().isOk())
				.andReturn();
		//Then
		Map<String, Object> resultMap = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> listObject = (Page<PostForCommunityViewDTO>)resultMap.get("listObject");
		assertEquals(Math.min(givenExercisePostNum, PageSize.COMMUNITY_PAGE_SIZE.getSize()), listObject.getNumberOfElements());
	}
	
	@Test
	@Transactional
	@DisplayName("특정 카테고리 게시글만 반환 -> 정상, 모든 ExercisePost가 만족하는 키워드, 모든 ExercisePost가 만족하는 TargetType")
	public void exercisePostViewWithTargetTestKeywordForNothingTargetTypeForAll() throws Exception {
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		int givenExercisePostNum = 10;
		saveExercisePostListWithSettingUserAndExercise(givenExercisePostNum, user, exercise);
		
		TargetType targetTypeForAll = exercise.getTarget();
		String keywordForNothing = "nothing!!!";
		
		//When
		MvcResult result = mockMvc.perform(get("/community/" + targetTypeForAll.toString())
				.param("keyword", keywordForNothing))
				.andExpect(status().isOk())
				.andReturn();
		//Then
		Map<String, Object> resultMap = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> listObject = (Page<PostForCommunityViewDTO>)resultMap.get("listObject");
		assertEquals(0, listObject.getNumberOfElements());
	}
	
	private void saveExercisePostListWithSettingUserAndExercise(int exercisePostNum, User user, Exercise exercise) {
		List<ExercisePost> exercisePostList = new LinkedList<>();
		for(int i=0; i< exercisePostNum ; i++) {
			ExercisePost ep = ExercisePostTest.getExercisePostInstance();
			ep.setExercise(exercise);
			ep.setUser(user);
			exercisePostList.add(ep);
		}
		exercisePostRepository.saveAll(exercisePostList);
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
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",TargetType.ARM);
		
		exerciseRepository.save(exercise);
		
		ExercisePost ep = ExercisePostTest.getExercisePostInstance();
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
	 * public ModelAndView addExercisePost(@ModelAttribute SaveExercisePostDTO dto, ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("게시글 추가 처리 -> 세션에 저장된ID에 해당하는 User 없을 때")
	public void addExercisePostUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		String exerciseName = "exercise";
		Exercise exercise = ExerciseTest.getExerciseInstance(exerciseName,TargetType.ARM);
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(post("/community/post/addpost")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, nonExistUserId)
				.params(ExercisePostTest.getSaveExercisePostDTOMap(exerciseName)))
				
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
		Exercise exercise = ExerciseTest.getExerciseInstance(exerciseName,TargetType.ARM);
		exerciseRepository.save(exercise);
		
		String nonExistExerciseName = "nonExist";
		
		//When
		mockMvc.perform(post("/community/post/addpost")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(ExercisePostTest.getSaveExercisePostDTOMap(nonExistExerciseName)))
		
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
		Exercise exercise = ExerciseTest.getExerciseInstance(exerciseName,TargetType.ARM);
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(post("/community/post/addpost")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(ExercisePostTest.getSaveExercisePostDTOMap(exerciseName)))
		
		//Then
				.andExpect(redirectedUrl("/community"));
		
		assertEquals(1, exercisePostRepository.count());
	}
	
	/*
	 * @RequestMapping(value="/community/post/{post_id}/addComment",method = RequestMethod.POST)
	 * @LoginIdSessionNotNull
	 * public ModelAndView addCommentToExercisePost(@PathVariable("post_id") Long postId,@ModelAttribute SaveCommentDTO dto,
	 *		@RequestParam("isAnonymous") boolean isAnonymous,HttpServletRequest request)
	 */
	
	@Test
	@Transactional
	@DisplayName("게시글에 댓글 추가 처리 -> 세션에 저장된 ID의 유저 없을 때")
	public void addCommentToExercisePostuserNonExist() throws Exception {
		//Given
		Long nonExistUserId = UserTest.getNonExistUserId(userRepository);
		
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		ep.setUser(user);
		ep.setExercise(exercise);
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(post("/community/post/" + ep.getId() + "/addComment")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, nonExistUserId)
				.params(CommentTest.getSaveCommentDTOMap())
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
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		ep.setUser(user);
		ep.setExercise(exercise);
		exercisePostRepository.save(ep);
		
		Long nonExistExercisePostId = ExercisePostTest.getNonExistExercisePostId(exercisePostRepository);
		
		//When
		mockMvc.perform(post("/community/post/" + nonExistExercisePostId + "/addComment")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(CommentTest.getSaveCommentDTOMap())
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
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		ep.setUser(user);
		ep.setExercise(exercise);
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(post("/community/post/" + ep.getId() + "/addComment")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(CommentTest.getSaveCommentDTOMap())
				.param("isAnonymous", "false"))
		
		//Then
				.andExpect(redirectedUrl("/community/post/" + ep.getId()));
		
		assertEquals(1,commentRepository.count());
	}
}
