package com.gunyoung.tmb.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gunyoung.tmb.error.codes.JoinErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.EmailDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.NickNameDuplicationFoundedException;

@RestControllerAdvice
public class ErrorController {
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(EmailDuplicationFoundedException.class)
	public ErrorMsg emailDuplicated(EmailDuplicationFoundedException e) {
		return new ErrorMsg(JoinErrorCode.EmailDuplicationFound.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(NickNameDuplicationFoundedException.class)
	public ErrorMsg nickNameDuplicated(NickNameDuplicationFoundedException e) {
		return new ErrorMsg(JoinErrorCode.NickNameDuplicationFound.getCode(),e.getMessage());
	}
}