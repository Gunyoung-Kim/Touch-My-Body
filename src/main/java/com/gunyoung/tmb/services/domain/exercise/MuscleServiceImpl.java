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
	
	public static final String MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY = "bycategory";
	
	private final MuscleRepository muscleRepository;

	@Override
	@Transactional(readOnly= true)
	public Muscle findById(Long id) {
		Optional<Muscle> result = muscleRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Muscle findByName(String name) {
		Optional<Muscle> result = muscleRepository.findByName(name);
		if(result.isEmpty())
			return null;
		return result.get();
			
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Muscle> findAllInPage(Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return muscleRepository.findAll(pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Muscle> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return muscleRepository.findAllWithNameKeyword(keyword, pageRequest);
	}	
	
	@Override
	@Transactional(readOnly =true)
	@Cacheable(cacheNames=CacheUtil.MUSCLE_SORT_NAME, key= "#root.target.MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY")
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

	@Override
	@CacheEvict(cacheNames= {CacheUtil.MUSCLE_SORT_NAME}, key="#root.target.MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public Muscle save(Muscle muscle) {
		return muscleRepository.save(muscle);
	}

	@Override
	@CacheEvict(cacheNames= {CacheUtil.MUSCLE_SORT_NAME}, key="#root.target.MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public void delete(Muscle muscle) {
		muscleRepository.delete(muscle);
	}
	
	@Override
	@CacheEvict(cacheNames= {CacheUtil.MUSCLE_SORT_NAME}, key="#root.target.MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public void deleteById(Long id) {
		Muscle muscle = findById(id);
		if(muscle!= null)
			delete(muscle);
	}

	@Override
	@Transactional(readOnly=true)
	public long countAll() {
		return muscleRepository.count();
	}

	@Override
	@Transactional(readOnly=true)
	public long countAllWithNameKeyword(String keyword) {
		return muscleRepository.countAllWithNamekeyword(keyword);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean existsByName(String name) {
		return muscleRepository.existsByName(name);
	}

}
