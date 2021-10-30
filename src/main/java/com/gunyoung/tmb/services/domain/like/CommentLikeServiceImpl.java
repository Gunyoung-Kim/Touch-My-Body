package com.gunyoung.tmb.services.domain.like;

import static com.gunyoung.tmb.utils.CacheConstants.*;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.precondition.Preconditions;
import com.gunyoung.tmb.repos.CommentLikeRepository;

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
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public CommentLike findByUserIdAndCommentId(Long userId, Long commentId) {
		Optional<CommentLike> result = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);
		return result.orElse(null);
	}

	@Override
	@CacheEvict(cacheNames=COMMENT_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#commentLike.user.id).concat(':').concat(#commentLike.comment.id)")
	public CommentLike save(CommentLike commentLike) {
		return commentLikeRepository.save(commentLike);
	}

	@Override
	@CacheEvict(cacheNames=COMMENT_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#user.id).concat(':').concat(#comment.id)")
	public CommentLike createAndSaveWithUserAndComment(User user, Comment comment) {
		Preconditions.notNull(user, "Given user must be not null");
		Preconditions.notNull(comment, "Given comment must be not null");
		
		CommentLike newCommentLike = CommentLike.of(user, comment);
		user.getCommentLikes().add(newCommentLike);
		comment.getCommentLikes().add(newCommentLike);
		
		return save(newCommentLike);		
	}
	
	@Override
	@CacheEvict(cacheNames=COMMENT_LIKE_NAME, 
	key="#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#commentLike.user.id).concat(':').concat(#commentLike.comment.id)")
	public void delete(CommentLike commentLike) {
		commentLikeRepository.delete(commentLike);
	}
	
	@Override
	public void deleteAllByUserId(Long userId) {
		commentLikeRepository.deleteAllByUserIdInQuery(userId);
	}
	
	@Override
	public void deleteAllByCommentId(Long commentId) {
		commentLikeRepository.deleteAllByCommentIdInQuery(commentId);
	}

	@Override
	@Transactional(readOnly=true)
	@Cacheable(cacheNames=COMMENT_LIKE_NAME,
			key="#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAUALT_CACHE_KEY.concat(':').concat(#userId).concat(':').concat(#commentId)",
			unless="#result != true")
	public boolean existsByUserIdAndCommentId(Long userId, Long commentId) {
		return commentLikeRepository.existsByUserIdAndCommentId(userId, commentId);
	}
}
