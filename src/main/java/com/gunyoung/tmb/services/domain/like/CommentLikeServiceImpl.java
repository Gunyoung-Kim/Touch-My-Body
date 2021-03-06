package com.gunyoung.tmb.services.domain.like;

import static com.gunyoung.tmb.utils.CacheConstants.COMMENT_LIKE_NAME;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.error.codes.LikeErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.LikeNotFoundedException;
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
	public CommentLike findByUserIdAndCommentId(Long userId, Long commentId) {
		Optional<CommentLike> result = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);
		return result.orElseThrow(() -> new LikeNotFoundedException(LikeErrorCode.LIKE_NOT_FOUNDED_ERROR.getDescription()));
	}

	@Override
	@CacheEvict(cacheNames=COMMENT_LIKE_NAME, 
	key="{#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAULT_CACHE_KEY, #commentLike.user.id, #commentLike.comment.id}")
	public CommentLike save(CommentLike commentLike) {
		return commentLikeRepository.save(commentLike);
	}

	@Override
	@CacheEvict(cacheNames=COMMENT_LIKE_NAME, 
	key="{#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAULT_CACHE_KEY, #user.id, #comment.id}")
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
	key="{#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAULT_CACHE_KEY, #commentLike.user.id, #commentLike.comment.id}")
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
			key="{#root.target.EXIST_BY_USER_ID_AND_COMMENT_ID_DEFAULT_CACHE_KEY, #userId, #commentId}",
			unless="#result != true")
	public boolean existsByUserIdAndCommentId(Long userId, Long commentId) {
		return commentLikeRepository.existsByUserIdAndCommentId(userId, commentId);
	}
}
