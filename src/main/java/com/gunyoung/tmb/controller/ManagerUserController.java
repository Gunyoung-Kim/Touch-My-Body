package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.UserManageListDTO;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.PageUtil;

@Controller
public class ManagerUserController {
	
	@Autowired
	UserService userService;
	
	/**
	 * 매니저들의 유저 검색 (for managing) 페이지 반환
	 * @param mav
	 * @param page 검색하려는 페이지
	 * @param keyword 검색 키워드
	 * @return
	 */
	@RequestMapping(value="/manager/usermanage",method= RequestMethod.GET)
	public ModelAndView userManageView(@RequestParam(value="page", required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav) {
		mav.setViewName("userManage");
		int page_size = PageUtil.BY_NICKNAME_NAME_PAGE_SIZE;
		
		Page<User> pageResult;
		long totalPageNum;
		
		if(keyword != null) {
			pageResult = userService.findAllByNickNameOrName(keyword, page);
			totalPageNum = userService.countAllByNickNameOrName(keyword)/page_size +1;
		} else {
			pageResult = new PageImpl<User>(new ArrayList<>());
			totalPageNum = 1;
		}
		
		List<UserManageListDTO> resultList = new ArrayList<>();
		
		for(User p: pageResult) {
			resultList.add(UserManageListDTO.of(p));
		}
		
		mav.addObject("listObject",resultList);
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/page_size)*page_size+1);
		mav.addObject("lastIndex",(page/page_size)*page_size+page_size-1 > totalPageNum ? totalPageNum : (page/page_size)*page_size+page_size-1);
		
		return mav;
	}
	
	
}
