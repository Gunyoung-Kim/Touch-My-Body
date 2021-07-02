package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.services.domain.exercise.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerUserRestController {
	
	private final CommentService commentService;
	
	/**
	 * 
	 * @param commentId
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/usermanage/{user_id}/comments/remove", method = RequestMethod.DELETE)
	public void removeCommentByManager(@PathVariable("user_id") Long userId,@RequestParam("comment_id") Long commentId) {
		commentService.deleteById(commentId);
	}
}
