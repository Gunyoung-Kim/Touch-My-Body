package com.gunyoung.tmb.error.exceptions.nonexist;

import com.gunyoung.tmb.error.exceptions.BusinessException;

@SuppressWarnings("serial")
public class PrivacyPolicyNotFoundedException extends BusinessException {

	public PrivacyPolicyNotFoundedException(String msg) {
		super(msg);
	}

}
