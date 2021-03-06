package com.gunyoung.tmb.services.domain.like;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;

public interface CommentLikeService {
	
	public static final String EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAULT_CACHE_KEY = "existByUserIdAndCommentId";
	
	/**
	 * User Id, Comment Id 로 CommentLike 찾기
	 * @param userId 찾으려는 CommenLike의 User ID
	 * @param commentId 찾으려는 CommentLike의 Comment ID
	 * @return CommentLike
	 * @throws LikeNotFoundedException 해당 조건을 만족하는 CommentLike 없을때 
	 * @author kimgun-yeong
	 */
	public CommentLike findByUserIdAndCommentId(Long userId, Long commentId);
	
	/**
	 * CommentLike 생성 및 수정 <br>
	 * {@code CacheUtil.COMMENT_LIKE_NAME} 관련 캐쉬 삭제
	 * @param commentLike save할 CommentLike 객체
	 * @return save 된 CommentLike 객체
	 * @author kimgun-yeong
	 */
	public CommentLike save(CommentLike commentLike);
	
	/**
	 * CommentLike 생성 및 User, Comment 와 연관 관계 추가 후 저장 <br>
	 * {@code CacheUtil.COMMENT_LIKE_NAME} 관련 캐쉬 삭제
	 * @param user CommentLike 추가하려는 User
	 * @param comment CommentLike가 추가되는 Comment
	 * @return 새로 저장된 CommentLike
	 * @throws PreconditionViolationException user == null || comment == null
	 * @author kimgun-yeong
	 */
	public CommentLike createAndSaveWithUserAndComment(User user, Comment comment);
	
	/**
	 * CommentLike 삭제 <br>
	 * {@code CacheUtil.COMMENT_LIKE_NAME} 관련 캐쉬 삭제
	 * @param commentLike delete할 commentLike 객체
	 * @author kimgun-yeong
	 */
	public void delete(CommentLike commentLike);
	
	/**
	 * User Id를 만족하는 CommentLike들 일괄 삭제
	 * @param userId 삭제하려는 CommenLike들의 User ID
	 * @author kimgun-yeong
	 */
	public void deleteAllByUserId(Long userId);
	
	/**
	 * Comment Id를 만족하는 CommentLike들 일괄 삭제
	 * @param commentId 삭제하려는 CommentLike들의 Comment ID
	 * @author kimgun-yeong
	 */
	public void deleteAllByCommentId(Long commentId);
	
	/**
	 * User Id, Comment Id로 CommentLike 존재하는 여부 반환 <br>
	 * 캐쉬 사용, 결과값이 true인 경우에
	 * @param userId 찾으려는 CommenLike의 User ID
	 * @param commentId 찾으려는 CommentLike의 Comment ID
	 * @author kimgun-yeong
	 */
	public boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}
