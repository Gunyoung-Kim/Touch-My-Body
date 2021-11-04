package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Muscle;

public interface MuscleService {
	
	public static final String MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY = "bycategory";
	
	/**
	 * ID로 Muscle 찾기
	 * @param id 찾으려는 Muscle의 id
	 * @return Muscle
	 * @throws MuscleNotFoundedException 해당 id의 Muscle이 없을때
	 * @author kimgun-yeong
	 */
	public Muscle findById(Long id);
	
	/**
	 * name으로 Muscle 찾기
	 * @param name 찾으려는 Muscle 이름
	 * @return Muscle
	 * @throws MuscleNotFoundedException 해당 name의 Muscle이 없을때
	 * @author kimgun-yeong
	 */
	public Muscle findByName(String name);
	
	/**
	 * 모든 Muscle들 페이지 반환
	 * @throws PreconditionViolationException pageNumber 이 1보다 작거나 pageSize가 1보다 작을 경우
	 * @author kimgun-yeong
	 */
	public Page<Muscle> findAllInPage(Integer pageNumber, int pageSize);
	
	/**
	 * 키워드 name에 포함하는 Muscle 페이지 반환
	 * @param keyword Muscle name 검색 키워드 
	 * @throws PreconditionViolationException pageNumber 이 1보다 작거나 pageSize가 1보다 작을 경우
	 * @author kimgun-yeong
	 */
	public Page<Muscle> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize);
	
	/**
	 * 모든 Muscle들 category로 분류해서 반환 <br>
	 * Cache 사용
	 * @return key: TargetType.koreanName
	 * @author kimgun-yeong
	 */
	public Map<String, List<String>> getAllMusclesWithSortingByCategory();
	
	/**
	 * Muscle name들 이용하여 Muscle List 반환 <br>
	 * name에 대한 결과 값이 없는 경우 해당 Muscle은 제외한 결과를 반환한다.
	 * @author kimgun-yeong
	 */
	public List<Muscle> findAllByNames(List<String> muscleNames);
	
	/**
	 * Muscle 생성 및 수정 <br>
	 * {@code CacheUtil.MUSCLE_SORT_NAME} 관련 Cache 삭제
	 * @param muscle 저장하려는 Muscle
	 * @return 저장된 Muscle
	 * @author kimgun-yeong
	 */
	public Muscle save(Muscle muscle);
	
	/**
	 * Muscle 삭제 <br>
	 * {@code CacheUtil.MUSCLE_SORT_NAME} 관련 Cache 삭제
	 * @param muscle 삭제하려는 muscle
	 * @throws PreconditionViolationException muscle == null
	 * @author kimgun-yeong
	 */
	public void delete(Muscle muscle);
	
	/**
	 * ID를 만족하는 Muscle 삭제 <br>
	 * {@code CacheUtil.MUSCLE_SORT_NAME} 관련 Cache 삭제
	 * @param id 삭제하려는 Muscle의 ID
	 * @author kimgun-yeong
	 */
	public void deleteById(Long id);
	
	/**
	 * 모든 Muscle의 개수 반환
	 * @author kimgun-yeong
	 */
	public long countAll();
	
	/**
	 * name 키워드 만족하는 모든 Muscle 개수 반환
	 * @param keyword Muscle name 검색 키워드  
	 * @author kimgun-yeong
	 */
	public long countAllWithNameKeyword(String keyword);

	/**
	 * name을 만족하는 Muscle 존재 여부 반환
	 * @param name 찾으려는 Muscle의 이
	 * @author kimgun-yeong
	 */
	public boolean existsByName(String name);
}
