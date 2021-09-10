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
	
	@Override
	@Transactional(readOnly=true)
	public CommentLike findById(Long id) {
		Optional<CommentLike> result = commentLikeRepository.findById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public CommentLike findByUserIdAndCommentId(Long userId, Long commentId) {
		Optional<CommentLike> result = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	@Override
	@CacheEvict(cacheNames=CacheUtil.COMMENT_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#commentLike.user.id).concat(':').concat(#commentLike.comment.id)")
	public CommentLike save(CommentLike commentLike) {
		return commentLikeRepository.save(commentLike);
	}

	@Override
	@CacheEvict(cacheNames=CacheUtil.COMMENT_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#user.id).concat(':').concat(#comment.id)")
	public CommentLike createAndSaveWithUserAndComment(User user, Comment comment) {
		CommentLike commentLike = CommentLike.builder()
				.user(user)
				.comment(comment)
				.build();
		
		user.getCommentLikes().add(commentLike);
		comment.getCommentLikes().add(commentLike);
		
		return save(commentLike);		
	}
	
	@Override
	@CacheEvict(cacheNames=CacheUtil.COMMENT_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#commentLike.user.id).concat(':').concat(#commentLike.comment.id)")
	public void delete(CommentLike commentLike) {
		commentLikeRepository.delete(commentLike);
	}

	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=CacheUtil.COMMENT_LIKE_NAME,
			key="#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#userId).concat(':').concat(#commentId)",
			unless="#result != true")
	public boolean existsByUserIdAndCommentId(Long userId, Long commentId) {
		return commentLikeRepository.existsByUserIdAndCommentId(userId, commentId);
	}

}
