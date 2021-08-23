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
import com.gunyoung.tmb.repos.CommentRepository;

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

	@Override
	@Transactional(readOnly=true)
	public Comment findById(Long id) {
		Optional<Comment> result = commentRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Comment findWithUserAndExercisePostById(Long id) {
		Optional<Comment> result = commentRepository.findWithUserAndExercisePostById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Comment findWithCommentLikesById(Long id) {
		Optional<Comment> result = commentRepository.findWithCommentLikesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Comment> findAllByExercisePostId(Long postId) {
		return commentRepository.findAllByExercisePostIdCustom(postId);
	}
	
	@Override
	@Transactional(readOnly= true)
	public Page<Comment> findAllByUserIdOrderByCreatedAtAsc(Long userId,Integer pageNum, int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, page_size);
		return commentRepository.findAllByUserIdOrderByCreatedAtASCCustom(userId,pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Comment> findAllByUserIdOrderByCreatedAtDesc(Long userId,Integer pageNum, int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, page_size);
		return commentRepository.findAllByUserIdOrderByCreatedAtDescCustom(userId,pageRequest);
	}

	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}
	
	@Override
	public Comment saveWithUserAndExercisePost(Comment comment, User user, ExercisePost exercisePost) {
		comment.setUser(user);
		comment.setExercisePost(exercisePost);
		user.getComments().add(comment);
		exercisePost.getComments().add(comment);
		
		return save(comment);
	}

	@Override
	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}
	
	@Override
	public void deleteById(Long id) {
		Comment comment = findById(id);
		if(comment == null) 
			return ;
		delete(comment);
	}
	
	@Override
	public void checkIsMineAndDelete(Long userId, Long commentId) {
		Optional<Comment> comment = commentRepository.findByUserIdAndCommentId(userId, commentId);
		comment.ifPresent((c) -> {
			delete(c);
		});
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
