package com.gunyoung.tmb.error;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gunyoung.tmb.error.codes.CommentErrorCode;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.ExercisePostErrorCode;
import com.gunyoung.tmb.error.codes.FeedbackErrorCode;
import com.gunyoung.tmb.error.codes.JoinErrorCode;
import com.gunyoung.tmb.error.codes.LikeErrorCode;
import com.gunyoung.tmb.error.codes.MuscleErrorCode;
import com.gunyoung.tmb.error.codes.PrivacyPolicyErrorCode;
import com.gunyoung.tmb.error.codes.SearchCriteriaErrorCode;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.EmailDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.ExerciseNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.LikeAlreadyExistException;
import com.gunyoung.tmb.error.exceptions.duplication.MuscleNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.duplication.NickNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.CommentNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExercisePostNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.FeedbackNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.LikeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.PrivacyPolicyNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.SessionAttributesNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.notmatch.UserNotMatchException;
import com.gunyoung.tmb.error.exceptions.request.AccessDeniedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;

/**
 * 컨트롤러에서 Exeption 발생했을때 이를 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestControllerAdvice
public class ErrorController {
	
	/*
	 * --------------------- CONFLICT ------------------------------------------------
	 */
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(EmailDuplicationFoundedException.class)
	public ErrorMsg emailDuplicated(EmailDuplicationFoundedException e) {
		return new ErrorMsg(JoinErrorCode.EMAIL_DUPLICATION_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(NickNameDuplicationFoundedException.class)
	public ErrorMsg nickNameDuplicated(NickNameDuplicationFoundedException e) {
		return new ErrorMsg(JoinErrorCode.NICKNAME_DUPLICATION_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ExerciseNameDuplicationFoundedException.class) 
	public ErrorMsg exerciseNameDuplicated(ExerciseNameDuplicationFoundedException e) {
		return new ErrorMsg(ExerciseErrorCode.EXERCISE_NAME_DUPLICATION_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.CONFLICT) 
	@ExceptionHandler(MuscleNameDuplicationFoundedException.class)
	public ErrorMsg muscleNameDuplicated(MuscleNameDuplicationFoundedException e) {
		return new ErrorMsg(MuscleErrorCode.MUSCLE_NAME_DUPLICATION_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.CONFLICT) 
	@ExceptionHandler(LikeAlreadyExistException.class)
	public ErrorMsg likeAlreadyExisst(LikeAlreadyExistException e) {
		return new ErrorMsg(LikeErrorCode.LIKE_ALREADY_EXIST_ERROR.getCode(),e.getMessage());
	}
	
	/*
	 *  --------------------- NO CONTENT ------------------------------------------------
	 */
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(UserNotFoundedException.class)
	public ErrorMsg userNotFounded(UserNotFoundedException e) {
		return new ErrorMsg(UserErrorCode.USER_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(ExerciseNotFoundedException.class)
	public ErrorMsg exerciseNotFounded(ExerciseNotFoundedException e) {
		return new ErrorMsg(ExerciseErrorCode.EXERCISE_BY_NAME_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(MuscleNotFoundedException.class)
	public ErrorMsg muscleNotFounded(MuscleNotFoundedException e) {
		return new ErrorMsg(MuscleErrorCode.MUSCLE_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(ExercisePostNotFoundedException.class)
	public ErrorMsg exercisePostNotFounded(ExercisePostNotFoundedException e) {
		return new ErrorMsg(ExercisePostErrorCode.EXERCISE_POST_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(TargetTypeNotFoundedException.class)
	public ErrorMsg targetTypeNotFounded(TargetTypeNotFoundedException e) {
		return new ErrorMsg(TargetTypeErrorCode.TARGET_TYPE_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(LikeNotFoundedException.class)
	public ErrorMsg likeNotFounded(LikeNotFoundedException e) {
		return new ErrorMsg(LikeErrorCode.LIKE_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(CommentNotFoundedException.class)
	public ErrorMsg commentNotFounded(CommentNotFoundedException e) {
		return new ErrorMsg(CommentErrorCode.COMMENT_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(FeedbackNotFoundedException.class)
	public ErrorMsg feedbackNotFounded(FeedbackNotFoundedException e) {
		return new ErrorMsg(FeedbackErrorCode.FEEDBACK_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(PrivacyPolicyNotFoundedException.class)
	public ErrorMsg privacyPolicyNotFounded(PrivacyPolicyNotFoundedException e) {
		return new ErrorMsg(PrivacyPolicyErrorCode.PRIVACY_NOT_FOUNDED_ERROR.getCode(),e.getMessage());
	}
	
	/*
	 * --------------------- FORBIDDEN ------------------------------------------------
	 */
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(UserNotMatchException.class)
	public ErrorMsg userNotMatch(UserNotMatchException e) {
		return new ErrorMsg(UserErrorCode.USER_NOT_MATCH_ERROR.getCode(), e.getMessage());
	}
	
	/*
	 * --------------------- BAD_REQUEST ------------------------------------------------
	 */
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(SearchCriteriaInvalidException.class)
	public ErrorMsg searchCriteriaInvalid(SearchCriteriaInvalidException e) {
		return new ErrorMsg(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getCode(),e.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AccessDeniedException.class)
	public ErrorMsg accessDenied(AccessDeniedException e) {
		return new ErrorMsg(UserErrorCode.ACESS_DENIED_ERROR.getCode(), e.getMessage());
	}
	
	/*
	 *  --------------------- REDIRECT ------------------------------------------------
	 */
	
	@ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
	@ExceptionHandler(SessionAttributesNotFoundedException.class)
	public void sessionAttributesNotFounded(SessionAttributesNotFoundedException e,HttpServletResponse response) {
		try {
			response.sendRedirect("/login");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
