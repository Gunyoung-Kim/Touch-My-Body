package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.SaveMuscleDTO;
import com.gunyoung.tmb.error.codes.MuscleErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.MuscleNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;

import lombok.RequiredArgsConstructor;

/**
 * 매니저의 Muscle 관련 요청 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ManagerMuscleRestController {
	
	private final MuscleService muscleService;
	
	/**
	 * Muscle 정보 수정 요청 처리하는 메소드
	 * @param muscleId 정보 수정하려는 대상 Muscle의 Id
	 * @throws MuscleNotFoundedException 해당 Id의 Muscle 없으면
	 * @throws MuscleNameDuplicationFoundedException 수정된 이름이 이미 존재하는 Muscle의 이름과 일치한다면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle/modify/{muscleId}", method = RequestMethod.PUT)
	public void modifyMuscle(@PathVariable("muscleId") Long muscleId, @ModelAttribute SaveMuscleDTO dto) {
		Muscle muscle = muscleService.findById(muscleId);
		
		if(muscle == null) {
			throw new MuscleNotFoundedException(MuscleErrorCode.MUSCLE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		if(!muscle.getName().equals(dto.getName()) && muscleService.existsByName(dto.getName())) {
			throw new MuscleNameDuplicationFoundedException(MuscleErrorCode.MUSCLE_NAME_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		muscle = SaveMuscleDTO.toMuscle(muscle, dto);
		
		muscleService.save(muscle);
	}
	
	
	/**
	 * Muscle의 삭제 요철 처리하는 메소드
	 * @param muscleId 삭제하려는 대상 Muscle의 Id
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle/remove" ,method = RequestMethod.DELETE)
	public void removeMuscle(@RequestParam("muscleId") Long muscleId) {
		muscleService.deleteById(muscleId);
	}	
}
