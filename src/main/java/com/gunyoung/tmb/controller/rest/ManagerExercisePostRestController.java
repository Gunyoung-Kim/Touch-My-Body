package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerExercisePostRestController {
	
	private final ExercisePostService exercisePostService;
	
	/**
	 * 
	 * @param postId
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/community/remove/post", method = RequestMethod.DELETE)
	public void removeExercisePostByManager(@RequestParam("postId") Long postId) {
		exercisePostService.deleteById(postId);
	}
}
