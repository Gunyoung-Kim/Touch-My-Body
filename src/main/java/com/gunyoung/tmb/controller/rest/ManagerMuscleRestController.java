package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.AddMuscleDTO;
import com.gunyoung.tmb.error.codes.MuscleErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.MuscleNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerMuscleRestController {
	
	private final MuscleService muscleService;
	
	/**
	 * 
	 * @param muscleId
	 * @param dto
	 * @return
	 */
	@RequestMapping(value="/manager/muscle/modify/{muscleId}", method = RequestMethod.PUT)
	public void modifyMuscle(@PathVariable("muscleId") Long muscleId, @ModelAttribute AddMuscleDTO dto) {
		Muscle muscle = muscleService.findById(muscleId);
		
		if(muscle == null) {
			throw new MuscleNotFoundedException(MuscleErrorCode.MUSCLE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		if(!muscle.getName().equals(dto.getName()) && muscleService.existsByName(dto.getName())) {
			throw new MuscleNameDuplicationFoundedException(MuscleErrorCode.MUSCLE_NAME_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		muscle = AddMuscleDTO.toMuscle(muscle, dto);
		
		muscleService.save(muscle);
	}
	
	
	/**
	 * 
	 * @param muscleId
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle/remove" ,method = RequestMethod.DELETE)
	public void removeMuscle(@RequestParam("muscleId") Long muscleId) {
		muscleService.deleteById(muscleId);
	}	
}
