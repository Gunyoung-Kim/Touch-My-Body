package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.services.domain.exercise.MuscleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerMuscleRestController {
	
	private final MuscleService muscleService;
	
	@RequestMapping(value="/manager/muscle/remove" ,method = RequestMethod.DELETE)
	public void removeMuscle(@RequestParam("muscleId") Long muscleId) {
		muscleService.deleteById(muscleId);
	}	
}
