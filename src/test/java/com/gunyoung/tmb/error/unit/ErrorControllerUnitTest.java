package com.gunyoung.tmb.error.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.error.ErrorController;
import com.gunyoung.tmb.error.ErrorMsg;
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
import com.gunyoung.tmb.error.exceptions.nonexist.RoleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.SessionAttributesNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.error.exceptions.request.AccessDeniedException;
import com.gunyoung.tmb.error.exceptions.request.SearchCriteriaInvalidException;

/**
 * {@link ErrorController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ErrorController only <br>
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ErrorControllerUnitTest {
	
	@InjectMocks
	private ErrorController errorController;
	
	/*
	 * public ErrorMsg emailDuplicated(EmailDuplicationFoundedException e)
	 */
	
	@Test
	public void emailDuplicatedTest() {
		//Given
		String errorMsg = "email Duplicated";
		EmailDuplicationFoundedException exception = new EmailDuplicationFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.emailDuplicated(exception);
		
		//Then
		assertEquals(JoinErrorCode.EMAIL_DUPLICATION_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg nickNameDuplicated(NickNameDuplicationFoundedException e)
	 */
	
	@Test
	public void nickNameDuplicatedTest() {
		//Given
		String errorMsg = "nickName Duplicated";
		NickNameDuplicationFoundedException exception = new NickNameDuplicationFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.nickNameDuplicated(exception);
		
		//Then
		assertEquals(JoinErrorCode.NICKNAME_DUPLICATION_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg exerciseNameDuplicated(ExerciseNameDuplicationFoundedException e)
	 */
	
	@Test
	public void exerciseNameDuplicatedTest() {
		//Given
		String errorMsg = "exercise name Duplicated";
		ExerciseNameDuplicationFoundedException exception = new ExerciseNameDuplicationFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.exerciseNameDuplicated(exception);
		
		//Then
		assertEquals(ExerciseErrorCode.EXERCISE_NAME_DUPLICATION_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg muscleNameDuplicated(MuscleNameDuplicationFoundedException e)
	 */
	
	@Test
	public void muscleNameDuplicatedTest() {
		//Given
		String errorMsg = "muscle name Duplicated";
		MuscleNameDuplicationFoundedException exception = new MuscleNameDuplicationFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.muscleNameDuplicated(exception);
		
		//Then
		assertEquals(MuscleErrorCode.MUSCLE_NAME_DUPLICATION_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg likeAlreadyExisst(LikeAlreadyExistException e)
	 */
	
	@Test
	public void likeAlreadyExisstTest() {
		//Given
		String errorMsg = "like Already Exist";
		LikeAlreadyExistException exception = new LikeAlreadyExistException(errorMsg);
		
		//When
		ErrorMsg result = errorController.likeAlreadyExisst(exception);
		
		//Then
		assertEquals(LikeErrorCode.LIKE_ALREADY_EXIST_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg userNotFounded(UserNotFoundedException e)
	 */
	
	@Test
	public void userNotFoundedTest() {
		//Given
		String errorMsg = "user not founded";
		UserNotFoundedException exception = new UserNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.userNotFounded(exception);
		
		//Then
		assertEquals(UserErrorCode.USER_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg exerciseNotFounded(UserNotFoundedException e)
	 */
	
	@Test
	public void exerciseNotFoundedTest() {
		//Given
		String errorMsg = "exercise not founded";
		ExerciseNotFoundedException exception = new ExerciseNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.exerciseNotFounded(exception);
		
		//Then
		assertEquals(ExerciseErrorCode.EXERCISE_BY_NAME_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg muscleNotFounded(MuscleNotFoundedException e)
	 */
	
	@Test
	public void muscleNotFoundedTest() {
		//Given
		String errorMsg = "muscle not founded";
		MuscleNotFoundedException exception = new MuscleNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.muscleNotFounded(exception);
		
		//Then
		assertEquals(MuscleErrorCode.MUSCLE_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg exercisePostNotFounded(ExercisePostNotFoundedException e) 
	 */
	
	@Test
	public void exercisePostNotFoundedTest() {
		//Given
		String errorMsg = "muscle not founded";
		ExercisePostNotFoundedException exception = new ExercisePostNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.exercisePostNotFounded(exception);
		
		//Then
		assertEquals(ExercisePostErrorCode.EXERCISE_POST_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg targetTypeNotFounded(TargetTypeNotFoundedException e)
	 */
	
	@Test
	public void targetTypeNotFoundedTest() {
		//Given
		String errorMsg = "muscle not founded";
		TargetTypeNotFoundedException exception = new TargetTypeNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.targetTypeNotFounded(exception);
		
		//Then
		assertEquals(TargetTypeErrorCode.TARGET_TYPE_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg likeNotFounded(LikeNotFoundedException e)
	 */
	
	@Test
	public void likeNotFoundedTest() {
		//Given
		String errorMsg = "like not founded";
		LikeNotFoundedException exception = new LikeNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.likeNotFounded(exception);
		
		//Then
		assertEquals(LikeErrorCode.LIKE_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg commentNotFounded(CommentNotFoundedException e)
	 */
	
	@Test
	public void commentNotFoundedTest() {
		//Given
		String errorMsg = "comment not founded";
		CommentNotFoundedException exception = new CommentNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.commentNotFounded(exception);
		
		//Then
		assertEquals(CommentErrorCode.COMMENT_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg feedbackNotFounded(FeedbackNotFoundedException e)
	 */
	
	@Test
	public void feedbackNotFoundedTest() {
		//Given
		String errorMsg = "feedback not founded";
		FeedbackNotFoundedException exception = new FeedbackNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.feedbackNotFounded(exception);
		
		//Then
		assertEquals(FeedbackErrorCode.FEEDBACK_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg privacyPolicyNotFounded(PrivacyPolicyNotFoundedException e)
	 */
	
	@Test
	public void privacyPolicyNotFoundedTest() {
		//Given
		String errorMsg = "privacy policy not founded";
		PrivacyPolicyNotFoundedException exception = new PrivacyPolicyNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.privacyPolicyNotFounded(exception);
		
		//Then
		assertEquals(PrivacyPolicyErrorCode.PRIVACY_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg roleNotFounded(RoleNotFoundedException e)
	 */
	
	@Test
	public void roleNotFoundedTest() {
		//Given
		String errorMsg = "role not founded";
		RoleNotFoundedException exception = new RoleNotFoundedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.roleNotFounded(exception);
		
		//Then
		assertEquals(UserErrorCode.ROLE_NOT_FOUNDED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg searchCriteriaInvalid(SearchCriteriaInvalidException e)
	 */
	
	@Test
	public void searchCriteriaInvalidTest() {
		//Given
		String errorMsg = "search criteria is invalid";
		SearchCriteriaInvalidException exception = new SearchCriteriaInvalidException(errorMsg);
		
		//When
		ErrorMsg result = errorController.searchCriteriaInvalid(exception);
		
		//Then
		assertEquals(SearchCriteriaErrorCode.ORDER_BY_CRITERIA_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public ErrorMsg accessDenied(AccessDeniedException e)
	 */
	
	@Test
	public void accessDeniedTest() {
		//Given
		String errorMsg = "access denied";
		AccessDeniedException exception = new AccessDeniedException(errorMsg);
		
		//When
		ErrorMsg result = errorController.accessDenied(exception);
		
		//Then
		assertEquals(UserErrorCode.ACESS_DENIED_ERROR.getCode(), result.getErrorCode());
		assertEquals(errorMsg, result.getDescription());
	}
	
	/*
	 * public void sessionAttributesNotFounded(SessionAttributesNotFoundedException e, HttpServletResponse response) throws IOException 
	 */
	
	@Test
	public void sessionAttributesNotFoundedTest() throws IOException{
		//Given
		String errorMsg = "session attributes not founded";
		SessionAttributesNotFoundedException exception = new SessionAttributesNotFoundedException(errorMsg);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		//When
		errorController.sessionAttributesNotFounded(exception, response);
		
		//Then
		then(response).should(times(1)).sendRedirect("/login");
	}
}
