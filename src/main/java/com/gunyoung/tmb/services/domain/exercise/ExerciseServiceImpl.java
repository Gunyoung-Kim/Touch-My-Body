package com.gunyoung.tmb.services.domain.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.jpa.ExerciseNameAndTargetDTO;
import com.gunyoung.tmb.dto.reqeust.AddExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.MuscleErrorCode;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.repos.ExerciseRepository;

import lombok.RequiredArgsConstructor;


/**
 * ExerciesService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("exerciseService")
@Transactional
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
	
	private final ExerciseRepository exerciseRepository;
	
	private final MuscleService muscleService;
	
	private final ExerciseMuscleService exerciseMuscleService;

	/**
	 * @param id 찾으려는 Exercise의 id
	 * @return Exercise, Null(해당 id의 Exercise가 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Exercise findById(Long id) {
		Optional<Exercise> result = exerciseRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * @param name 찾으려는 Exercise의 이름
	 * @return Exercise, Null(해당 name의 Exercise가 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Exercise findByName(String name) {
		Optional<Exercise> result = exerciseRepository.findByName(name);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * 모든 운동 페이지로 가져오기 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<Exercise> findAllInPage(Integer pageNumber,int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, page_size);
		return exerciseRepository.findAll(pageRequest);
	}
	
	/**
	 * 이름에 키워드를 포함하는 모든 운동 페이지로 가져오는 메소드
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<Exercise> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, page_size);
		return exerciseRepository.findAllWithNameKeyword(keyword, pageRequest);
	}
	
	
	/**
	 * 모든 운동들을 주 운동 부위들로 분류해 반환하는 메소드
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String, List<String>> getAllExercisesNamewithSorting() {
		Map<String, List<String>> result = new HashMap<>();
		List<ExerciseNameAndTargetDTO> list = exerciseRepository.findAllWithNameAndTarget();
		
		for(ExerciseNameAndTargetDTO dto: list) {
			String target = dto.getTarget().getKoreanName();
			String name = dto.getName();
			
			if(result.containsKey(target)) {
				result.get(target).add(name);
			} else {
				List<String> newList = new ArrayList<>();
				newList.add(name);
				result.put(target, newList);
			}
		}
		
		return result;
	}

	/**
	 * @param exercise 저장하려는 Exercise
	 * @return 저장된 Exercise
	 * @author kimgun-yeong
	 */
	@Override
	public Exercise save(Exercise exercise) {
		return exerciseRepository.save(exercise);
	}
	
	/**
	 * @param dto 클라이언트로부터 받은 Exercise 추가하기 위한 DTO 객체
	 * @author kimgun-yeong
	 */
	@Override
	public Exercise saveWithAddExerciseDTO(AddExerciseDTO dto) {
		
		// Exercise 객체 생성
		Exercise exercise = Exercise.builder()
				.name(dto.getName())
				.description(dto.getDescriptoin())
				.caution(dto.getCaution())
				.movement(dto.getMovement())
				.build();
		
		// AddExerciseDTO에 입력된 target에 해당하는 TargetType 있는지 확인
		boolean isTargetTypeExist = false;
		for(TargetType type: TargetType.values()) {
			if(dto.getTarget().equals(type.getKoreanName())) {
				exercise.setTarget(type);
				isTargetTypeExist= true;
				break;
			}
		}
		
		if(!isTargetTypeExist) {
			throw new TargetTypeNotFoundedException(TargetTypeErrorCode.TargetTypeNotFoundedError.getDescription());
		}
		
		// Main Muscle 들 이름으로 Muscle 객체들 가져오기
		List<String> mainMusclesName = dto.getMainMuscles();
		List<Muscle> mainMuscles = new ArrayList<>();
		
		for(String muscleName: mainMusclesName) {
			Muscle muscle = muscleService.findByName(muscleName);
			
			if(muscle == null)
				throw new MuscleNotFoundedException(MuscleErrorCode.MuscleNotFoundedError.getDescription());
			
			mainMuscles.add(muscle);
		}
		
		// Sub Muscle 들 이름으로 Muscle 객체들 가져오기
		List<String> subMusclesName = dto.getSubMuscles();
		List<Muscle> subMuscles = new ArrayList<>();
		
		for(String muscleName: subMusclesName) {
			Muscle muscle = muscleService.findByName(muscleName);
			
			if(muscle == null)
				throw new MuscleNotFoundedException(MuscleErrorCode.MuscleNotFoundedError.getDescription());
			
			subMuscles.add(muscle);
		}
		
		// 생성한 Exercise 객체와 가져온 Muscle 객체로 ExerciseMuscle 객체 생성
		List<ExerciseMuscle> exerciseMuscles = new ArrayList<>();
		
		for(Muscle muscle: mainMuscles) {
			ExerciseMuscle em = ExerciseMuscle.builder()
					.isMain(true)
					.muscleName(muscle.getName())
					.muscle(muscle)
					.exercise(exercise)
					.build();
			
			exerciseMuscles.add(em);
			exercise.getExerciseMuscles().add(em);
		}
		
		for(Muscle muscle: subMuscles) {
			ExerciseMuscle em = ExerciseMuscle.builder()
					.isMain(false)
					.muscleName(muscle.getName())
					.muscle(muscle)
					.exercise(exercise)
					.build();
			exerciseMuscles.add(em);
			exercise.getExerciseMuscles().add(em);
		}
		
		//Exercise 객체 저장 
		exerciseRepository.save(exercise);
		
		//ExerciseMuscle 객체 저장
		exerciseMuscleService.saveAll(exerciseMuscles);
		
		return exercise;
	}

	/**
	 * @param exercise 삭제하려는 Exercise
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(Exercise exercise) {
		exerciseRepository.delete(exercise);
	}

	/**
	 * 모든 운동들 개수 반환
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countAll() {
		return exerciseRepository.count();
	}

	/**
	 * 이름 키워드를 만족하는 Exercise 개수 반환
	 * @param nameKeyword 이름 키워드
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countAllWithNameKeyword(String nameKeyword) {
		return exerciseRepository.countAllWithNameKeyword(nameKeyword);
	}

	/**
	 * Exercise Id로 찾은 Exercise로 ExerciseForInfoViewDTO 생성 및 반환 
	 * @return ExerciseForInfoViewDTO, null(해당 id의 Exercise 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public ExerciseForInfoViewDTO getExerciseForInfoViewDTOByExerciseId(Long exerciseId) {
		Exercise exercise = findById(exerciseId);
		
		if(exercise == null)
			return null;
		
		ExerciseForInfoViewDTO dto = ExerciseForInfoViewDTO.builder()
				.id(exercise.getId())
				.name(exercise.getName())
				.description(exercise.getDescription())
				.caution(exercise.getCaution())
				.movement(exercise.getMovement())
				.target(exercise.getTarget().getKoreanName())
				.build();
		
		List<ExerciseMuscle> muscles = exercise.getExerciseMuscles();
		StringBuilder mainMuscleBuilder = new StringBuilder();
		StringBuilder subMuscleBuilder = new StringBuilder();
		
		for(ExerciseMuscle muscle: muscles) {
			if(muscle.isMain()) {
				mainMuscleBuilder.append(muscle.getMuscleName()+".");
			} else {
				subMuscleBuilder.append(muscle.getMuscleName()+".");
			}
		}
		
		dto.setMainMuscle(mainMuscleBuilder.toString());
		dto.setSubMuscle(subMuscleBuilder.toString());
		
		return dto;
	}

}
