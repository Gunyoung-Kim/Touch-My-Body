package com.gunyoung.tmb.services.domain.like;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;

public interface CommentLikeService {
	
	/**
	 * ID로 CommentLike 찾기
	 * @param id 찾으려는 commentLike의 id
	 * @return CommentLike, Null(해당 id의 commentLike 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public CommentLike findById(Long id);
	
	/**
	 * User Id, Comment Id 로 CommentLike 찾기
	 * @param userId 찾으려는 CommenLike의 User ID
	 * @param commentId 찾으려는 CommentLike의 Comment ID
	 * @return CommentLike, Null (해당 조건을 만족하는 CommentLike 없을때)
	 * @since 11
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
	 * CommentLike 생성 및 User, Comment 와 연관 관계 추가 후 저장
	 * @param user CommentLike 추가하려는 User
	 * @param comment CommentLike가 추가되는 Comment
	 * @return 새로 저장된 CommentLike
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
	 * User Id, Comment Id로 CommentLike 존재하는 여부 반환 <br>
	 * 캐쉬 사용
	 * @param userId 찾으려는 CommenLike의 User ID
	 * @param commentId 찾으려는 CommentLike의 Comment ID
	 * @author kimgun-yeong
	 */
	public boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}
