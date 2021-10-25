package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;

import lombok.RequiredArgsConstructor;

/**
 * ExercisePost 관련 매니저의 요청 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ManagerExercisePostRestController {
	
	private final ExercisePostService exercisePostService;
	
	/**
	 * 매니저가 특정 게시글을 삭제하려는 요청 처리 
	 * @param postId 삭제하려는 대상 ExercisePost의 Id
	 * @author kimgun-yeong
	 */
	@DeleteMapping(value="/manager/community/remove/post")
	public void removeExercisePostByManager(@RequestParam("postId") Long postId) {
		exercisePostService.deleteById(postId);
	}
}
