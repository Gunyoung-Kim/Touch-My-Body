package com.gunyoung.tmb.services.domain.exercise;

import static com.gunyoung.tmb.utils.CacheConstants.MUSCLE_SORT_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.jpa.MuscleNameAndCategoryDTO;
import com.gunyoung.tmb.repos.MuscleRepository;

import lombok.RequiredArgsConstructor;

@Service("muscleService")
@Transactional
@RequiredArgsConstructor
public class MuscleServiceImpl implements MuscleService {
	
	private final MuscleRepository muscleRepository;
	
	private final ExerciseMuscleService exerciseMuscleService;

	@Override
	@Transactional(readOnly = true)
	public Muscle findById(Long id) {
		Optional<Muscle> result = muscleRepository.findById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Muscle findByName(String name) {
		Optional<Muscle> result = muscleRepository.findByName(name);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Muscle> findAllInPage(Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return muscleRepository.findAll(pageRequest);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Muscle> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return muscleRepository.findAllWithNameKeyword(keyword, pageRequest);
	}	
	
	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = MUSCLE_SORT_NAME, key = "#root.target.MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public Map<String, List<String>> getAllMusclesWithSortingByCategory() {
		List<MuscleNameAndCategoryDTO> listOfDTOFromRepo = muscleRepository.findAllWithNamaAndCategory();
		Map<String,List<String>> sortingResult = new HashMap<>();
		listOfDTOFromRepo.stream().forEach( dto -> {
			String koreanNameOfCategory = dto.getCategory().getKoreanName();
			String nameOfMuscle = dto.getName();
			sortingResult.putIfAbsent(koreanNameOfCategory, new ArrayList<>());
			sortingResult.get(koreanNameOfCategory).add(nameOfMuscle);
		});
		
		return sortingResult;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Muscle> findAllByNames(List<String> muscleNames){
		return muscleRepository.findAllByNamesInQuery(muscleNames);
	}
	
	@Override
	@CacheEvict(cacheNames = {MUSCLE_SORT_NAME}, key = "#root.target.MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public Muscle save(Muscle muscle) {
		return muscleRepository.save(muscle);
	}

	@Override
	@CacheEvict(cacheNames = {MUSCLE_SORT_NAME}, key = "#root.target.MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public void delete(Muscle muscle) {
		Objects.requireNonNull(muscle);
		exerciseMuscleService.deleteAllByMuscleId(muscle.getId());
		muscleRepository.delete(muscle);
	}
	
	@Override
	@CacheEvict(cacheNames = {MUSCLE_SORT_NAME}, key = "#root.target.MUSCLE_SORT_BY_CATEGORY_DEFAULY_KEY")
	public void deleteById(Long id) {
		Muscle muscle = findById(id);
		if(muscle != null)
			delete(muscle);
	}

	@Override
	@Transactional(readOnly = true)
	public long countAll() {
		return muscleRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public long countAllWithNameKeyword(String keyword) {
		return muscleRepository.countAllWithNamekeyword(keyword);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByName(String name) {
		return muscleRepository.existsByName(name);
	}
}
