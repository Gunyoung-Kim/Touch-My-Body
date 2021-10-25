package com.gunyoung.tmb.controller.unit;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.ManagerController;

/**
 * {@link ManagerController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ManagerControllerUnitTest {
	
	@InjectMocks
	ManagerController managerController;
	
	private ModelAndView mav;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
	}
	
	/*
	 * ModelAndView managerView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("매니저 메인 화면 반환 -> 정상")
	void managerViewTest() {
		//Given
		
		//When
		managerController.managerView(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("manager");
	}
}
