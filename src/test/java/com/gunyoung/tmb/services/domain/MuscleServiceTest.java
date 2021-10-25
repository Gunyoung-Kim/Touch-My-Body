package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseMuscleRepository;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;
import com.gunyoung.tmb.testutil.ExerciseMuscleTest;
import com.gunyoung.tmb.testutil.MuscleTest;
import com.gunyoung.tmb.testutil.TargetTypeTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * MuscleService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class MuscleServiceTest {
	
	@Autowired
	ExerciseMuscleRepository exerciseMuscleRepository;
	
	@Autowired
	MuscleRepository muscleRepository;
	
	@Autowired
	MuscleService muscleService;
	
	private Muscle muscle;
	
	@BeforeEach
	void setup() {
		muscle = MuscleTest.getMuscleInstance();
		muscleRepository.save(muscle);
	}
	
	@AfterEach
	void tearDown() {
		muscleRepository.deleteAll();
	}
	
	/*
	 *  Muscle findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 Muscle 찾기 -> 해당 id의 muscle 없음")
	void findByIdNonExist() {
		//Given
		long nonExistId = MuscleTest.getNonExistMuscleId(muscleRepository);
		
		//When
		Muscle result = muscleService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 Muscle 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long existMuscleId = muscle.getId();
		
		//When
		Muscle result = muscleService.findById(existMuscleId);
		
		//Then
		assertNotNull(result);
		
	}
	
	/*
	 *   Muscle findByName(String name)
	 */
	
	@Test
	@DisplayName("이름으로 Muscle 찾기 -> 해당 이름의 Muscle 없음")
	void findByNameNonExist() {
		//Given
		String nonExistName= "none";
		
		//When
		Muscle result = muscleService.findByName(nonExistName);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("이름으로 Muscle 찾기 -> 정상")
	void findNyNameTest() {
		//Given
		String existName = muscle.getName();
		
		//When
		Muscle result = muscleService.findByName(existName);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 * Page<Muscle> findAllInPage(Integer pageNumber, int pageSize)
	 */
	@Test
	@Transactional
	@DisplayName("모든 근육 정보들을 페이지 처리해서 가져오기 ->정상")
	void findAllInPageTest() {
		//Given
		int pageSize = PageSize.MUSCLE_FOR_MANAGE_PAGE_SIZE.getSize();
		
		MuscleTest.addNewMusclesInDBByNum(10, muscleRepository);
		
		long givenMuscleTest = muscleRepository.count();
		
		//When
		List<Muscle> result = muscleService.findAllInPage(1, pageSize).getContent();
		
		//Then
		assertEquals(Math.min(pageSize, givenMuscleTest), result.size());
	}
	
	/*
	 * Page<Muscle> findAllWithNameKeywordInPage(String keyword, Integer pageNumber, int pageSize)
	 */
	
	@Test
	@Transactional
	@DisplayName("키워드 이름에 포함하는 근육정보들 페이지 처리해서 가져오기 ->정상, 모든 Muscle이 만족하는 키워드")
	void findAllWithNameKeywordInPageTestAll() {
		//Given
		int pageSize = PageSize.MUSCLE_FOR_MANAGE_PAGE_SIZE.getSize();
		String keywordForAllMuscle = MuscleTest.getMuscleInstance().getName();
		
		MuscleTest.addNewMusclesInDBByNum(10, muscleRepository);
		
		long givenMuscleNum = muscleRepository.count();
		
		//When
		List<Muscle> result = muscleService.findAllWithNameKeywordInPage(keywordForAllMuscle, 1, pageSize).getContent();
		
		//Then
		assertEquals(Math.min(pageSize, givenMuscleNum), result.size());
	}
	
	@Test
	@Transactional
	@DisplayName("키워드 이름에 포함하는 근육정보들 페이지 처리해서 가져오기 ->정상, 단 하나의 Muscle이 만족하는 키워드")
	void findAllWithNameKeywordInPageTestOnlyOne() {
		//Given
		int pageSize = PageSize.MUSCLE_FOR_MANAGE_PAGE_SIZE.getSize();
		String keywordForOnlyOne = "I am only one";
		Muscle onlyOneMuscle = MuscleTest.getMuscleInstance(keywordForOnlyOne, TargetType.ARM);
		muscleRepository.save(onlyOneMuscle);
		
		MuscleTest.addNewMusclesInDBByNum(10, muscleRepository);
		
		//When
		List<Muscle> result = muscleService.findAllWithNameKeywordInPage(keywordForOnlyOne, 1, pageSize).getContent();
		
		//Then
		assertEquals(1, result.size());
	}
	
	@Test
	@Transactional
	@DisplayName("키워드 이름에 포함하는 근육정보들 페이지 처리해서 가져오기 ->정상")
	void findAllWithNameKeywordInPageTest() {
		//Given
		int pageSize = PageSize.MUSCLE_FOR_MANAGE_PAGE_SIZE.getSize();
		String keywordForNothing = "none!!!";
		
		MuscleTest.addNewMusclesInDBByNum(10, muscleRepository);
		
		//When
		List<Muscle> result = muscleService.findAllWithNameKeywordInPage(keywordForNothing, 1, pageSize).getContent();
		
		//Then
		assertEquals(0, result.size());
	}
	
	/*
	 *  Map<String, List<String>> getAllMusclesWithSortingByCategory()
	 */
	
	@Test
	@Transactional
	@DisplayName("카테고리로 분류해서 Muscle들 반환 -> 정상, 단 하나의 Muscle 만 만족하는 TargetType으로 확인")
	void getAllMusclesWithSortingByCategoryTest() {
		//Given
		TargetType targetTypeForOnlyOneMuscle = TargetTypeTest.getAnotherTargetType(MuscleTest.getMuscleInstance().getCategory());
		Muscle onlyOneCategoryMuscle = MuscleTest.getMuscleInstance("onlyOne", targetTypeForOnlyOneMuscle);
		muscleRepository.save(onlyOneCategoryMuscle);
		
		//When
		Map<String, List<String>> result = muscleService.getAllMusclesWithSortingByCategory();
		
		//Then
		assertEquals(1, result.get(targetTypeForOnlyOneMuscle.getKoreanName()).size());
	}
	
	/*
	 * List<Muscle> getMuscleListFromMuscleNameList(Iterable<String> muscleNames) throws MuscleNotFoundedException 
	 */
	
	@Test
	@Transactional
	@DisplayName("Muscle 이름 리스트로 부터 Muscle 리스트 반환하기 -> 리스트중에 해당 이름의 Muscle 없는 경우가 있는 케이스")
	void getMuscleListFromMuscleNameListNonExist() {
		//Given
		String nonExistName = "nonExist";
		
		List<String> muscleNameList = new ArrayList<>();
		muscleNameList.add(nonExistName);
		
		MuscleTest.addNewMusclesInDBByNum(10, muscleRepository);
		
		List<Muscle> muscleList = muscleRepository.findAll();
		for(int i=0;i<muscleList.size();i++) {
			muscleNameList.add(muscleList.get(i).getName());
		}
		
		//When 
		List<Muscle> result = muscleService.findAllByNames(muscleNameList);
		
		//Then
		assertEquals(muscleRepository.count(), result.size());
	}
	
	@Test
	@Transactional
	@DisplayName("Muscle 이름 리스트로 부터 Muscle 리스트 반환하기 -> 정상")
	void getMuscleListFromMuscleNameListTest() {
		//Given
		List<String> muscleNameList = new ArrayList<>();
		
		MuscleTest.addNewMusclesInDBByNum(10, muscleRepository);
		
		List<Muscle> muscleList = muscleRepository.findAll();
		long givenMuscleNum = muscleList.size();
		for(int i=0;i<muscleList.size();i++) {
			muscleNameList.add(muscleList.get(i).getName());
		}
		
		//When
		List<Muscle> result = muscleService.findAllByNames(muscleNameList);
		
		//Then
		assertEquals(givenMuscleNum, result.size());
	}
	
	/*
	 *  Muscle save(Muscle muscle)
	 */
	
	@Test
	@DisplayName("Muscle 수정하기 -> 정상, 변화 확인")
	void mergeTestCheckChange() {
		//Given
		String changeName = "Changed Name";
		Long muscleId = muscle.getId();
		muscle.setName(changeName);
		
		//When
		muscleService.save(muscle);
		
		//Then
		Muscle result = muscleRepository.findById(muscleId).get();
		assertEquals(changeName, result.getName());
	}
	
	@Test
	@DisplayName("Muscle 수정하기 -> 정상, 개수 동일 확인")
	void mergeTestCheckCount() {
		//Given
		String changeName = "Changed Name";
		muscle.setName(changeName);
		
		long givenMuscleNum = muscleRepository.count();
		
		//When
		muscleService.save(muscle);
		
		//Then
		assertEquals(givenMuscleNum,  muscleRepository.count());
	}
	
	@Test
	@DisplayName("Muscle 추가하기 -> 정상")
	void saveTest() {
		//Given
		Muscle newMuscle = MuscleTest.getMuscleInstance("newMuscle");
		Long givenMuscleNum = muscleRepository.count();
		
		//When
		muscleService.save(newMuscle);
		
		//Then
		assertEquals(givenMuscleNum + 1, muscleRepository.count());
	}
	
	/*
	 *  void delete(Muscle muscle)
	 */
	
	@Test
	@DisplayName("Muscle 삭제하기 -> 정상, Muscle 삭제 확인")
	void deleteTestCheckMuscleDelete() {
		//Given
		Long givenMuscleNum = muscleRepository.count();
		
		//When
		muscleService.delete(muscle);
		
		//Then
		assertEquals(givenMuscleNum - 1, muscleRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Muscle 삭제하기 -> 정상, 관련 ExerciseMuscle 삭제 확인")
	void deleteTestCheckExerciseMusclesDelete() {
		//Given
		Long givenExerciseMuscleNum = Long.valueOf(12);
		List<ExerciseMuscle> exerciseMuscles = getExerciseMusclesWithMuscle(givenExerciseMuscleNum);
		exerciseMuscleRepository.saveAll(exerciseMuscles);
		
		//When
		muscleService.delete(muscle);
		
		//Then
		assertEquals(0, exerciseMuscleRepository.count());
	}
	
	private List<ExerciseMuscle> getExerciseMusclesWithMuscle(Long givenExerciseMusclesNum) {
		Long givenExerciseMuscleNum = Long.valueOf(12);
		List<ExerciseMuscle> exerciseMuscles = new ArrayList<>();
		for(int i=0;i<givenExerciseMuscleNum;i++) {
			ExerciseMuscle em = ExerciseMuscleTest.getExerciseMuscleInstance();
			em.setMuscle(muscle);
			exerciseMuscles.add(em);
		}
		return exerciseMuscles;
	}
	
	/*
	 *  long countAllWithNameKeyword(String keyword)
	 */
	
	@Test
	@DisplayName("키워드를 이름에 포함하는 Muscle 개수 세기 -> 정상, 모든 Muscle 이 만족하는 키워드")
	void countAllWithNameKeywordTestAll() {
		//Given
		String keywordForAllMuscle = MuscleTest.DEFAULT_NAME;
		
		MuscleTest.addNewMusclesInDBByNum(10, muscleRepository);
		
		long givenMuscleNum = muscleRepository.count();
		
		//When
		long result = muscleService.countAllWithNameKeyword(keywordForAllMuscle);
		
		//Then
		assertEquals(givenMuscleNum, result);
	}
	
	@Test
	@DisplayName("키워드를 이름에 포함하는 Muscle 개수 세기 -> 정상, 단 하나의 Muscle만 만족하는 키워드")
	void countAllWithNameKeywordTestOnlyOne() {
		//Given
		String keywordForOnlyOneMuscle = "i am only one";
		Muscle onlyOneMuscle = MuscleTest.getMuscleInstance(keywordForOnlyOneMuscle);
		muscleRepository.save(onlyOneMuscle);
		
		MuscleTest.addNewMusclesInDBByNum(10, muscleRepository);
		
		//When
		long result = muscleService.countAllWithNameKeyword(keywordForOnlyOneMuscle);
		
		//Then
		assertEquals(1, result);
	}
	
	@Test
	@DisplayName("키워드를 이름에 포함하는 Muscle 개수 세기 -> 정상, 어느 Muscle 도 만족하지 않는 키워드")
	void countAllWithNameKeywordTestNothing() {
		//Given
		String keywordForNothing = "nothing.!!";
		
		MuscleTest.addNewMusclesInDBByNum(10, muscleRepository);
		
		//When
		long result = muscleService.countAllWithNameKeyword(keywordForNothing);
		
		//Then
		assertEquals(0, result);
	}
}
