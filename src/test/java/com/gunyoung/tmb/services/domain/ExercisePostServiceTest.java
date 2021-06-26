package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
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
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.utils.PageUtil;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class ExercisePostServiceTest {

	private static final int INIT_EXERCISE_POST_NUM = 30;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Autowired
	ExercisePostService exercisePostService;
	
	@BeforeEach
	void setup() {
		List<ExercisePost> list = new ArrayList<>();
		for(int i=1;i<=INIT_EXERCISE_POST_NUM;i++) {
			ExercisePost exercisePost = ExercisePost.builder()
									 .title("title" +i)
									 .contents("contents"+i)
									 .build();
									 
			list.add(exercisePost);
		}
		exercisePostRepository.saveAll(list);
	}
	
	@AfterEach
	void tearDown() {
		exercisePostRepository.deleteAll();
	}
	
	/*
	 *  public ExercisePost findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 ExercisePost 찾기 -> 해당 id의 exercisePost 없음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<ExercisePost> list = exercisePostRepository.findAll();
		
		for(ExercisePost c: list) {
			maxId = Math.max(maxId, c.getId());
		}
		
		//When
		ExercisePost result = exercisePostService.findById(maxId+ 1000);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 ExercisePost 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		ExercisePost exercisePost = exercisePostRepository.findAll().get(0);
		Long id = exercisePost.getId();
		
		//When
		ExercisePost result = exercisePostService.findById(id);
		
		//Then
		
		assertEquals(result != null, true);
		
	}
	
	/*
	 *  public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber)
	 */
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 객체들 가져오기 -> 정상")
	public void findAllForPostForCommunityViewDTOByPageTest() {
		//Given
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("test")
				.lastName("test")
				.nickName("test")
				.role(RoleType.USER)
				.build();
	
		userRepository.save(user);
		
		Exercise exercise = Exercise.builder()
			    .name("Exercies")
			    .description("Description")
			    .caution("Caution")
			    .movement("Movement")
			    .target(TargetType.CHEST)
			    .build();
		
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		
		// User, Exercise 의 setExercisePost는 해당 테스트에 필요없는 작업이라 배제
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		
		Page<PostForCommunityViewDTO> result = exercisePostService.findAllForPostForCommunityViewDTOByPage(1);
		
		//Then
		
		//Math.Min은 인자에 있는 값들이 추후에 변경될수 있는점 고려
		assertEquals(result.getContent().size(),Math.min(PageUtil.COMMUNITY_PAGE_SIZE,INIT_EXERCISE_POST_NUM));
	}
	
	/*
	 * public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(String keyword,
			Integer pageNumber)
	 */
	
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 객체들 키워드로 가져오기 -> 정상")
	public void findAllForPostForCommunityViewDTOWithKeywordByPageTest() {
		//Given
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("test")
				.lastName("test")
				.nickName("test")
				.role(RoleType.USER)
				.build();
	
		userRepository.save(user);
		
		Exercise exercise = Exercise.builder()
			    .name("Exercies")
			    .description("Description")
			    .caution("Caution")
			    .movement("Movement")
			    .target(TargetType.CHEST)
			    .build();
		
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		
		// User, Exercise 의 setExercisePost는 해당 테스트에 필요없는 작업이라 배제
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		
		// 내용 검색
		Page<PostForCommunityViewDTO> result1 = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage("ontent",1);
		
		//제목 검색
		Page<PostForCommunityViewDTO> result2 = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage("itle", 1);
		
		Page<PostForCommunityViewDTO> result3 = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage("none!!!", 1);
		
		//Then
		
		//Math.Min은 인자에 있는 값들이 추후에 변경될수 있는점 고려
		assertEquals(result1.getContent().size(),Math.min(PageUtil.COMMUNITY_PAGE_SIZE,INIT_EXERCISE_POST_NUM));
		assertEquals(result2.getContent().size(),Math.min(PageUtil.COMMUNITY_PAGE_SIZE,INIT_EXERCISE_POST_NUM));
		assertEquals(result3.getContent().size(),0);
	}
	
	/*
	 *  public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(TargetType target,
			Pageable pageable)
	 */
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 객체들 특정 target만 가져오기 -> 정상")
	public void  findAllForPostForCommunityViewDTOWithTargetByPage() {
		//Given
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("test")
				.lastName("test")
				.nickName("test")
				.role(RoleType.USER)
				.build();
	
		userRepository.save(user);
		
		Exercise exercise = Exercise.builder()
			    .name("Exercies")
			    .description("Description")
			    .caution("Caution")
			    .movement("Movement")
			    .target(TargetType.CHEST)
			    .build();
		
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		
		// User, Exercise 의 setExercisePost는 해당 테스트에 필요없는 작업이라 배제
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		
		Page<PostForCommunityViewDTO> result1 = exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(TargetType.ARM, 1);
		Page<PostForCommunityViewDTO> result2 = exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(TargetType.CHEST, 1);
		
		//Then
		assertEquals(result1.getContent().size(),0);
		assertEquals(result2.getContent().size(),Math.min(PageUtil.COMMUNITY_PAGE_SIZE,INIT_EXERCISE_POST_NUM));
	}
	
	/*
	 *  public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType target,
			String keyword, Integer pageNumber)
	 */
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 키워드로 객체들 특정 target만 가져오기 -> 정상")
	public void findAllForPostForCommunityViewDTOWithTargetAndKeywordByPageTest() {
		//Given
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("test")
				.lastName("test")
				.nickName("test")
				.role(RoleType.USER)
				.build();
	
		userRepository.save(user);
		
		Exercise exercise = Exercise.builder()
			    .name("Exercies")
			    .description("Description")
			    .caution("Caution")
			    .movement("Movement")
			    .target(TargetType.CHEST)
			    .build();
		
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		
		// User, Exercise 의 setExercisePost는 해당 테스트에 필요없는 작업이라 배제
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		
		Page<PostForCommunityViewDTO> result1 = exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType.CHEST, "none!!!", 1);
		Page<PostForCommunityViewDTO> result2 = exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType.CHEST, "title",1);
		
		//Then
		assertEquals(result1.getContent().size(),0);
		assertEquals(result2.getContent().size(),Math.min(PageUtil.COMMUNITY_PAGE_SIZE,INIT_EXERCISE_POST_NUM));
	}
	
	
	/*
	 *  public ExercisePost save(ExercisePost exercisePost)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		ExercisePost existExercisePost = exercisePostRepository.findAll().get(0);
		Long id = existExercisePost.getId();
		existExercisePost.setTitle("Changed Title");
		
		//When
		exercisePostService.save(existExercisePost);
		
		//Then
		ExercisePost result = exercisePostRepository.findById(id).get();
		assertEquals(result.getTitle(),"Changed Title");
	}
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 추가하기 -> 정상")
	public void saveTest() {
		//Given
		ExercisePost newExercisePost = ExercisePost.builder()
									.title("New Title")
									.contents("New contents")
									.build();
		Long beforeNum = exercisePostRepository.count();
		
		//When
		exercisePostService.save(newExercisePost);
		
		//Then
		assertEquals(beforeNum+1,exercisePostRepository.count());
	}
	
	/*
	 *  public void delete(ExercisePost exercisePost)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		ExercisePost existExercisePost = exercisePostRepository.findAll().get(0);
		Long beforeNum = exercisePostRepository.count();
		
		//When
		exercisePostService.delete(existExercisePost);
		
		//Then
		assertEquals(beforeNum-1,exercisePostRepository.count());
	}
	
	/*
	 *  public long countWithTitleAndContentsKeyword(String keyword)
	 */
	
	@Test
	@Transactional
	@DisplayName("키워드(제목, 내용)를 만족하는 게시글 개수 반환 -> 정상")
	public void countWithTitleAndContentsKeywordTest() {
		//Given
		String keywordEveryHas = "title";
		String keywordOnlyOneHas = String.valueOf(INIT_EXERCISE_POST_NUM);
		String keywordNoOneHas = "none!!!!";
		
		//When
		long resultEvery = exercisePostService.countWithTitleAndContentsKeyword(keywordEveryHas);
		long resultOnlyOne = exercisePostService.countWithTitleAndContentsKeyword(keywordOnlyOneHas);
		long resultNoOne = exercisePostService.countWithTitleAndContentsKeyword(keywordNoOneHas);
		
		//Then 
		assertEquals(resultEvery,INIT_EXERCISE_POST_NUM);
		assertEquals(resultOnlyOne,1);
		assertEquals(resultNoOne,0);
	}
	
	/*
	 *  public long countWithTarget(TargetType target)
	 */
	
	@Test
	@Transactional
	@DisplayName("해당 부위에 해당하는 Post만 개수 반환 -> 정상")
	public void countWithTargetTest() {
		//Given
		Exercise exercise = Exercise.builder()
			    .name("Exercies")
			    .description("Description")
			    .caution("Caution")
			    .movement("Movement")
			    .target(TargetType.CHEST)
			    .build();
		
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
		}
		
		// Exercise 의 setExercisePost는 해당 테스트에 필요없는 작업이라 배제
		exercisePostRepository.saveAll(exercisePostList);
		
		TargetType allType = TargetType.CHEST;
		TargetType nonType = TargetType.CORE;
		
		//When
		long resultEvery = exercisePostService.countWithTarget(allType);
		long resultNone = exercisePostService.countWithTarget(nonType);
		
		//Then
		assertEquals(resultEvery,INIT_EXERCISE_POST_NUM);
		assertEquals(resultNone,0);
	}
	
	/*
	 *  public long countWithTargetAndKeyword(TargetType target, String keyword)
	 */
	@Test
	@Transactional
	@DisplayName("해당 부위에 해당하고 이름과 제목에 키워드 포함하는 게시글 개수 반환 ->정상")
	public void countWithTargetAndKeywordTest() {
		//Given
		
		Exercise exercise = Exercise.builder()
			    .name("Exercies")
			    .description("Description")
			    .caution("Caution")
			    .movement("Movement")
			    .target(TargetType.CHEST)
			    .build();
		
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
		}
		
		// Exercise 의 setExercisePost는 해당 테스트에 필요없는 작업이라 배제
		exercisePostRepository.saveAll(exercisePostList);
		
		TargetType type = TargetType.CHEST;
		String everyKeyword = "title";
		String noneKeyword = "none!!!";
		
		//When
		long resultEvery = exercisePostService.countWithTargetAndKeyword(type, everyKeyword);
		long resultNone = exercisePostService.countWithTargetAndKeyword(type, noneKeyword);
		
		//Then
		assertEquals(resultEvery,INIT_EXERCISE_POST_NUM);
		assertEquals(resultNone,0);
		
	}
}
