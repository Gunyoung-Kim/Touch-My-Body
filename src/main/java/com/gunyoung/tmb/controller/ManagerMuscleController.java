package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.response.MuscleForTableDTO;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;
import com.gunyoung.tmb.utils.PageUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ManagerMuscleController {
	
	private final MuscleService muscleService;
	
	/**
	 * 
	 * @param page
	 * @param keyword
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle",method = RequestMethod.GET)
	public ModelAndView muscleViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav) {
		int pageSize = PageUtil.MUSCLE_FOR_MANAGE_PAGE_SIZE;
		
		Page<Muscle> pageResult;
		long totalPageNum;
		
		if(keyword != null) {
			pageResult = muscleService.findAllWithNameKeywordInPage(keyword, page, pageSize);
			totalPageNum = muscleService.countAllWithNameKeyword(keyword)/pageSize +1;
		} else {
			pageResult = muscleService.findAllInPage(page,pageSize);
			totalPageNum = muscleService.countAll()/pageSize +1;
		}
		
		List<MuscleForTableDTO> resultList = new ArrayList<>();
		
		for(Muscle m : pageResult) {
			resultList.add(MuscleForTableDTO.of(m));
		}
		
		mav.setViewName("muscleListViewForManage");
		mav.addObject("listObject",resultList);
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		return mav;
	}
	
	@RequestMapping(value="/manager/muscle/add" , method= RequestMethod.GET)
	public ModelAndView addMuscleView(ModelAndView mav) {
		return mav;
	}
	
	@RequestMapping(value="/manager/muscle/add" ,method=RequestMethod.POST)
	public ModelAndView addMuscle() {
		return new ModelAndView("redirect:/manager/muscle");
	}
	
	@RequestMapping(value="/manager/muscle/modify", method = RequestMethod.GET)
	public ModelAndView modifyMuscleView(ModelAndView mav) {
		return mav;
	}
	
	@RequestMapping(value="/manager/muscle/modify", method = RequestMethod.POST)
	public ModelAndView modifyMuscle(ModelAndView mav) {
		
		return new ModelAndView("redirect:/manager/muscle/modify");
	}
}
