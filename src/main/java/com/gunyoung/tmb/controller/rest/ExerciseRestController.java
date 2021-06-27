package com.gunyoung.tmb.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.services.domain.exercise.ExerciseService;

@RestController
public class ExerciseRestController {
	
	@Autowired
	ExerciseService exerciseService;
	
	
}
