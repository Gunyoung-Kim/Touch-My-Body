package com.gunyoung.tmb.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;

/**
 * Test 클래스 전용 ExercisePost 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class ExercisePostTest {
	
	public static final String DEFAULT_TITLE = "title";
	
	/**
	 * 테스트용 ExercisePost 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static ExercisePost getExercisePostInstance() {
		ExercisePost ep = ExercisePost.builder()
				.title(DEFAULT_TITLE)
				.contents("contents")
				.build();
		return ep;
	}
	
	/**
	 * Repository를 통해 존재하지 않는 ExercisePost ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistExercisePostId(JpaRepository<ExercisePost, Long> exercisePostRepository) {
		Long nonExistExercisePostId = Long.valueOf(1);
		
		for(ExercisePost u : exercisePostRepository.findAll()) {
			nonExistExercisePostId = Math.max(nonExistExercisePostId, u.getId());
		}
		nonExistExercisePostId++;
		
		return nonExistExercisePostId;
	}
	
	/**
	 * Repository를 사용해 DB에 인자로 전해진 num 만큼 ExercisePost 생성 후 저장
	 * @author kimgun-yeong
	 */
	public static List<ExercisePost> addNewExercisePostsInDBByNum(int num, JpaRepository<ExercisePost, Long> exercisePostRepository) {
		List<ExercisePost> newExercisePosts = new ArrayList<>();
		for(int i=0;i < num;i++) {
			ExercisePost newExercisePost = getExercisePostInstance();
			newExercisePosts.add(newExercisePost);
		}
		return exercisePostRepository.saveAll(newExercisePosts);
	}
	
	/**
	 * Repository를 사용해 DB에 인자로 전해진 num 만큼 ExercisePost 생성 후 User와 연관관계 설정 후 저장
	 * @author kimgun-yeong
	 */
	public static List<ExercisePost> addNewExercisePostsInDBWithSettingUser(int num, User user,JpaRepository<ExercisePost, Long> exercisePostRepository) {
		List<ExercisePost> exercisePostList = new LinkedList<>();
		
		for(int i= 0 ; i<num ; i++) {
			ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
			exercisePost.setUser(user);
			exercisePostList.add(exercisePost);
		}
		
		return exercisePostRepository.saveAll(exercisePostList);
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.SaveExercisePostDTO} 에 바인딩 될 수 있는 MultiValueMap 반환 <br>
	 * exerciseName value 커스터마이징 가능 
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String, String> getSaveExercisePostDTOMap(String exerciseName) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", DEFAULT_TITLE);
		map.add("contents", "contents");
		map.add("exerciseName", exerciseName);
		
		return map;
	}
}
