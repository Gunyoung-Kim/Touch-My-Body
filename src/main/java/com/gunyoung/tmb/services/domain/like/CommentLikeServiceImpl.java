package com.gunyoung.tmb.services.domain.like;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.repos.CommentLikeRepository;
import com.gunyoung.tmb.utils.CacheUtil;

import lombok.RequiredArgsConstructor;

/**
 * CommentLikeService 구현하는 클래스
 * @author kimgun-yeong
 *
 */
@Service("commentLikeService")
@Transactional
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

	private final CommentLikeRepository commentLikeRepository;
	
	/**
	 * @param id 찾으려는 commentLike의 id
	 * @return CommentLike, Null(해당 id의 commentLike 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public CommentLike findById(Long id) {
		Optional<CommentLike> result = commentLikeRepository.findById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}
	
	/**
	 * @param userId 찾으려는 CommenLike의 User ID
	 * @param commentId 찾으려는 CommentLike의 Comment ID
	 * @return CommentLike, Null (해당 조건을 만족하는 CommentLike 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public CommentLike findByUserIdAndCommentId(Long userId, Long commentId) {
		Optional<CommentLike> result = commentLikeRepository.findByUserIdAndCommentIdCustom(userId, commentId);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	/**
	 * @param commentLike save할 CommentLike 객체
	 * @return save 된 CommentLike 객체
	 * @author kimgun-yeong
	 */
	@Override
	@CacheEvict(cacheNames=CacheUtil.COMMENT_LIKE_NAME,allEntries=true)
	public CommentLike save(CommentLike commentLike) {
		return commentLikeRepository.save(commentLike);
	}

	/**
	 * @param user CommentLike 추가하려는 User
	 * @param comment CommentLike가 추가되는 Comment
	 * @return 새로 저장된 CommentLike
	 * @author kimgun-yeong
	 */
	@Override
	public CommentLike saveWithUserAndComment(User user, Comment comment) {
		CommentLike commentLike = CommentLike.builder()
				.user(user)
				.comment(comment)
				.build();
		
		user.getCommentLikes().add(commentLike);
		comment.getCommentLikes().add(commentLike);
		
		return save(commentLike);		
	}
	
	/**
	 * @param commentLike delete할 commentLike 객체
	 * @return delete 된 CommentLike 객체
	 * @author kimgun-yeong
	 */
	@Override
	@CacheEvict(cacheNames=CacheUtil.COMMENT_LIKE_NAME,allEntries=true)
	public void delete(CommentLike commentLike) {
		User user = commentLike.getUser();
	
		if(user != null)
			user.getCommentLikes().remove(commentLike);
		
		Comment comment = commentLike.getComment();
		
		if(comment != null)
			comment.getCommentLikes().remove(commentLike);
		
		commentLikeRepository.delete(commentLike);
	}

	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=CacheUtil.COMMENT_LIKE_NAME,
			key="#root.methodName.concat(':').concat('#userId').concat(':').concat('#exercisePostId')",
			unless="#result != true")
	public boolean existsByUserIdAndCommentId(Long userId, Long commentId) {
		return commentLikeRepository.existsByUserIdAndCommentId(userId, commentId);
	}

}
