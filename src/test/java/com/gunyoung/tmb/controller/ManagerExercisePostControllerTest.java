package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
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
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * {@link ManagerExercisePostController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
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
	
	/**
	 *  ---------------------------- 본 테스트 코드 ---------------------------------
	 */
	
	/*
	 * @RequestMapping(value="/manager/community", method = RequestMethod.GET)
	 * public ModelAndView manageCommunityView(@RequestParam(value="page", defaultValue="1") int page,
	 *		@RequestParam(value ="keyword", required=false) String keyword,ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("커뮤니티 매니징 메인 화면 반환 -> 정상")
	public void manageCommunityViewTest() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		Exercise exercise = getExerciseInstance("exericse",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		int exercisePostNum = 10;
		List<ExercisePost> epList = new LinkedList<>();
		
		for(int i=1; i<= exercisePostNum; i++) {
			ExercisePost ep = getExercisePostInstance();
			ep.setUser(user);
			ep.setExercise(exercise);
			epList.add(ep);
		
		}
		exercisePostRepository.saveAll(epList);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/community")
				.param("keyword", "title"))
				
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<PostForCommunityViewDTO> resultList = (Page<PostForCommunityViewDTO>) model.get("listObject");
		
		assertEquals(Math.min(PageUtil.POST_FOR_MANAGE_PAGE_SIZE, exercisePostNum), resultList.getContent().size());
	}
}
