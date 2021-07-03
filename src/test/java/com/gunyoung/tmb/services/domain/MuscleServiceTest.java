package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;
import com.gunyoung.tmb.utils.PageUtil;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class MuscleServiceTest {

	private static final int INIT_MUSCLE_NUM = 30;
	
	@Autowired
	MuscleRepository muscleRepository;
	
	@Autowired
	MuscleService muscleService;
	
	@BeforeEach
	void setup() {
		List<Muscle> list = new ArrayList<>();
		for(int i=1;i<=INIT_MUSCLE_NUM;i++) {
			Muscle muscle = Muscle.builder()
									 .name("name" +i)
									 .category(TargetType.ARM)
									 .build();
									 
			list.add(muscle);
		}
		muscleRepository.saveAll(list);
	}
	
	@AfterEach
	void tearDown() {
		muscleRepository.deleteAll();
	}
	
	/*
	 *  public Muscle findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 Muscle 찾기 -> 해당 id의 muscle 없음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<Muscle> list = muscleRepository.findAll();
		
		for(Muscle c: list) {
			maxId = Math.max(maxId, c.getId());
		}
		
		//When
		Muscle result = muscleService.findById(maxId+ 1000);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 Muscle 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Muscle muscle = muscleRepository.findAll().get(0);
		Long id = muscle.getId();
		
		//When
		Muscle result = muscleService.findById(id);
		
		//Then
		
		assertEquals(result != null, true);
		
	}
	
	/*
	 *   public Muscle findByName(String name)
	 */
	
	@Test
	@DisplayName("이름으로 Muscle 찾기 -> 해당 이름의 Muscle 없음")
	public void findByNameNonExist() {
		//Given
		String nonExistName= "none";
		
		//When
		Muscle result = muscleService.findByName(nonExistName);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@DisplayName("이름으로 Muscle 찾기 -> 정상")
	public void findNyNameTest() {
		//Given
		String existName = "name1";
		
		//When
		Muscle result = muscleService.findByName(existName);
		
		//Then
		assertEquals(result != null, true);
	}
	
	/*
	 * public Page<Muscle> findAllInPage(Integer pageNumber, int pageSize)
	 */
	@Test
	@Transactional
	@DisplayName("모든 근육 정보들을 페이지 처리해서 가져오기 ->정상")
	public void findAllInPageTest() {
		//Given
		int pageSize = PageUtil.MUSCLE_FOR_MANAGE_PAGE_SIZE;
		
		//When
		List<Muscle> result = muscleService.findAllInPage(1, pageSize).getContent();
		
		//Then
		assertEquals(result.size(),Math.min(pageSize, INIT_MUSCLE_NUM));
	}
	
	/*
	 * public Page<Muscle> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize)
	 */
	@Test
	@Transactional
	@DisplayName("키워드 이름에 포함하는 근육정보들 페이지 처리해서 가져오기 ->정상")
	public void findAllWithNameKeywordInPageTest() {
		//Given
		String allKeyword = "name";
		String noneKeyword = "none!!!";
		String oneKeyword = String.valueOf(INIT_MUSCLE_NUM);
		
		int pageSize = PageUtil.MUSCLE_FOR_MANAGE_PAGE_SIZE;
		
		//When
		List<Muscle> allResult = muscleService.findAllWithNameKeywordInPage(allKeyword, 1, pageSize).getContent();
		List<Muscle> noneResult = muscleService.findAllWithNameKeywordInPage(noneKeyword, 1, pageSize).getContent();
		List<Muscle> oneResult = muscleService.findAllWithNameKeywordInPage(oneKeyword, 1, pageSize).getContent();
		
		//Then
		assertEquals(allResult.size(),Math.min(pageSize, INIT_MUSCLE_NUM));
		assertEquals(noneResult.size(),0);
		assertEquals(oneResult.size(),1);
	}
	
	/*
	 *  public Map<String, List<String>> getAllMusclesWithSortingByCategory()
	 */
	
	@Test
	@Transactional
	@DisplayName("카테고리로 분류해서 Muscle들 반환 -> 정상")
	public void getAllMusclesWithSortingByCategoryTest() {
		//Given
		Muscle muscle = Muscle.builder()
				 .name("new")
				 .category(TargetType.BACK)
				 .build();
		muscleRepository.save(muscle);
		
		//When
		Map<String, List<String>> result = muscleService.getAllMusclesWithSortingByCategory();
		
		//Then
		assertEquals(result.size(),2);
		assertEquals(result.get(TargetType.ARM.getKoreanName()).size(),INIT_MUSCLE_NUM);
		assertEquals(result.get(TargetType.BACK.getKoreanName()).size(),1);
	}
	
	/*
	 *  public Muscle save(Muscle muscle)
	 */
	
	@Test
	@Transactional
	@DisplayName("Muscle 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		Muscle existMuscle = muscleRepository.findAll().get(0);
		Long id = existMuscle.getId();
		existMuscle.setName("Changed Name");
		
		//When
		muscleService.save(existMuscle);
		
		//Then
		Muscle result = muscleRepository.findById(id).get();
		assertEquals(result.getName(),"Changed Name");
	}
	
	@Test
	@Transactional
	@DisplayName("Muscle 추가하기 -> 정상")
	public void saveTest() {
		//Given
		Muscle newMuscle = Muscle.builder()
									.name("new Name")
									.category(TargetType.BACK)
									.build();
		Long beforeNum = muscleRepository.count();
		
		//When
		muscleService.save(newMuscle);
		
		//Then
		assertEquals(beforeNum+1,muscleRepository.count());
	}
	
	/*
	 *  public void delete(Muscle muscle)
	 */
	
	@Test
	@Transactional
	@DisplayName("Muscle 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		Muscle existMuscle = muscleRepository.findAll().get(0);
		Long beforeNum = muscleRepository.count();
		
		//When
		muscleService.delete(existMuscle);
		
		//Then
		assertEquals(beforeNum-1,muscleRepository.count());
	}
	
	/*
	 *  public long countAllWithNameKeyword(String keyword)
	 */
	
	@Test
	@DisplayName("키워드를 이름에 포함하는 Muscle 개수 세기 -> 정상")
	public void countAllWithNameKeywordTest() {
		//Given
		String allKeyword = "name";
		String noneKeyword = "none!!!";
		String oneKeyword = String.valueOf(INIT_MUSCLE_NUM);
		
		//When
		long resultAll = muscleService.countAllWithNameKeyword(allKeyword);
		long resultZero = muscleService.countAllWithNameKeyword(noneKeyword);
		long resultOne = muscleService.countAllWithNameKeyword(oneKeyword);
		
		//Then
		assertEquals(resultAll,INIT_MUSCLE_NUM);
		assertEquals(resultZero,0);
		assertEquals(resultOne,1);
	}
}
