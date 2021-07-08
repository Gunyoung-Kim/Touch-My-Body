package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.AddMuscleDTO;
import com.gunyoung.tmb.dto.response.MuscleForTableDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.MuscleErrorCode;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.MuscleNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
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
	
	/**
	 * 근육 추가 화면 반환하는 메소드 
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle/add" , method= RequestMethod.GET)
	public ModelAndView addMuscleView(ModelAndView mav) {
		List<String> targetTypeStrings = new ArrayList<>();
		for(TargetType tt: TargetType.values()) {
			targetTypeStrings.add(tt.getKoreanName());
		}
		
		mav.addObject("targetTypes", targetTypeStrings);
		mav.setViewName("addMuscle");
		
		return mav;
	}
	
	/**
	 * 근육 추가 처리하는 메소드
	 * @param dto
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle/add" ,method=RequestMethod.POST)
	public ModelAndView addMuscle(@ModelAttribute AddMuscleDTO dto) {
		if(muscleService.existsByName(dto.getName())) {
			throw new MuscleNameDuplicationFoundedException(MuscleErrorCode.MUSCLE_NAME_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		TargetType newMusclesCategory = null;
		
		String category = dto.getCategory();
		
		for(TargetType tt: TargetType.values()) {
			if(tt.getKoreanName().equals(category)) {
				newMusclesCategory = tt;
				break;
			}
		}
		
		if(newMusclesCategory == null) {
			throw new TargetTypeNotFoundedException(TargetTypeErrorCode.TARGET_TYPE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Muscle newMuscle = Muscle.builder()
				.name(dto.getName())
				.category(newMusclesCategory)
				.build();
		
		muscleService.save(newMuscle);
		
		return new ModelAndView("redirect:/manager/muscle");
	}
	
	/**
	 * 
	 * @param muscleId
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/muscle/modify/{muscleId}", method = RequestMethod.GET)
	public ModelAndView modifyMuscleView(@PathVariable("muscleId") Long muscleId, ModelAndView mav) {
		Muscle muscle = muscleService.findById(muscleId);
		
		if(muscle == null) {
			throw new MuscleNotFoundedException(MuscleErrorCode.MUSCLE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		List<String> targetTypeStrings = new ArrayList<>();
		for(TargetType tt: TargetType.values()) {
			targetTypeStrings.add(tt.getKoreanName());
		}
		
		mav.addObject("muscleInfo", AddMuscleDTO.of(muscle));
		mav.addObject("targetTypes", targetTypeStrings);
		mav.setViewName("modifyMuscle");
		
		return mav;
	}
	
	/**
	 * 
	 * @param muscleId
	 * @param dto
	 * @return
	 */
	@RequestMapping(value="/manager/muscle/modify/{muscleId}", method = RequestMethod.POST)
	public ModelAndView modifyMuscle(@PathVariable("muscleId") Long muscleId, @ModelAttribute AddMuscleDTO dto) {
		Muscle muscle = muscleService.findById(muscleId);
		
		if(muscle == null) {
			throw new MuscleNotFoundedException(MuscleErrorCode.MUSCLE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		if(!muscle.getName().equals(dto.getName()) && muscleService.existsByName(dto.getName())) {
			throw new MuscleNameDuplicationFoundedException(MuscleErrorCode.MUSCLE_NAME_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		muscle = AddMuscleDTO.toMuscle(muscle, dto);
		
		muscleService.save(muscle);
		
		return new ModelAndView("redirect:/manager/muscle");
	}
}
