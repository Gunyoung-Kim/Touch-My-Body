package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;

public interface ExerciseService {
	
	/**
	 * ID로 Exercise 찾기
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public Exercise findById(Long id);
	
	/**
	 * name으로 Exercise 찾기
	 * @param name 찾으려는 Exercise의 이름
	 * @return Exercise, Null(해당 name의 Exercise가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public Exercise findByName(String name);
	
	/**
	 * ID 로 Feedbacks 페치 조인 후 반환 
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public Exercise findWithFeedbacksById(Long id);
	
	/**
	 * ID로 ExercisePosts 페치 조인 후 반환
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @since 11 
	 * @author kimgun-yeong
	 */
	public Exercise findWithExercisePostsByName(String name);
	
	/**
	 * ID로 ExerciseMuscles 페치 조인 후 반환
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @since 11 
	 * @author kimgun-yeong
	 */
	public Exercise findWithExerciseMusclesById(Long id);
	
	/**
	 * 모든 Exercise 페이지로 가져오기 
	 * @author kimgun-yeong
	 */
	public Page<Exercise> findAllInPage(Integer pageNumber,int page_size);
	
	/**
	 * name에 키워드를 포함하는 모든 Exercise 페이지로 가져오는 메소드
	 * @param keyword name 검색 키워드 
	 * @author kimgun-yeong
	 */
	public Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber,int page_size);
	
	/**
	 * 모든 Exercise들을 target들로 분류해 반환 <br>
	 * Cache 이용
	 * @author kimgun-yeong
	 */
	public Map<String, List<String>> getAllExercisesNamewithSorting();
	
	/** 
	 * Exercise 생성 및 수정 <br>
	 * {@code CacheUtil.EXERCISE_SORT_NAME} 관련 캐시 삭제
	 * @param exercise 저장하려는 Exercise
	 * @return 저장된 Exercise
	 * @author kimgun-yeong
	 */
	public Exercise save(Exercise exercise);
	
	/**
	 * {@link SaveExerciseDTO} 에 담긴 정보로 Exercise save <br>
	 * {@code CacheUtil.EXERCISE_SORT_NAME} 관련 캐시 삭제
	 * @param dto 클라이언트로부터 받은 Exercise save하기 위한 {@link SaveExerciseDTO} 객체
	 * @throws TargetTypeNotFoundedException dto 객체에 담긴 target이 아무런 TargetType의 이름이 아닐때 
	 * @author kimgun-yeong
	 */
	public Exercise saveWithSaveExerciseDTO(Exercise exercise, SaveExerciseDTO dto);
	
	/**
	 * Exercise 삭제 <br>
	 * {@code CacheUtil.EXERCISE_SORT_NAME} 관련 캐시 삭제
	 * @param exercise 삭제하려는 Exercise
	 * @author kimgun-yeong
	 */
	public void delete(Exercise exercise);
	
	/**
	 * ID를 만족하는 Exercise 삭제 <br>
	 * {@code CacheUtil.EXERCISE_SORT_NAME} 관련 캐시 삭제
	 * @author kimgun-yeong
	 */
	public void deleteById(Long id);
	
	/**
	 * 모든 Exercise들 개수 반환
	 * @author kimgun-yeong
	 */
	public long countAll();
	
	/**
	 * 이름 키워드를 만족하는 Exercise 개수 반환
	 * @param nameKeyword 이름 키워드
	 * @author kimgun-yeong
	 */
	public long countAllWithNameKeyword(String nameKeyword);
	
	/**
	 * Exercise Id로 찾은 Exercise로 {@link ExerciseForInfoViewDTO} 생성 및 반환 
	 * @param exerciseId 찾으려는 Exercise의 ID
	 * @return {@link ExerciseForInfoViewDTO}, null(해당 id의 Exercise 없을때)
	 * @author kimgun-yeong
	 */
	public ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId);
	
	/**
	 * 해당 name의 Exercise 존재하는지 여부 반환
	 * @param 찾으려는 Exercise의 name
	 * @author kimgun-yeong
	 */
	public boolean existsByName(String name);
}
