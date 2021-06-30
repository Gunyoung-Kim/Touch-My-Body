package com.gunyoung.tmb.services.domain.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

	/**
	 * @param id 찾으려는 Muscle의 id
	 * @return Muscle, Null (해당 id의 Muscle이 없을때)
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
	 * @param name 찾으려는 Muscle 이름
	 * @return Muscle, Null (해당 이름의 Muscle이 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Muscle findByName(String name) {
		Optional<Muscle> result = muscleRepository.findByName(name);
		if(result.isEmpty())
			return null;
		return result.get();
			
	}
	
	/**
	 * Muscle을 category로 분류해서 반환할때 사용
	 * @return key: TargetType.koreanName
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly =true)
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
	 * @param muscle 저장하려는 Muscle
	 * @return 저장된 Muscle
	 * @author kimgun-yeong
	 */
	@Override
	public Muscle save(Muscle muscle) {
		return muscleRepository.save(muscle);
	}

	/**
	 * @param muscle 삭제하려는 muscle
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(Muscle muscle) {
		muscleRepository.delete(muscle);
	}

	
}
