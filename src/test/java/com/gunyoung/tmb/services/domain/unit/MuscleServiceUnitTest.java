package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.jpa.MuscleNameAndCategoryDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.services.domain.exercise.MuscleServiceImpl;

/**
 * {@link MuscleServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) MuscleServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class MuscleServiceUnitTest {
	
	@Mock
	MuscleRepository muscleRepository;
	
	@InjectMocks
	MuscleServiceImpl muscleService;
	
	private Muscle muscle;
	
	@BeforeEach
	void setup() {
		muscle = Muscle.builder()
				.build();
	}
	
	/*
	 * public Muscle findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Muscle 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(muscleRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Muscle result = muscleService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Muscle 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long muscleId = Long.valueOf(1);
		given(muscleRepository.findById(muscleId)).willReturn(Optional.of(muscle));
		
		//When
		Muscle result = muscleService.findById(muscleId);
		
		//Them
		assertEquals(muscle, result);
	}
	
	/*
	 * public Muscle findByName(String name)
	 */
	
	@Test
	@DisplayName("Name으로 Muscle 찾기 -> 존재하지 않음")
	public void findByNameNonExist() {
		//Given
		String nonExistName = "nonExist";
		given(muscleRepository.findByName(nonExistName)).willReturn(Optional.empty());
		
		//When
		Muscle result = muscleService.findByName(nonExistName);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("Name으로 Muscle 찾기 -> 정상")
	public void findByNameTest() {
		//Given
		String muscleName = "muscle";
		given(muscleRepository.findByName(muscleName)).willReturn(Optional.of(muscle));
		
		//When
		Muscle result = muscleService.findByName(muscleName);
		
		//Them
		assertEquals(muscle, result);
	}
	
	/*
	 * public Page<Muscle> findAllInPage(Integer pageNumber, int pageSize)
	 */
	
	@Test
	@DisplayName("모든 Muscle들 페이지 반환 -> 정상")
	public void findAllInPageTest() {
		//Given
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		muscleService.findAllInPage(pageNum, pageSize);
		
		//Then
		then(muscleRepository).should(times(1)).findAll(any(PageRequest.class));
	}
	
	/*
	 * public Page<Muscle> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize) 
	 */
	
	@Test
	@DisplayName("키워드 name에 포함하는 Muscle 페이지 반환 -> 정상")
	public void findAllWithNameKeywordInPageTest() {
		//Given
		String keyword = "keyword";
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		muscleService.findAllWithNameKeywordInPage(keyword, pageNum, pageSize);
		
		//Then
		then(muscleRepository).should(times(1)).findAllWithNameKeyword(anyString(), any(PageRequest.class));
	}
	
	/*
	 * public Map<String, List<String>> getAllMusclesWithSortingByCategory()
	 */
	
	@Test
	@DisplayName("모든 Muscle들 category로 분류해서 반환 -> 정상")
	public void getAllMusclesWithSortingByCategoryTest() {
		//Given
		TargetType[] targetTypes = TargetType.values();
		List<MuscleNameAndCategoryDTO> muscleNameAndCategoryDTOList = new ArrayList<>();
		
		int numForEachTargetType = 2;
		
		for(int i=0;i < numForEachTargetType;i++) {
			for(TargetType target: targetTypes) {
				MuscleNameAndCategoryDTO dto = MuscleNameAndCategoryDTO.builder()
						.name("muscle" + i)
						.category(target)
						.build();
				
				muscleNameAndCategoryDTOList.add(dto);
			}
		}
		
		given(muscleRepository.findAllWithNamaAndCategory()).willReturn(muscleNameAndCategoryDTOList);
		
		//When
		Map<String, List<String>> result = muscleService.getAllMusclesWithSortingByCategory();
		
		//Then
		assertEquals(targetTypes.length, result.size());
		
		for(String category: result.keySet()) {
			assertEquals(numForEachTargetType, result.get(category).size());
		}
	}
	
	/*
	 * public List<Muscle> getMuscleListFromMuscleNameList(Iterable<String> muscleNames) throws MuscleNotFoundedException 
	 */
	
	@Test
	@DisplayName("Muscle name List 이용하여 Muscle List 반환 -> 해당 이름의 Muscle 없을 때")
	public void getMuscleListFromMuscleNameListNonExist() {
		//Given
		String nonExistName = "nonExist";
		List<String> muscleNames = new ArrayList<>();
		muscleNames.add(nonExistName);
		
		given(muscleRepository.findByName(nonExistName)).willReturn(Optional.empty());
		
		//When, Then
		assertThrows(MuscleNotFoundedException.class, () -> {
			muscleService.getMuscleListFromMuscleNameList(muscleNames);
		});
	}
	
	@Test
	@DisplayName("Muscle name List 이용하여 Muscle List 반환 -> 정상")
	public void getMuscleListFromMuscleNameListTest() {
		//Given
		String muscleName = "muscle";
		List<String> muscleNames = new ArrayList<>();
		muscleNames.add(muscleName);
		
		given(muscleRepository.findByName(muscleName)).willReturn(Optional.of(muscle));
		
		//When
		List<Muscle> result = muscleService.getMuscleListFromMuscleNameList(muscleNames);
		
		//Then
		assertEquals(muscle, result.get(0));
	}
	
	/*
	 * public Muscle save(Muscle muscle)
	 */
	
	@Test
	@DisplayName("Muscle 저장 -> 정상")
	public void saveTest() {
		//Given
		given(muscleRepository.save(muscle)).willReturn(muscle);
		
		//When
		Muscle result = muscleService.save(muscle);
		
		//Then
		assertEquals(muscle, result);
	}
	
	/*
	 * public void delete(Muscle muscle)
	 */
	
	@Test
	@DisplayName("Muscle 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		muscleService.delete(muscle);
		
		//Then
		then(muscleRepository).should(times(1)).delete(muscle);
	}
	
	/*
	 * public void deleteById(Long id)
	 */
	
	@Test
	@DisplayName("ID를 만족하는 Muscle 삭제 -> ID를 만족하는 Muscle 없음")
	public void deleteByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(muscleRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		muscleService.deleteById(nonExistId);
		
		//Then
		then(muscleRepository).should(never()).delete(any(Muscle.class));
	}
	
	@Test
	@DisplayName("ID를 만족하는 Muscle 삭제 -> 정상")
	public void deleteByIdTest() {
		//Given
		Long muscleId = Long.valueOf(1);
		
		given(muscleRepository.findById(muscleId)).willReturn(Optional.of(muscle));
		
		//When
		muscleService.deleteById(muscleId);
		
		//Then
		then(muscleRepository).should(times(1)).delete(muscle);
	}
	
	/*
	 * public long countAll()
	 */
	
	@Test
	@DisplayName("모든 Muscle의 개수 반환 -> 정상")
	public void countAllTest() {
		//Given
		long num = 1;
		given(muscleRepository.count()).willReturn(num);
		
		//When
		long result = muscleService.countAll();
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * public long countAllWithNameKeyword(String keyword)
	 */
	
	@Test
	@DisplayName("name 키워드 만족하는 모든 Muscle 개수 반환 -> 정상")
	public void countAllWithNameKeyword() {
		long num = 1;
		String keyword = "keyword";
		given(muscleRepository.countAllWithNamekeyword(keyword)).willReturn(num);
		
		//When
		long result = muscleService.countAllWithNameKeyword(keyword);
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * public boolean existsByName(String name)
	 */
	
	@Test
	@DisplayName("name을 만족하느 Muscle 존재 여부 반환 -> 정상")
	public void existsByNameTest() {
		//Given
		String name = "muscle";
		boolean isExist = true;
		
		given(muscleRepository.existsByName(name)).willReturn(isExist);
		
		//When
		boolean result = muscleService.existsByName(name);
		
		//Then
		assertEquals(isExist, result);
	}
}
