package com.gunyoung.tmb.controller;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;

import lombok.RequiredArgsConstructor;

/**
 * 매니저의 게시글 관리 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerExercisePostController {
	
	public static final int POST_FOR_MANAGE_PAGE_SIZE = PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize();
	
	private final ExercisePostService exercisePostService;
	
	/**
	 * ExercisePost 리스트 화면 반환하는 메소드 <br>
	 * 키워드 빈 문자열일 시 아무것도 리스트에 반환하지 않음
	 * @param keyword title,contents 검색 키워드
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/manager/community")
	public ModelAndView manageCommunityView(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "keyword", required = false) String keyword, ModelAndPageView mav) {
		Page<PostForCommunityViewDTO> pageResult = new PageImpl<>(new ArrayList<>());
		long totalPageNum = 1;
		
		if(keyword != null) {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, page, POST_FOR_MANAGE_PAGE_SIZE);
			totalPageNum = exercisePostService.countWithTitleAndContentsKeyword(keyword)/POST_FOR_MANAGE_PAGE_SIZE + 1;
		}
		
		mav.addObject("listObject", pageResult);
		mav.setPageNumbers(page, totalPageNum);
		
		mav.setViewName("communityForManage");
		
		return mav;
	}
	
}
