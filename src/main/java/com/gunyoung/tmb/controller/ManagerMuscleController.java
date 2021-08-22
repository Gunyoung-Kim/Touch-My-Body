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

import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.SaveMuscleDTO;
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

/**
 * 매니저의 Muscle 관리 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerMuscleController {
	
	private final MuscleService muscleService;
	
	/**
	 * Muscle의 리스트 화면 반환하는 메소드
	 * @param keyword Muscle Name 검색 키워드 
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle",method = RequestMethod.GET)
	public ModelAndView muscleViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,
			ModelAndPageView mav) {
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
		
		List<MuscleForTableDTO> listObject = new ArrayList<>();
		
		for(Muscle m : pageResult) {
			listObject.add(MuscleForTableDTO.of(m));
		}
		
		mav.addObject("listObject",listObject);
		
		mav.setPageNumbers(page, totalPageNum);
		
		mav.setViewName("muscleListViewForManage");
		
		return mav;
	}
	
	/**
	 * 근육 추가 화면 반환하는 메소드 
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle/add" , method= RequestMethod.GET)
	public ModelAndView addMuscleView(ModelAndView mav) {
		List<String> targetTypeKoreanNames = new ArrayList<>();
		for(TargetType tt: TargetType.values()) {
			targetTypeKoreanNames.add(tt.getKoreanName());
		}
		
		mav.addObject("targetTypes", targetTypeKoreanNames);
		
		mav.setViewName("addMuscle");
		
		return mav;
	}
	
	/**
	 * 근육 추가 처리하는 메소드
	 * @param dto
	 * @throws MuscleNameDuplicationFoundedException 추가하려는 Muscle의 Name이 중복된다면
	 * @throws TargetTypeNotFoundedException 추가하려는 Muscle의 category와 일치하는 TargetType 없으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle/add" ,method=RequestMethod.POST)
	public ModelAndView addMuscle(@ModelAttribute SaveMuscleDTO dto) {
		if(muscleService.existsByName(dto.getName())) {
			throw new MuscleNameDuplicationFoundedException(MuscleErrorCode.MUSCLE_NAME_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		String category = dto.getCategory();
		
		TargetType newMusclesCategory = TargetType.getFromKoreanName(category);
		
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
	 * Muscle 정보 수정 화면 반환하는 메소드 
	 * @param muscleId 정보 수정하려는 대상 Muscle의 Id
	 * @throws MuscleNotFoundedException 해당 Id의 Muscle 없으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/muscle/modify/{muscleId}", method = RequestMethod.GET)
	public ModelAndView modifyMuscleView(@PathVariable("muscleId") Long muscleId, ModelAndView mav) {
		Muscle muscle = muscleService.findById(muscleId);
		
		if(muscle == null) {
			throw new MuscleNotFoundedException(MuscleErrorCode.MUSCLE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		List<String> targetTypeKoreanNames = new ArrayList<>();
		for(TargetType tt: TargetType.values()) {
			targetTypeKoreanNames.add(tt.getKoreanName());
		}
		
		mav.addObject("muscleInfo", SaveMuscleDTO.of(muscle));
		mav.addObject("targetTypes", targetTypeKoreanNames);
		mav.addObject("muscleId", muscleId);
		
		mav.setViewName("modifyMuscle");
		
		return mav;
	}
	
}
