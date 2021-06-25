package com.gunyoung.tmb.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.ExercisePostErrorCode;
import com.gunyoung.tmb.error.codes.JoinErrorCode;
import com.gunyoung.tmb.error.codes.LikeErrorCode;
import com.gunyoung.tmb.error.codes.MuscleErrorCode;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.EmailDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.NickNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExercisePostNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.LikeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;

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
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(UserNotFoundedException.class)
	public ErrorMsg userNotFounded(UserNotFoundedException e) {
		return new ErrorMsg(UserErrorCode.UserNotFoundedError.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(ExerciseNotFoundedException.class)
	public ErrorMsg exerciseNotFounded(ExerciseNotFoundedException e) {
		return new ErrorMsg(ExerciseErrorCode.ExerciseByNameNotFoundedError.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(MuscleNotFoundedException.class)
	public ErrorMsg muscleNotFounded(MuscleNotFoundedException e) {
		return new ErrorMsg(MuscleErrorCode.MuscleNotFoundedError.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(ExercisePostNotFoundedException.class)
	public ErrorMsg exercisePostNotFounded(ExercisePostNotFoundedException e) {
		return new ErrorMsg(ExercisePostErrorCode.ExercisePostNotFoundedError.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(TargetTypeNotFoundedException.class)
	public ErrorMsg targetTypeNotFounded(TargetTypeNotFoundedException e) {
		return new ErrorMsg(TargetTypeErrorCode.TargetTypeNotFoundedError.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(LikeNotFoundedException.class)
	public ErrorMsg likeNotFounded(LikeNotFoundedException e) {
		return new ErrorMsg(LikeErrorCode.LikeNotFoundedError.getCode(),e.getMessage());
	}
	
}
