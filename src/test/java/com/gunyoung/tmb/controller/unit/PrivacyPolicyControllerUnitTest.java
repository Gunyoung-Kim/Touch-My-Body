package com.gunyoung.tmb.controller.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.gunyoung.tmb.controller.PrivacyPolicyController;
import com.gunyoung.tmb.error.exceptions.nonexist.PrivacyPolicyNotFoundedException;

/**
 * {@link PrivacyPolicyController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) PrivacyPolicyController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class PrivacyPolicyControllerUnitTest {
	
	@InjectMocks
	PrivacyPolicyController privacyPolicyController;
	
	private ModelAndView mav;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
	}
	
	/*
	 * public ModelAndView privacyPolicyLastest(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("가장 최근에 적용된 개인정보 처리방침 문서 화면 반환 -> 정상, View name 확인")
	public void privacyPolicyLastestTest() {
		//Given
		
		//When
		privacyPolicyController.privacyPolicyLastest(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("privacyPolicy_" + PrivacyPolicyController.LATEST_POLICY_VERSION);
	}
	
	/*
	 * public ModelAndView privacyPolicyWithVersion(@PathVariable("version") int version,ModelAndView mav)
	 */
	
	@Test
	@DisplayName("특정 버전의 개인정보 처리방침 화면 반환 -> 버전이 0보다 작거나 같음")
	public void privacyPolicyWithVersionMinusVersion() {
		//Given
		int givenVersion = -1;
		
		//When
		assertThrows(PrivacyPolicyNotFoundedException.class, () -> {
			privacyPolicyController.privacyPolicyWithVersion(givenVersion, mav);
		});
	}
	
	@Test
	@DisplayName("특정 버전의 개인정보 처리방침 화면 반환 -> 버전이 최신 버전보다 높음")
	public void privacyPolicyWithVersionOverVersion() {
		//Given
		int givenVersion = PrivacyPolicyController.LATEST_POLICY_VERSION + 2;
		
		//When
		assertThrows(PrivacyPolicyNotFoundedException.class, () -> {
			privacyPolicyController.privacyPolicyWithVersion(givenVersion, mav);
		});
	}
	
	@Test
	@DisplayName("특정 버전의 개인정보 처리방침 화면 반환 -> 정상")
	public void privacyPolicyWithVersionTest() {
		//Given
		int givenVersion = PrivacyPolicyController.LATEST_POLICY_VERSION;
		
		//When
		privacyPolicyController.privacyPolicyWithVersion(givenVersion, mav);
		
		//Then
		then(mav).should(times(1)).setViewName("privacyPolicy_" + givenVersion);
	}
}
