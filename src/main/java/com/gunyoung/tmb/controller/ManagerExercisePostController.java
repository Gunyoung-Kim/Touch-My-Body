package com.gunyoung.tmb.controller;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.utils.PageUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ManagerExercisePostController {
	
	private final ExercisePostService exercisePostService;
	
	/**
	 * 
	 * @param page
	 * @param keyword
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/community", method = RequestMethod.GET)
	public ModelAndView manageCommunityView(@RequestParam(value="page", defaultValue="1") int page,
			@RequestParam(value ="keyword", required=false) String keyword,ModelAndView mav) {
		int pageSize = PageUtil.POST_FOR_MANAGE_PAGE_SIZE;
		
		Page<PostForCommunityViewDTO> pageResult;
		long totalPageNum;
		
		if(keyword != null) {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, page, pageSize);
			totalPageNum = exercisePostService.countWithTitleAndContentsKeyword(keyword)/pageSize +1;
		} else {
			pageResult = new PageImpl<PostForCommunityViewDTO>(new ArrayList<>());
			totalPageNum = 1;
		}
		
		mav.setViewName("communityForManage");
		mav.addObject("listObject", pageResult);
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		return mav;
	}
	
}
