package com.gunyoung.tmb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.utils.PageUtil;

@Controller
public class ExercisePostcontroller {
	
	@Autowired
	ExercisePostService exercisePostService;
	
	/**
	 * 특정 운동 관련 게시글 화면 반환하는 메소드 
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/community",method=RequestMethod.GET)
	public ModelAndView exercisePostView(@RequestParam(value="page", required = false,defaultValue="1") int page,@RequestParam(value="keyword",required=false)String keyword,ModelAndView mav) {
		mav.setViewName("community");
		int page_size = PageUtil.COMMUNITY_PAGE_SIZE;
		
		Page<PostForCommunityViewDTO> pageResult;
		long totalPageNum;
		
		if(keyword != null) {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOByPage(page);
			totalPageNum = exercisePostService.count();
		} else {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, page);
			totalPageNum = exercisePostService.countWithTitleAndContentsKeyword(keyword);
		}
		
		List<PostForCommunityViewDTO> resultList = pageResult.getContent();
		
		mav.addObject("listObject",resultList);
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/page_size)*page_size+1);
		mav.addObject("lastIndex",(page/page_size)*page_size+page_size-1 > totalPageNum ? totalPageNum : (page/page_size)*page_size+page_size-1);
		
		return mav;
	}
	
	@RequestMapping(value="/community/{target}",method = RequestMethod.GET)
	public ModelAndView exercisePostViewWithTarget(@RequestParam(value="page", required = false,defaultValue="1") int page
			,@RequestParam(value="keyword",required=false)String keyword,ModelAndView mav,@PathVariable("target") String targetName) {
		TargetType type;
		try {
			type = TargetType.valueOf(targetName);
		} catch(IllegalArgumentException e) {
			throw new TargetTypeNotFoundedException(TargetTypeErrorCode.TargetTypeNotFoundedError.getDescription());
		}
		
		mav.setViewName("community");
		int page_size = PageUtil.COMMUNITY_PAGE_SIZE;
		
		Page<PostForCommunityViewDTO> pageResult;
		long totalPageNum;
		
		if(keyword != null) {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(type, page);
			totalPageNum = exercisePostService.countWithTarget(type);
		} else {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(type, keyword, page);
			totalPageNum = exercisePostService.countWithTargetAndKeyword(type, keyword);
		}
		
		List<PostForCommunityViewDTO> resultList = pageResult.getContent();
		
		mav.addObject("listObject",resultList);
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/page_size)*page_size+1);
		mav.addObject("lastIndex",(page/page_size)*page_size+page_size-1 > totalPageNum ? totalPageNum : (page/page_size)*page_size+page_size-1);
		
		return mav;

	}
	
}
