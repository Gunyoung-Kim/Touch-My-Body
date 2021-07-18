package com.gunyoung.tmb.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.dto.response.ExerciseInfoBySortDTO;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;

import lombok.RequiredArgsConstructor;

/**
 * Exercise 관련 요청 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ExerciseRestController {	
	
	private final ExerciseService exerciseService;
	
	/**
	 * 각 부위별 운동 종류 반환하는 메소드 <br>
	 * Exercise를 target으로 분류해서 반환
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/getexercises",method=RequestMethod.GET)
	public List<ExerciseInfoBySortDTO> getExercisesByNameAndTarget() {
		List<ExerciseInfoBySortDTO> resultList = new ArrayList<>();
		Map<String ,List<String>> map = exerciseService.getAllExercisesNamewithSorting();
		
		Set<String> keySet = map.keySet();
		
		for(String key: keySet) {
			resultList.add(new ExerciseInfoBySortDTO(key,map.get(key)));
		}
		
		return resultList;
	}
}
