package com.gunyoung.tmb.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.SaveMuscleDTO;
import com.gunyoung.tmb.dto.response.MuscleForTableDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.MuscleErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.MuscleNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;

import lombok.RequiredArgsConstructor;

/**
 * 매니저의 Muscle 관리 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerMuscleController {
	
	public static final int MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE = PageSize.MUSCLE_FOR_MANAGE_PAGE_SIZE.getSize();
	
	private final MuscleService muscleService;
	
	/**
	 * Muscle의 리스트 화면 반환하는 메소드
	 * @param keyword Muscle Name 검색 키워드 
	 * @author kimgun-yeong
	 */
	@GetMapping(value="/manager/muscle")
	public ModelAndView muscleViewForManager(@RequestParam(value="page", required=false, defaultValue="1") int page, @RequestParam(value="keyword", required=false) String keyword,
			ModelAndPageView mav) {
		Page<Muscle> pageResult = getPageResultForMuscleViewForManager(keyword, page);
		long totalPageNum = getTotalPageNumForMuscleViewForManager(keyword);
		
		List<MuscleForTableDTO> listObject = MuscleForTableDTO.of(pageResult);
		
		mav.addObject("listObject",listObject);
		mav.setPageNumbers(page, totalPageNum);
		
		mav.setViewName("muscleListViewForManage");
		
		return mav;
	}
	
	private Page<Muscle> getPageResultForMuscleViewForManager(String keyword, Integer page) {
		if(keyword == null) {
			return muscleService.findAllInPage(page, MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE);
		}
		return muscleService.findAllWithNameKeywordInPage(keyword, page, MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE);
	}
	
	private long getTotalPageNumForMuscleViewForManager(String keyword) {
		if(keyword == null) {
			return muscleService.countAll() / MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE +1;
		}
		return muscleService.countAllWithNameKeyword(keyword) / MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE +1;
	}
	
	/**
	 * 근육 추가 화면 반환하는 메소드 
	 * @author kimgun-yeong
	 */
	@GetMapping(value="/manager/muscle/add")
	public ModelAndView addMuscleView(ModelAndView mav) {
		List<String> koreanNamesForAllTargetType = TargetType.getKoreanNamesForAllTargetType();
		
		mav.addObject("targetTypes", koreanNamesForAllTargetType);
		
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
	@PostMapping(value="/manager/muscle/add")
	public ModelAndView addMuscle(@ModelAttribute SaveMuscleDTO dto) {
		if(muscleService.existsByName(dto.getName())) {
			throw new MuscleNameDuplicationFoundedException(MuscleErrorCode.MUSCLE_NAME_DUPLICATION_FOUNDED_ERROR.getDescription());
		}
		
		Muscle newMuscle = dto.createMuscle();
		muscleService.save(newMuscle);
		
		return new ModelAndView("redirect:/manager/muscle");
	}
	
	/**
	 * Muscle 정보 수정 화면 반환하는 메소드 
	 * @param muscleId 정보 수정하려는 대상 Muscle의 Id
	 * @throws MuscleNotFoundedException 해당 Id의 Muscle 없으면
	 * @author kimgun-yeong
	 */
	@GetMapping(value="/manager/muscle/modify/{muscleId}")
	public ModelAndView modifyMuscleView(@PathVariable("muscleId") Long muscleId, ModelAndView mav) {
		Muscle muscle = muscleService.findById(muscleId);
		if(muscle == null) {
			throw new MuscleNotFoundedException(MuscleErrorCode.MUSCLE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		List<String> koreanNamesForAllTargetType = TargetType.getKoreanNamesForAllTargetType();
		
		SaveMuscleDTO saveMuscleDTO = SaveMuscleDTO.of(muscle);
		
		mav.addObject("muscleId", muscleId);
		mav.addObject("targetTypes", koreanNamesForAllTargetType);
		mav.addObject("muscleInfo", saveMuscleDTO);
		
		mav.setViewName("modifyMuscle");
		
		return mav;
	}
	
}
