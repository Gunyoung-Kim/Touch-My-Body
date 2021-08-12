package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;

public interface MuscleService {
	
	/**
	 * ID로 Muscle 찾기
	 * @param id 찾으려는 Muscle의 id
	 * @return Muscle, Null (해당 id의 Muscle이 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public Muscle findById(Long id);
	
	/**
	 * name으로 Muscle 찾기
	 * @param name 찾으려는 Muscle 이름
	 * @return Muscle, Null (해당 이름의 Muscle이 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public Muscle findByName(String name);
	
	/**
	 * 모든 Muscle들 페이지 반환
	 * @author kimgun-yeong
	 */
	public Page<Muscle> findAllInPage(Integer pageNumber, int pageSize);
	
	/**
	 * 키워드 name에 포함하는 Muscle 페이지 반환
	 * @param keyword Muscle name 검색 키워드 
	 * @author kimgun-yeong
	 */
	public Page<Muscle> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize);
	
	/**
	 * 모든 Muscle들 category로 분류해서 반환 <br>
	 * Cache 사용
	 * @return key: TargetType.koreanName
	 * @author kimgun-yeong
	 */
	public Map<String,List<String>> getAllMusclesWithSortingByCategory();
	
	/**
	 * Muscle name들 이용하여 Muscle List 반환 
	 * @throws MuscleNotFoundedException Muscle name에 해당하는 Muscle 없을 때
	 * @author kimgun-yeong
	 */
	public List<Muscle> getMuscleListFromMuscleNameList(Iterable<String> muscleNames) throws MuscleNotFoundedException;
	
	/**
	 * Muscle 생성 및 수정 <br>
	 * {@code CacheUtil.MUSCLE_NAME}, {@code CacheUtil.MUSCLE_SORT_NAME} 관련 Cache 삭제 
	 * @param muscle 저장하려는 Muscle
	 * @return 저장된 Muscle
	 * @author kimgun-yeong
	 */
	public Muscle save(Muscle muscle);
	
	/**
	 * Muscle 삭제 <br>
	 * {@code CacheUtil.MUSCLE_NAME}, {@code CacheUtil.MUSCLE_SORT_NAME} 관련 Cache 삭제
	 * @param muscle 삭제하려는 muscle
	 * @author kimgun-yeong
	 */
	public void delete(Muscle muscle);
	
	/**
	 * ID를 만족하는 Muscle 삭제
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
