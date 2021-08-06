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
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.utils.PageUtil;

@SpringBootTest
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
	 *  public List<ExercisePost> findAllByUserIdOrderByCreatedAtASCCustom(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 ExercisePost들 오래된 순으로 찾기 ->정상")
	public void findAllByUserIdOrderByCreatedAtASCCustomTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePosts) {
			ep.setUser(user);
		}
		
		exercisePostRepository.saveAll(exercisePosts);
		
		//When
		List<ExercisePost> result = exercisePostService.findAllByUserIdOrderByCreatedAtAsc(user.getId(),1,PageUtil.POST_FOR_MANAGE_PAGE_SIZE).getContent();
		
		//Then
		assertEquals(result.size(),Math.min(INIT_EXERCISE_POST_NUM, PageUtil.POST_FOR_MANAGE_PAGE_SIZE));
		assertEquals(result.get(0).getCreatedAt().isBefore(result.get(1).getCreatedAt()),true);
	}
	
	/*
	 * public List<Comment> findAllByUserIdOrderByCreatedAtDesc(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 ExercisePost들 최신 순으로 찾기 ->정상")
	public void findAllByUserIdOrderByCreatedAtDescTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePosts) {
			ep.setUser(user);
		}
		
		exercisePostRepository.saveAll(exercisePosts);
		
		//When
		List<ExercisePost> result = exercisePostService.findAllByUserIdOrderByCreatedAtDesc(user.getId(),1,PageUtil.POST_FOR_MANAGE_PAGE_SIZE).getContent();
		
		//Then
		assertEquals(result.size(),Math.min(INIT_EXERCISE_POST_NUM, PageUtil.POST_FOR_MANAGE_PAGE_SIZE));
		assertEquals(result.get(0).getCreatedAt().isAfter(result.get(1).getCreatedAt()),true);
	}
	
	/*
	 *  public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber)
	 */
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 객체들 가져오기 -> 정상")
	public void findAllForPostForCommunityViewDTOByPageTest() {
		//Given
		User user = getUserInstance();
	
		userRepository.save(user);
		
		Exercise exercise = getExerciseInstance();
		
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		
		// User, Exercise 의 setExercisePost는 해당 테스트에 필요없는 작업이라 배제
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		
		Page<PostForCommunityViewDTO> result = exercisePostService.findAllForPostForCommunityViewDTOByPage(1,PageUtil.COMMUNITY_PAGE_SIZE);
		
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
		User user = getUserInstance();
	
		userRepository.save(user);
		
		Exercise exercise = getExerciseInstance();
		
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
		Page<PostForCommunityViewDTO> result1 = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage("ontent",1,PageUtil.COMMUNITY_PAGE_SIZE);
		
		//제목 검색
		Page<PostForCommunityViewDTO> result2 = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage("itle", 1,PageUtil.COMMUNITY_PAGE_SIZE);
		
		Page<PostForCommunityViewDTO> result3 = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage("none!!!", 1,PageUtil.COMMUNITY_PAGE_SIZE);
		
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
		User user = getUserInstance();
	
		userRepository.save(user);
		
		Exercise exercise = getExerciseInstance();
		
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		
		// User, Exercise 의 setExercisePost는 해당 테스트에 필요없는 작업이라 배제
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		
		Page<PostForCommunityViewDTO> result1 = exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(TargetType.ARM, 1,PageUtil.COMMUNITY_PAGE_SIZE);
		Page<PostForCommunityViewDTO> result2 = exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(TargetType.CHEST, 1,PageUtil.COMMUNITY_PAGE_SIZE);
		
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
		User user = getUserInstance();
	
		userRepository.save(user);
		
		Exercise exercise = getExerciseInstance();
		
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		
		// User, Exercise 의 setExercisePost는 해당 테스트에 필요없는 작업이라 배제
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		
		Page<PostForCommunityViewDTO> result1 = exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType.CHEST, "none!!!", 1,PageUtil.COMMUNITY_PAGE_SIZE);
		Page<PostForCommunityViewDTO> result2 = exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType.CHEST, "title",1,PageUtil.COMMUNITY_PAGE_SIZE);
		
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
	 * public ExercisePost saveWithUserAndExercise(ExercisePost exercisePost, User user, Exercise exericse)
	 */
	@Test
	@Transactional
	@DisplayName("User, Exercise와 연결 짓고 저장하기 ->정상")
	public void saveWithUserAndExerciseTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		Exercise exercise = getExerciseInstance();
		
		exerciseRepository.save(exercise);
		
		ExercisePost exercisePost = ExercisePost.builder()
				.title("new Title")
				.contents("new contents")
				.build();
		
		long exercisePostNum = exercisePostRepository.count();
		int userExercisePostsNum = user.getExercisePosts().size();
		int exerciseExercisePostsNum = exercise.getExercisePosts().size();
		
		//When
		
		exercisePostService.saveWithUserAndExercise(exercisePost, user, exercise);
		
		//Then
		assertEquals(exercisePostNum +1, exercisePostRepository.count());
		assertEquals(userExercisePostsNum +1, userRepository.findById(user.getId()).get().getExercisePosts().size());
		assertEquals(exerciseExercisePostsNum +1, exerciseRepository.findById(exercise.getId()).get().getExercisePosts().size());
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
	 * public void checkIsMineAndDelete(Long userId, Long exercisePostId) {
	 */
	
	@Test
	@Transactional
	@DisplayName("User ID, ExercisePost ID로 찾고 존재하면 삭제 -> 존재하지 않음")
	public void checkIsMineAndDeleteNotMine() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		ExercisePost ep = exercisePostRepository.findAll().get(0);
		ep.setUser(user);
		
		exercisePostRepository.save(ep);
		
		Long otherUserId = user.getId() + 1;
		Long exercisePostId = ep.getId();
		
		//When
		exercisePostService.checkIsMineAndDelete(otherUserId, exercisePostId);
		
		//Then
		
		assertEquals(true, exercisePostRepository.existsById(exercisePostId));
	}
	
	@Test
	@Transactional
	@DisplayName("User ID, ExercisePost ID로 찾고 존재하면 삭제 -> 정상 삭제")
	public void checkIsMineAndDeleteTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		ExercisePost ep = exercisePostRepository.findAll().get(0);
		ep.setUser(user);
		
		exercisePostRepository.save(ep);
		
		Long userId = user.getId();
		Long exercisePostId = ep.getId();
		
		//When
		exercisePostService.checkIsMineAndDelete(userId, exercisePostId);
		
		//Then
		assertEquals(false, exercisePostRepository.existsById(exercisePostId));
	}
	
	
	/*
	 *  public long countWithUserId(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID 만족하는 ExercisePost 개수 반환 -> 정상")
	public void countWithUserIdTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		
		for(ExercisePost ep: exercisePostList) {
			ep.setUser(user);
		}
		
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		
		long result = exercisePostService.countWithUserId(user.getId());
		
		//Then
		assertEquals(result, INIT_EXERCISE_POST_NUM);
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
		Exercise exercise = getExerciseInstance();
		
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
		
		Exercise exercise = getExerciseInstance();
		
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
	
	/*
	 *  public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostId(Long id)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 해당 id의 ExercisePost 없음")
	public void getExercisePostViewDTOWithExercisePostIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		for(ExercisePost ep: exercisePostRepository.findAll()) {
			nonExistId = Math.max(nonExistId, ep.getId());
		}
		nonExistId++;
		
		//When
		
		ExercisePostViewDTO result = exercisePostService.getExercisePostViewDTOWithExercisePostId(nonExistId);
		
		//Then
		assertEquals(result,null);
		
	}
	
	@Test
	@Transactional
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 ->  정상")
	public void getExercisePostViewDTOWithExercisePostIdTest() {
		//Given
		ExercisePost exercisePost = exercisePostRepository.findAll().get(0);
		Long existId = exercisePost.getId();
		
		User user = getUserInstance();
	
		userRepository.save(user);
		
		Exercise exercise = getExerciseInstance();
		
		exerciseRepository.save(exercise);
		
		exercisePost.setUser(user);
		exercisePost.setExercise(exercise);
		
		exercisePostRepository.save(exercisePost);
		//When
		ExercisePostViewDTO result = exercisePostService.getExercisePostViewDTOWithExercisePostId(existId);
		
		//Then
		assertEquals(result != null, true);
		
	}
	
	private User getUserInstance() {
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("test")
				.lastName("test")
				.nickName("test")
				.role(RoleType.USER)
				.build();
		return user;
	}
	
	private Exercise getExerciseInstance() {
		Exercise exercise = Exercise.builder()
			    .name("Exercies")
			    .description("Description")
			    .caution("Caution")
			    .movement("Movement")
			    .target(TargetType.CHEST)
			    .build();
		return exercise;
	}
}
