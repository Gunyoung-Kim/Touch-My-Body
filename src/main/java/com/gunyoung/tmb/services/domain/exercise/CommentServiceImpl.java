package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;
import com.gunyoung.tmb.error.codes.CommentErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.CommentNotFoundedException;
import com.gunyoung.tmb.precondition.Preconditions;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.services.domain.like.CommentLikeService;

import lombok.RequiredArgsConstructor;

/**
 * CommentService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("commentService")
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	
	private final CommentRepository commentRepository;
	
	private final CommentLikeService commentLikeService;

	@Override
	@Transactional(readOnly=true)
	public Comment findById(Long id) {
		Optional<Comment> result = commentRepository.findById(id);
		return result.orElseThrow(() -> new CommentNotFoundedException(CommentErrorCode.COMMENT_NOT_FOUNDED_ERROR.getDescription()));
	}
	
	@Override
	@Transactional(readOnly=true)
	public Comment findWithUserAndExercisePostById(Long id) {
		Optional<Comment> result = commentRepository.findWithUserAndExercisePostById(id);
		return result.orElseThrow(() -> new CommentNotFoundedException(CommentErrorCode.COMMENT_NOT_FOUNDED_ERROR.getDescription()));
	}
	
	@Override
	@Transactional(readOnly=true)
	public Comment findWithCommentLikesById(Long id) {
		Optional<Comment> result = commentRepository.findWithCommentLikesById(id);
		return result.orElseThrow(() -> new CommentNotFoundedException(CommentErrorCode.COMMENT_NOT_FOUNDED_ERROR.getDescription()));
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Comment> findAllByExercisePostId(Long postId) {
		return commentRepository.findAllByExercisePostIdInQuery(postId);
	}
	
	@Override
	@Transactional(readOnly= true)
	public Page<Comment> findAllByUserIdOrderByCreatedAtAsc(Long userId, Integer pageNum, int pageSize) {
		checkParameterForPageRequest(pageNum, pageSize);
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		return commentRepository.findAllByUserIdOrderByCreatedAtAscInPage(userId,pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Comment> findAllByUserIdOrderByCreatedAtDesc(Long userId, Integer pageNum, int pageSize) {
		checkParameterForPageRequest(pageNum, pageSize);
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		return commentRepository.findAllByUserIdOrderByCreatedAtDescInPage(userId,pageRequest);
	}
	
	private void checkParameterForPageRequest(Integer pageNumber, int pageSize) {
		Preconditions.notLessThan(pageNumber, 1, "pageNumber should be equal or greater than 1");
		Preconditions.notLessThanInt(pageSize, 1, "pageSize should be equal or greater than 1");
	}

	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}
	
	@Override
	public Comment saveWithUserAndExercisePost(Comment comment, User user, ExercisePost exercisePost) {
		Preconditions.notNull(comment, "Given comment must not be null!");
		Preconditions.notNull(user, "Given user must not be null!");
		Preconditions.notNull(exercisePost, "Given exercisePost must not be null!");
		
		setRelationBetweenCommentAndUser(comment, user);
		setRelationBetweenCommentAndPost(comment, exercisePost);
		
		return save(comment);
	}
	
	private void setRelationBetweenCommentAndUser(Comment comment, User user) {
		comment.setUser(user);
		user.getComments().add(comment);
	}
	
	private void setRelationBetweenCommentAndPost(Comment comment, ExercisePost exercisePost) {
		comment.setExercisePost(exercisePost);
		exercisePost.getComments().add(comment);
	}
	
	@Override
	public void checkIsMineAndDelete(Long userId, Long commentId) {
		Optional<Comment> comment = commentRepository.findByUserIdAndCommentId(userId, commentId);
		comment.ifPresent(this::delete);
	}
	
	@Override
	public void deleteById(Long id) {
		Optional<Comment> targetComment = commentRepository.findById(id);
		targetComment.ifPresent(this::delete);
	}
	
	@Override
	public void delete(Comment comment) {
		Preconditions.notNull(comment, "Given comment must be not null");
		
		deleteAllWeakEntityForComment(comment);
		commentRepository.deleteByIdInQuery(comment.getId());
	}
	
	@Override
	public void deleteAllByUserId(Long userId) {
		List<Comment> comments = commentRepository.findAllByUserIdInQuery(userId);
		for(Comment comment: comments) {
			deleteAllWeakEntityForComment(comment);
		}
		commentRepository.deleteAllByUserIdInQuery(userId);
	}
	
	@Override
	public void deleteAllByExercisePostId(Long exercisePostId) {
		List<Comment> comments = commentRepository.findAllByExercisePostIdInQuery(exercisePostId);
		for(Comment comment: comments) {
			deleteAllWeakEntityForComment(comment);
		}
		commentRepository.deleteAllByExercisePostIdInQuery(exercisePostId);
	}
	
	private void deleteAllWeakEntityForComment(Comment comment) {
		Long commentId = comment.getId();
		commentLikeService.deleteAllByCommentId(commentId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public long countByUserId(Long userId) {
		return commentRepository.countByUserId(userId);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CommentForPostViewDTO> getCommentForPostViewDTOsByExercisePostId(Long postId) {
		return commentRepository.findForCommentForPostViewDTOByExercisePostId(postId);
	}
}
