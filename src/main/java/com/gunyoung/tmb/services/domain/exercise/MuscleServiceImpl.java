package com.gunyoung.tmb.services.domain.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.jpa.MuscleNameAndCategoryDTO;
import com.gunyoung.tmb.error.codes.MuscleErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.utils.CacheUtil;

import lombok.RequiredArgsConstructor;

@Service("muscleService")
@Transactional
@RequiredArgsConstructor
public class MuscleServiceImpl implements MuscleService {
	
	private final MuscleRepository muscleRepository;

	/**
	 * ID로 Muscle 찾기
	 * @param id 찾으려는 Muscle의 id
	 * @return Muscle, Null (해당 id의 Muscle이 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly= true)
	public Muscle findById(Long id) {
		Optional<Muscle> result = muscleRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * name으로 Muscle 찾기
	 * @param name 찾으려는 Muscle 이름
	 * @return Muscle, Null (해당 이름의 Muscle이 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=CacheUtil.MUSCLE_NAME,key="#name")
	public Muscle findByName(String name) {
		Optional<Muscle> result = muscleRepository.findByName(name);
		if(result.isEmpty())
			return null;
		return result.get();
			
	}
	
	/**
	 * 모든 Muscle들 페이지 반환
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<Muscle> findAllInPage(Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return muscleRepository.findAll(pageRequest);
	}
	
	/**
	 * 키워드 name에 포함하는 Muscle 페이지 반환
	 * @param keyword Muscle name 검색 키워드 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<Muscle> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return muscleRepository.findAllWithNameKeyword(keyword, pageRequest);
	}	
	
	/**
	 * 모든 Muscle들 category로 분류해서 반환 <br>
	 * Cache 사용
	 * @return key: TargetType.koreanName
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly =true)
	@Cacheable(cacheNames=CacheUtil.MUSCLE_SORT_NAME, key="#root.methodName")
	public Map<String, List<String>> getAllMusclesWithSortingByCategory() {
		Map<String,List<String>> result = new HashMap<>();
		List<MuscleNameAndCategoryDTO> muscles = muscleRepository.findAllWithNamaAndCategory();
		
		for(MuscleNameAndCategoryDTO muscle: muscles) {
			if(result.containsKey(muscle.getCategory().getKoreanName())) {
				result.get(muscle.getCategory().getKoreanName()).add(muscle.getName());
			} else {
				List<String> newList = new ArrayList<>();
				newList.add(muscle.getName());
				result.put(muscle.getCategory().getKoreanName(), newList);
			}
		}
		
		return result;
	}
	
	/**
	 * Muscle name List 이용하여 Muscle List 반환
	 * @param muscleNames Muscle name list
	 * @return 
	 * @throws MuscleNotFoundedException Muscle name에 해당하는 Muscle 없을 때
	 * @author kimgun-yeong
	 */
	public List<Muscle> getMuscleListFromMuscleNameList(Iterable<String> muscleNames) throws MuscleNotFoundedException {
		List<Muscle> muscleList = new ArrayList<>();
		
		for(String muscleName: muscleNames) {
			Muscle muscle = findByName(muscleName);
			
			if(muscle == null)
				throw new MuscleNotFoundedException(MuscleErrorCode.MUSCLE_NOT_FOUNDED_ERROR.getDescription());
			
			muscleList.add(muscle);
		}
		
		return muscleList;
	}

	/**
	 * Muscle 생성 및 수정 <br>
	 * {@code CacheUtil.MUSCLE_NAME}, {@code CacheUtil.MUSCLE_SORT_NAME} 관련 Cache 삭제 
	 * @param muscle 저장하려는 Muscle
	 * @return 저장된 Muscle
	 * @author kimgun-yeong
	 */
	@Override
	@CacheEvict(cacheNames= {CacheUtil.MUSCLE_NAME,CacheUtil.MUSCLE_SORT_NAME}, allEntries=true)
	public Muscle save(Muscle muscle) {
		return muscleRepository.save(muscle);
	}

	/**
	 * Muscle 삭제 <br>
	 * {@code CacheUtil.MUSCLE_NAME}, {@code CacheUtil.MUSCLE_SORT_NAME} 관련 Cache 삭제
	 * @param muscle 삭제하려는 muscle
	 * @author kimgun-yeong
	 */
	@Override
	@CacheEvict(cacheNames= {CacheUtil.MUSCLE_NAME,CacheUtil.MUSCLE_SORT_NAME}, allEntries=true)
	public void delete(Muscle muscle) {
		muscleRepository.delete(muscle);
	}
	
	/**
	 * ID를 만족하는 Muscle 삭제
	 * @param id 삭제하려는 Muscle의 ID
	 * @author kimgun-yeong
	 */
	@Override
	public void deleteById(Long id) {
		Muscle muscle = findById(id);
		if(muscle!= null)
			delete(muscle);
	}

	/**
	 * 모든 Muscle의 개수 반환
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countAll() {
		return muscleRepository.count();
	}

	/**
	 * name 키워드 만족하는 모든 Muscle 개수 반환
	 * @param keyword Muscle name 검색 키워드  
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countAllWithNameKeyword(String keyword) {
		return muscleRepository.countAllWithNamekeyword(keyword);
	}

	/**
	 * name을 만족하느 Muscle 존재 여부 반환
	 * @param name 찾으려는 Muscle의 이
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean existsByName(String name) {
		return muscleRepository.existsByName(name);
	}

}
