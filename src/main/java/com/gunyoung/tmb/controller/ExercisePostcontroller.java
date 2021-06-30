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

import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.ExercisePostErrorCode;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExercisePostNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.utils.PageUtil;

@Controller
public class ExercisePostController {
	
	@Autowired
	ExercisePostService exercisePostService;
	
	@Autowired
	CommentService commentService;
	
	/**
	 * 커뮤니티 메인 화면 반환하는 메소드
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/community",method=RequestMethod.GET)
	public ModelAndView exercisePostView(@RequestParam(value="page", required = false,defaultValue="1") int page,
			@RequestParam(value="keyword",required=false)String keyword,ModelAndView mav) {
		mav.setViewName("community");
		int page_size = PageUtil.COMMUNITY_PAGE_SIZE;
		
		Page<PostForCommunityViewDTO> pageResult;
		long totalPageNum;
		
		if(keyword == null) {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOByPage(page,PageUtil.COMMUNITY_PAGE_SIZE);
			totalPageNum = exercisePostService.count()/page_size +1;
		} else {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, page,PageUtil.COMMUNITY_PAGE_SIZE);
			totalPageNum = exercisePostService.countWithTitleAndContentsKeyword(keyword)/page_size +1;
		}
		
		List<PostForCommunityViewDTO> resultList = pageResult.getContent();
		
		TargetType[] targetTypes = TargetType.values();
		
		mav.addObject("listObject",resultList);
		mav.addObject("category", "전체");
		mav.addObject("currentPage",page);
		mav.addObject("targetNames", targetTypes);
		mav.addObject("startIndex",(page/page_size)*page_size+1);
		mav.addObject("lastIndex",(page/page_size)*page_size+page_size-1 > totalPageNum ? totalPageNum : (page/page_size)*page_size+page_size-1);
		
		return mav;
	}
	
	/**
	 * 특정 부류 게시글만 반환하는 메소
	 * @param page
	 * @param keyword
	 * @param mav
	 * @param targetName
	 * @return
	 */
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
		
		if(keyword == null) {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(type, page,PageUtil.COMMUNITY_PAGE_SIZE);
			totalPageNum = exercisePostService.countWithTarget(type)/page_size +1;
		} else {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(type, keyword, page,PageUtil.COMMUNITY_PAGE_SIZE);
			totalPageNum = exercisePostService.countWithTargetAndKeyword(type, keyword)/page_size +1;
		}
		
		List<PostForCommunityViewDTO> resultList = pageResult.getContent();
		
		TargetType[] targetTypes = TargetType.values();
		
		mav.addObject("listObject",resultList);
		mav.addObject("targetNames", targetTypes);
		mav.addObject("category", type.getKoreanName());
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/page_size)*page_size+1);
		mav.addObject("lastIndex",(page/page_size)*page_size+page_size-1 > totalPageNum ? totalPageNum : (page/page_size)*page_size+page_size-1);
		
		return mav;

	}
	
	/**
	 * 게시글을 보여주는 뷰 반환하는 메소드 
	 * @param postId
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/community/post/{post_id}" ,method = RequestMethod.GET)
	public ModelAndView exercisePostDetailView(@PathVariable("post_id") Long postId,ModelAndView mav) {
		ExercisePostViewDTO postDTO = exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreasViewNum(postId);
		
		if(postDTO == null) {
			throw new ExercisePostNotFoundedException(ExercisePostErrorCode.ExercisePostNotFoundedError.getDescription());
		}
		
		List<CommentForPostViewDTO> comments = commentService.getCommentForPostViewDTOsByExercisePostId(postId);
		
		mav.setViewName("postView");
		mav.addObject("exercisePost",postDTO);
		mav.addObject("comments", comments);
		
		return mav;
	}
	
}
