package com.gunyoung.tmb.util;

import com.gunyoung.tmb.enums.TargetType;

/**
 * 테스트용 TargetType 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class TargetTypeTest {
	
	/**
	 * 인자로 입력된 TargetType과 다른 종류의 임의의 TargetType 반환
	 * @return TargetType, null(인자로 입력된 TargetType과 다른 종류의 TargetType 없을때)
	 * @author kimgun-yeong
	 */
	public static TargetType getAnotherTargetType(TargetType targetType) {
		for(TargetType tt: TargetType.values()) {
			if(!tt.equals(targetType)) {
				return tt;
			}
		}
		return null;
	}
}
