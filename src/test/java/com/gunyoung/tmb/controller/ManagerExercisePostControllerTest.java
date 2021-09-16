package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
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
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.testutil.ControllerTest;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * {@link ManagerExercisePostController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerExercisePostControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private ExercisePostRepository exercisePostRepository;
	
	/*
	 * @RequestMapping(value="/manager/community", method = RequestMethod.GET)
	 * public ModelAndView manageCommunityView(@RequestParam(value="page", defaultValue="1") int page,
	 *		@RequestParam(value ="keyword", required=false) String keyword,ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("커뮤니티 매니징 메인 화면 반환 -> 정상, 모든 ExercisePost가 만족하는 키워드")
	public void manageCommunityViewTestKeywordForAll() throws Exception {
		//Given
		String keywordForAllExercisePost = ExercisePostTest.DEFAULT_TITLE;
		User user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance("exericse",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(10, exercisePostRepository);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		long givenExercisePostNum = exercisePostList.size();
		for(ExercisePost ep: exercisePostList) {
			ep.setUser(user);
			ep.setExercise(exercise);
			exercisePostRepository.save(ep);
		}
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/community")
				.param("keyword", keywordForAllExercisePost))
				
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> resultList = (Page<PostForCommunityViewDTO>) model.get("listObject");
		
		assertEquals(Math.min(PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize(), givenExercisePostNum), resultList.getContent().size());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("커뮤니티 매니징 메인 화면 반환 -> 정상, 어떤 ExercisePost도 만족하지 않는 키워드")
	public void manageCommunityViewTestKeywordForNothing() throws Exception {
		//Given
		String keywordForNothing = "nothing!!";
		User user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance("exericse",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(10, exercisePostRepository);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		for(ExercisePost ep: exercisePostList) {
			ep.setUser(user);
			ep.setExercise(exercise);
			exercisePostRepository.save(ep);
		}
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/community")
				.param("keyword", keywordForNothing))
				
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> resultList = (Page<PostForCommunityViewDTO>) model.get("listObject");
		
		assertEquals(0, resultList.getContent().size());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("커뮤니티 매니징 메인 화면 반환 -> 정상, 키워드 없음")
	public void manageCommunityViewTestNoKeyword() throws Exception {
		//Given
		User user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance("exericse",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(10, exercisePostRepository);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		for(ExercisePost ep: exercisePostList) {
			ep.setUser(user);
			ep.setExercise(exercise);
			exercisePostRepository.save(ep);
		}
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/community"))
				
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> resultList = (Page<PostForCommunityViewDTO>) model.get("listObject");
		
		assertEquals(0, resultList.getContent().size());
	}
}
