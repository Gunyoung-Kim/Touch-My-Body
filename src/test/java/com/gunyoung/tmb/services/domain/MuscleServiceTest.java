package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;

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
}
