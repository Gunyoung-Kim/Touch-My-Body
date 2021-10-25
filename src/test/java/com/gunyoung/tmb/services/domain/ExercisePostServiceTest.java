package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.PostLikeRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.testutil.CommentTest;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.PostLikeTest;
import com.gunyoung.tmb.testutil.TargetTypeTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * ExercisePostService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class ExercisePostServiceTest {
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	PostLikeRepository postLikeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Autowired
	ExercisePostService exercisePostService;
	
	private ExercisePost exercisePost;
	
	@BeforeEach
	void setup() {
		exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
	}
	
	@AfterEach
	void tearDown() {
		exercisePostRepository.deleteAll();
	}
	
	/*
	 *  ExercisePost findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 ExercisePost 찾기 -> 해당 id의 exercisePost 없음")
	void findByIdNonExist() {
		//Given
		long nonExistExercisePostId = ExercisePostTest.getNonExistExercisePostId(exercisePostRepository);
		
		//When
		ExercisePost result = exercisePostService.findById(nonExistExercisePostId+ 1000);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 ExercisePost 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long exercisePostId = exercisePost.getId();
		
		//When
		ExercisePost result = exercisePostService.findById(exercisePostId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  List<ExercisePost> findAllByUserIdOrderByCreatedAtASCCustom(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 ExercisePost들 오래된 순으로 찾기 ->정상, 개수 확인")
	void findAllByUserIdOrderByCreatedAtASCCustomTestCheckCount() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		int givenExercisePostNum = exercisePosts.size();
		for(ExercisePost ep: exercisePosts) {
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePosts);
		
		//When
		List<ExercisePost> result = exercisePostService.findAllByUserIdOrderByCreatedAtAsc(user.getId(),1,PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize()).getContent();
		
		//Then
		assertEquals(Math.min(givenExercisePostNum, PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize()),result.size());
	}
	
	@Test
	@Transactional
	@DisplayName("User ID로 ExercisePost들 오래된 순으로 찾기 ->정상, 정렬 순서 확인")
	void findAllByUserIdOrderByCreatedAtASCCustomTestCheckSorting() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		for(ExercisePost ep: exercisePosts) {
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePosts);
		
		//When
		List<ExercisePost> result = exercisePostService.findAllByUserIdOrderByCreatedAtAsc(user.getId(),1,PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize()).getContent();
		
		//Then
		assertTrue(result.get(0).getCreatedAt().isBefore(result.get(1).getCreatedAt()));
	}
	
	/*
	 * List<Comment> findAllByUserIdOrderByCreatedAtDesc(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 ExercisePost들 최신 순으로 찾기 ->정상, 개수 확인")
	void findAllByUserIdOrderByCreatedAtDescTestCheckCount() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		long givenExercisePostNum = exercisePosts.size();
		for(ExercisePost ep: exercisePosts) {
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePosts);
		
		//When
		List<ExercisePost> result = exercisePostService.findAllByUserIdOrderByCreatedAtDesc(user.getId(),1,PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize()).getContent();
		
		//Then
		assertEquals(result.size(),Math.min(givenExercisePostNum, PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize()));
	}
	
	@Test
	@Transactional
	@DisplayName("User ID로 ExercisePost들 최신 순으로 찾기 ->정상, 정렬 확인")
	void findAllByUserIdOrderByCreatedAtDescTestCheckSorting() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		for(ExercisePost ep: exercisePosts) {
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePosts);
		
		//When
		List<ExercisePost> result = exercisePostService.findAllByUserIdOrderByCreatedAtDesc(user.getId(),1,PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize()).getContent();
		
		//Then
		assertTrue(result.get(0).getCreatedAt().isAfter(result.get(1).getCreatedAt()));
	}
	
	/*
	 *  Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOOrderByCreatedAtDESCByPage(Integer pageNumber)
	 */
	
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 객체들 가져오기 -> 정상, 개수 확인")
	void findAllForPostForCommunityViewDTOByPageTestCheckSize() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		long givenExercisePostNum = exercisePostList.size();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		Page<PostForCommunityViewDTO> result = exercisePostService.findAllForPostForCommunityViewDTOOderByCreatedAtDESCByPage(1,PageSize.COMMUNITY_PAGE_SIZE.getSize());
		
		//Then
		
		//Math.Min은 인자에 있는 값들이 추후에 변경될수 있는점 고려
		assertEquals(Math.min(PageSize.COMMUNITY_PAGE_SIZE.getSize(),givenExercisePostNum), result.getContent().size());
	}
	
	/*
	 * Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(String keyword,
			Integer pageNumber)
	 */
	
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 객체들 키워드로 가져오기 -> 정상, 키워드가 모든 ExercisePost 만족")
	void findAllForPostForCommunityViewDTOWithKeywordByPageTestKeywordForAll() {
		//Given
		String contentsForAllExercisePost = ExercisePostTest.getExercisePostInstance().getContents();
		String keywordForAllExercisePostInDB = contentsForAllExercisePost.substring(0,contentsForAllExercisePost.length() -2);
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		long givenExercisePostNum = exercisePostList.size();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		Page<PostForCommunityViewDTO> result = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keywordForAllExercisePostInDB,1,PageSize.COMMUNITY_PAGE_SIZE.getSize());
		
		//Then
		
		//Math.Min은 인자에 있는 값들이 추후에 변경될수 있는점 고려
		assertEquals(Math.min(PageSize.COMMUNITY_PAGE_SIZE.getSize(),givenExercisePostNum), result.getContent().size());
	}
	
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 객체들 키워드로 가져오기 -> 정상, 키워드가 하나도 만족하지 않음")
	void findAllForPostForCommunityViewDTOWithKeywordByPageTest() {
		//Given
		String keywordForNothing = "nonExist!!!!";
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		Page<PostForCommunityViewDTO> result = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keywordForNothing, 1,PageSize.COMMUNITY_PAGE_SIZE.getSize());
		
		//Then
		
		//Math.Min은 인자에 있는 값들이 추후에 변경될수 있는점 고려
		assertEquals(0, result.getContent().size());
	}
	
	/*
	 *  Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(TargetType target,
			Pageable pageable)
	 */
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 객체들 특정 target만 가져오기 -> 정상")
	void  findAllForPostForCommunityViewDTOWithTargetByPage() {
		//Given
		TargetType targetTypeForAllExercisePost = ExerciseTest.getExerciseInstance().getTarget();
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		long givenExercisePostNum = exercisePostList.size();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		Page<PostForCommunityViewDTO> result = exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(targetTypeForAllExercisePost, 1,PageSize.COMMUNITY_PAGE_SIZE.getSize());
		
		//Then
		assertEquals(Math.min(PageSize.COMMUNITY_PAGE_SIZE.getSize(), givenExercisePostNum), result.getContent().size());
	}
	
	/*
	 *  Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType target,
			String keyword, Integer pageNumber)
	 */
	
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 키워드로 객체들 특정 target만 가져오기 -> 정상, 모두 target만 일치, 키워드는 모두 불일치")
	void findAllForPostForCommunityViewDTOWithTargetAndKeywordByPageTestAllTargetNoneKeyword() {
		//Given
		String keywordForNothing = "nonExist!!!!";
		TargetType targetTypeForAllExercisePost = ExerciseTest.getExerciseInstance().getTarget();
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		Page<PostForCommunityViewDTO> result = exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(targetTypeForAllExercisePost, keywordForNothing, 1,PageSize.COMMUNITY_PAGE_SIZE.getSize());
		
		//Then
		assertEquals(0, result.getContent().size());
	}
	
	@Test
	@Transactional
	@DisplayName("커뮤니티에 보여질 PostForCommunityViewDTO 키워드로 객체들 특정 target만 가져오기 -> 정상, 모두 target 일치, 키워드 모두 일치")
	void findAllForPostForCommunityViewDTOWithTargetAndKeywordByPageTestAllTargetAllKeyword() {
		//Given
		String contentsForAllExercisePost = ExercisePostTest.getExercisePostInstance().getContents();
		String keywordForAllExercisePostInDB = contentsForAllExercisePost.substring(0,contentsForAllExercisePost.length() -2);
		TargetType targetTypeForAllExercisePost = ExerciseTest.getExerciseInstance().getTarget();
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		long givenExercisePostNum = exercisePostList.size();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		Page<PostForCommunityViewDTO> result = exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(targetTypeForAllExercisePost, keywordForAllExercisePostInDB, 1,PageSize.COMMUNITY_PAGE_SIZE.getSize());
		
		//Then
		assertEquals(givenExercisePostNum, result.getContent().size());
	}
	
	/*
	 *  ExercisePost save(ExercisePost exercisePost)
	 */
	
	@Test
	@DisplayName("ExercisePost 수정하기 -> 정상, 변화 확인")
	void mergeTestCheckChanged() {
		//Given
		String changeTitle = "Changed Title";
		Long exercisePostId = exercisePost.getId();
		exercisePost.setTitle(changeTitle);
		
		//When
		exercisePostService.save(exercisePost);
		
		//Then
		ExercisePost result = exercisePostRepository.findById(exercisePostId).get();
		assertEquals(changeTitle, result.getTitle());
	}
	
	@Test
	@DisplayName("ExercisePost 수정하기 -> 정상, 개수 확인")
	void mergeTestCheckCount() {
		//Given
		String changeTitle = "Changed Title";
		exercisePost.setTitle(changeTitle);
		
		long givenExercisePostNum = exercisePostRepository.count();
		
		//When
		exercisePostService.save(exercisePost);
		
		//Then
		assertEquals(givenExercisePostNum, exercisePostRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 추가하기 -> 정상")
	void saveTest() {
		//Given
		ExercisePost newExercisePost = ExercisePostTest.getExercisePostInstance();
		Long givenExercisePostNum = exercisePostRepository.count();
		
		//When
		exercisePostService.save(newExercisePost);
		
		//Then
		assertEquals(givenExercisePostNum + 1, exercisePostRepository.count());
	}
	
	/*
	 * ExercisePost saveWithUserAndExercise(ExercisePost exercisePost, User user, Exercise exericse)
	 */
	
	@Test
	@Transactional
	@DisplayName("User, Exercise와 연결 짓고 저장하기 ->정상, 개수 확인")
	void saveWithUserAndExerciseTestCheckCount() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		
		long givenExercisePostNum = exercisePostRepository.count();
		
		//When
		
		exercisePostService.saveWithUserAndExercise(exercisePost, user, exercise);
		
		//Then
		assertEquals(givenExercisePostNum +1, exercisePostRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("User, Exercise와 연결 짓고 저장하기 ->정상, User와의 연관관계 확인")
	void saveWithUserAndExerciseTestCheckWithUser() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		
		//When
		ExercisePost result = exercisePostService.saveWithUserAndExercise(exercisePost, user, exercise);
	
		//Then
		assertEquals(user, result.getUser());
	}
	
	@Test
	@Transactional
	@DisplayName("User, Exercise와 연결 짓고 저장하기 ->정상, Exercise와의 연관관계 확인")
	void saveWithUserAndExerciseTestCheckWithExercise() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		
		//When
		ExercisePost result = exercisePostService.saveWithUserAndExercise(exercisePost, user, exercise);
	
		//Then
		assertEquals(exercise, result.getExercise());
	}
	
	/*
	 *  void delete(ExercisePost exercisePost)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 삭제하기 -> 정상, Check ExercisePost Delete")
	void deleteTestCheckExercisePostDelete() {
		//Given
		Long givenExercisePostNum = exercisePostRepository.count();
		
		//When
		exercisePostService.delete(exercisePost);
		
		//Then
		assertEquals(givenExercisePostNum - 1, exercisePostRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 삭제하기 -> 정상, Check PostLike Delete")
	void deleteTestCheckPostLikeDelete() {
		//Given
		PostLike postLike = PostLikeTest.getPostLikeInstance();
		postLike.setExercisePost(exercisePost);
		postLikeRepository.save(postLike);
		
		Long postLikeId = postLike.getId();
		
		//When
		exercisePostService.delete(exercisePost);
		
		//Then
		assertFalse(postLikeRepository.existsById(postLikeId));
	}
	
	@Test
	@Transactional
	@DisplayName("ExercisePost 삭제하기 -> 정상, Check Comment Delete")
	void deleteTestCheckCommentDelete() {
		//Given
		Comment comment = CommentTest.getCommentInstance();
		comment.setExercisePost(exercisePost);
		commentRepository.save(comment);
		
		Long commentId = comment.getId();
		
		//When
		exercisePostService.delete(exercisePost);
		
		//Then
		assertFalse(commentRepository.existsById(commentId));
	}
	
	/*
	 * void checkIsMineAndDelete(Long userId, Long exercisePostId) {
	 */
	
	@Test
	@Transactional
	@DisplayName("User ID, ExercisePost ID로 찾고 존재하면 삭제 -> 존재하지 않음")
	void checkIsMineAndDeleteNotMine() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		exercisePost.setUser(user);
		exercisePostRepository.save(exercisePost);
		
		Long otherUserId = user.getId() + 1;
		Long exercisePostId = exercisePost.getId();
		
		//When
		exercisePostService.checkIsMineAndDelete(otherUserId, exercisePostId);
		
		//Then
		assertTrue(exercisePostRepository.existsById(exercisePostId));
	}
	
	@Test
	@Transactional
	@DisplayName("User ID, ExercisePost ID로 찾고 존재하면 삭제 -> 정상 삭제")
	void checkIsMineAndDeleteTest() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		exercisePost.setUser(user);
		exercisePostRepository.save(exercisePost);
		
		Long userId = user.getId();
		Long exercisePostId = exercisePost.getId();
		
		//When
		exercisePostService.checkIsMineAndDelete(userId, exercisePostId);
		
		//Then
		assertFalse(exercisePostRepository.existsById(exercisePostId));
	}
	
	/*
	 * void deleteAllByUserId(Long userId)
	 */
	
	@Test
	@Transactional
	@DisplayName("User ID로 ExercisePost 일괄 삭제 -> 정상 ,Check ExercisePosts Delete")
	void deleteAllByUserIdTestCheckExercisePostsDelete() {
		//Given
		int addExercisePostNum = 5;
		addExercisePostNum(addExercisePostNum);
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		saveAllExercisePostWithUser(user);
		
		Long userId = user.getId();
		
		//When
		exercisePostService.deleteAllByUserId(userId);
		
		//Then
		assertEquals(0, exercisePostRepository.count());
	}
	
	private void saveAllExercisePostWithUser(User user) {
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		for(ExercisePost exercisePost: exercisePosts) {
			exercisePost.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePosts);
	}
	
	@Test
	@Transactional
	@DisplayName("User ID로 ExercisePost 일괄 삭제 -> 정상 ,Check PostLikes Delete")
	void deleteAllByUserIdTestCheckPostLikesDelete() {
		//Given
		int addExercisePostNum = 10;
		addExercisePostNum(addExercisePostNum);
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		saveAllExerciseWithUserAndAddPostLikeForEach(user);
		
		Long userId = user.getId();
		
		//When
		exercisePostService.deleteAllByUserId(userId);
		
		//Then
		assertEquals(0, postLikeRepository.count());
	}
	
	private void saveAllExerciseWithUserAndAddPostLikeForEach(User user) {
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		List<PostLike> newPostLikes = new ArrayList<>();
		for(ExercisePost exercisePost: exercisePosts) {
			PostLike postLike = PostLikeTest.getPostLikeInstance();
			postLike.setExercisePost(exercisePost);
			newPostLikes.add(postLike);
			
			exercisePost.setUser(user);
		}
		
		exercisePostRepository.saveAll(exercisePosts);
		postLikeRepository.saveAll(newPostLikes);
	}
	
	@Test
	@Transactional
	@DisplayName("User ID로 ExercisePost 일괄 삭제 -> 정상 ,Check Comments Delete")
	void deleteAllByUserIdTestCheckCommentsDelete() {
		//Given
		int addExercisePostNum = 10;
		addExercisePostNum(addExercisePostNum);
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		saveAllExerciseWithUserAndAddCommentForEach(user);
		
		Long userId = user.getId();
		
		//When
		exercisePostService.deleteAllByUserId(userId);
		
		//Then
		assertEquals(0, commentRepository.count());
	}
	
	private void saveAllExerciseWithUserAndAddCommentForEach(User user) {
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		List<Comment> newComments = new ArrayList<>();
		for(ExercisePost exercisePost: exercisePosts) {
			Comment comment = CommentTest.getCommentInstance();
			comment.setExercisePost(exercisePost);
			newComments.add(comment);
			
			exercisePost.setUser(user);
		}
		
		exercisePostRepository.saveAll(exercisePosts);
		commentRepository.saveAll(newComments);
	}

	/*
	 * void deleteAllByExerciseId(Long exerciseId)
	 */
	
	@Test
	@Transactional
	@DisplayName("Exercise ID로 ExercisePost 일괄 삭제 -> 정상 ,Check ExercisePosts Delete")
	void deleteAllByExerciseIdTestCheckExercisePostsDelete() {
		//Given
		int addExercisePostNum = 5;
		addExercisePostNum(addExercisePostNum);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		saveAllExercisePostWithExercise(exercise);
		
		Long exerciseId = exercise.getId();
		
		//When
		exercisePostService.deleteAllByExerciseId(exerciseId);
		
		//Then
		assertEquals(0, exercisePostRepository.count());
	}
	
	private void saveAllExercisePostWithExercise(Exercise exercise) {
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		for(ExercisePost exercisePost: exercisePosts) {
			exercisePost.setExercise(exercise);
		}
		exercisePostRepository.saveAll(exercisePosts);
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise ID로 ExercisePost 일괄 삭제 -> 정상 ,Check PostLikes Delete")
	void deleteAllByExerciseIdTestCheckPostLikesDelete() {
		//Given
		int addExercisePostNum = 10;
		addExercisePostNum(addExercisePostNum);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		saveAllExerciseWithExerciseAndAddPostLikeForEach(exercise);
		
		Long exerciseId = exercise.getId();
		
		//When
		exercisePostService.deleteAllByExerciseId(exerciseId);
		
		//Then
		assertEquals(0, postLikeRepository.count());
	}
	
	private void saveAllExerciseWithExerciseAndAddPostLikeForEach(Exercise exercise) {
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		List<PostLike> newPostLikes = new ArrayList<>();
		for(ExercisePost exercisePost: exercisePosts) {
			PostLike postLike = PostLikeTest.getPostLikeInstance();
			postLike.setExercisePost(exercisePost);
			newPostLikes.add(postLike);
			
			exercisePost.setExercise(exercise);
		}
		
		exercisePostRepository.saveAll(exercisePosts);
		postLikeRepository.saveAll(newPostLikes);
	}
	
	@Test
	@Transactional
	@DisplayName("Exercise ID로 ExercisePost 일괄 삭제 -> 정상 ,Check Comments Delete")
	void deleteAllByExerciseIdTestCheckCommentsDelete() {
		//Given
		int addExercisePostNum = 10;
		addExercisePostNum(addExercisePostNum);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		saveAllExerciseWithExerciseAndAddCommentForEach(exercise);
		
		Long exerciseId = exercise.getId();
		
		//When
		exercisePostService.deleteAllByExerciseId(exerciseId);
		
		//Then
		assertEquals(0, commentRepository.count());
	}
	
	private void saveAllExerciseWithExerciseAndAddCommentForEach(Exercise exercise) {
		List<ExercisePost> exercisePosts = exercisePostRepository.findAll();
		List<Comment> newComments = new ArrayList<>();
		for(ExercisePost exercisePost: exercisePosts) {
			Comment comment = CommentTest.getCommentInstance();
			comment.setExercisePost(exercisePost);
			newComments.add(comment);
			
			exercisePost.setExercise(exercise);
		}
		
		exercisePostRepository.saveAll(exercisePosts);
		commentRepository.saveAll(newComments);
	}
	
	private void addExercisePostNum(int addExercisePostNum) {
		List<ExercisePost> newExercisePosts = new ArrayList<>();
		for(int i=0; i< addExercisePostNum;i++) {
			ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
			newExercisePosts.add(exercisePost);
		}
		exercisePostRepository.saveAll(newExercisePosts);
	}
	
	
	/*
	 *  long countWithUserId(Long userId)
	 */
	
	@Test
	@Transactional
	@DisplayName("User ID 만족하는 ExercisePost 개수 반환 -> 정상")
	void countWithUserIdTest() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		long givenExercisePostNum = exercisePostList.size();
		for(ExercisePost ep: exercisePostList) {
			ep.setUser(user);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		long result = exercisePostService.countWithUserId(user.getId());
		
		//Then
		assertEquals(givenExercisePostNum, result);
	}
	
	/*
	 *  long countWithTitleAndContentsKeyword(String keyword)
	 */
	
	@Test
	@Transactional
	@DisplayName("키워드(제목, 내용)를 만족하는 게시글 개수 반환 -> 정상, 모두 반환")
	void countWithTitleAndContentsKeywordTestEvery() {
		//Given
		String keywordEveryHas = ExercisePostTest.getExercisePostInstance().getTitle();
		
		ExercisePostTest.addNewExercisePostsInDBByNum(10, exercisePostRepository);
		
		long givenExercisePostNum = exercisePostRepository.count();
		
		//When
		long result = exercisePostService.countWithTitleAndContentsKeyword(keywordEveryHas);
		
		//Then 
		assertEquals(givenExercisePostNum, result);
	}
	
	@Test
	@Transactional
	@DisplayName("키워드(제목, 내용)를 만족하는 게시글 개수 반환 -> 정상, 0개 반환")
	void countWithTitleAndContentsKeywordTestNone() {
		//Given
		String keywordNoOneHas = "none!!!!";
		
		//When
		long result = exercisePostService.countWithTitleAndContentsKeyword(keywordNoOneHas);
		
		//Then 
		assertEquals(0, result);
	}
	
	/*
	 *  long countWithTarget(TargetType target)
	 */
	
	@Test
	@Transactional
	@DisplayName("해당 부위에 해당하는 Post만 개수 반환 -> 정상, 모든 ExercisePost 개수")
	void countWithTargetTestAll() {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		long givenExercisePostNum = exercisePostList.size();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		TargetType typeForAllExercisePostsExercise = exercise.getTarget();
		
		//When
		long result = exercisePostService.countWithTarget(typeForAllExercisePostsExercise);
		
		//Then
		assertEquals(givenExercisePostNum, result);
	}
	
	@Test
	@Transactional
	@DisplayName("해당 부위에 해당하는 Post만 개수 반환 -> 정상, 만족하는 ExercisePost 없음")
	void countWithTargetTestNone() {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		TargetType typeForNoneExercisePostsExercise = TargetTypeTest.getAnotherTargetType(exercise.getTarget());
		
		//When
		long result = exercisePostService.countWithTarget(typeForNoneExercisePostsExercise);
		
		//Then
		assertEquals(0, result);
	}
	
	/*
	 *  long countWithTargetAndKeyword(TargetType target, String keyword)
	 */
	
	@Test
	@Transactional
	@DisplayName("해당 부위에 해당하고 이름과 제목에 키워드 포함하는 게시글 개수 반환 ->정상, 키워드 모두 불만족, target은 모두 만족")
	void countWithTargetAndKeywordTestAllTargetNonekeyword() {
		//Given
		String keywordForNothing = "nothing!!!";
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		TargetType typeForAllExercisePostsExercise = exercise.getTarget();
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		long resultNone = exercisePostService.countWithTargetAndKeyword(typeForAllExercisePostsExercise, keywordForNothing);
		
		//Then
		assertEquals(0, resultNone);
		
	}
	
	@Test
	@Transactional
	@DisplayName("해당 부위에 해당하고 이름과 제목에 키워드 포함하는 게시글 개수 반환 ->정상 , 키워드 target 모두 만족")
	void countWithTargetAndKeywordTestAll() {
		//Given
		String contentsForAllExercisePost = ExercisePostTest.getExercisePostInstance().getContents();
		String keywordForAllExercisePostInDB = contentsForAllExercisePost.substring(0,contentsForAllExercisePost.length() -2);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		TargetType typeForAllExercisePostsExercise = exercise.getTarget();
		
		ExercisePostTest.addNewExercisePostsInDBByNum(5, exercisePostRepository);
		
		List<ExercisePost> exercisePostList = exercisePostRepository.findAll();
		long givenExercisePostNum = exercisePostList.size();
		for(ExercisePost ep: exercisePostList) {
			ep.setExercise(exercise);
		}
		exercisePostRepository.saveAll(exercisePostList);
		
		//When
		long result = exercisePostService.countWithTargetAndKeyword(typeForAllExercisePostsExercise, keywordForAllExercisePostInDB);
		
		//Then
		assertEquals(givenExercisePostNum, result);
	}
	
	/*
	 *  ExercisePostViewDTO getExercisePostViewDTOWithExercisePostId(Long id)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 해당 id의 ExercisePost 없음")
	void getExercisePostViewDTOWithExercisePostIdNonExist() {
		//Given
		Long nonExistId = ExercisePostTest.getNonExistExercisePostId(exercisePostRepository);
		
		//When
		ExercisePostViewDTO result = exercisePostService.getExercisePostViewDTOWithExercisePostId(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@Transactional
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 ->  정상")
	void getExercisePostViewDTOWithExercisePostIdTest() {
		//Given
		Long exercisePostId = exercisePost.getId();
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		exercisePost.setUser(user);
		exercisePost.setExercise(exercise);
		exercisePostRepository.save(exercisePost);
		
		//When
		ExercisePostViewDTO result = exercisePostService.getExercisePostViewDTOWithExercisePostId(exercisePostId);
		
		//Then
		assertNotNull(result);
		
	}
}
