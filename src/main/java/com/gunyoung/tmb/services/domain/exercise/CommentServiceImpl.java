package com.gunyoung.tmb.services.domain.exercise;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.services.domain.user.UserService;

/**
 * CommentService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ExercisePostService exercisePostService;

	/**
	 * @param id 찾으려는 Comment의 id
	 * @return Comment, Null(해당 id의 Comment가 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Comment findById(Long id) {
		Optional<Comment> result = commentRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}

	/**
	 * @param comment 저장하려는 Comment
	 * @return 저장된 Comment 
	 * @author kimgun-yeong
	 */
	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}
	
	/**
	 * @param user 댓글을 추가한 User
	 * @param exercisePost 댓글이 추가된 ExercisePost
	 * @author kimgun-yeong
	 */
	@Override
	public Comment saveWithUserAndExercisePost(Comment comment, User user, ExercisePost exercisePost) {
		comment.setUser(user);
		comment.setExercisePost(exercisePost);
		user.getComments().add(comment);
		exercisePost.getComments().add(comment);
		
		return save(comment);
	}

	/**
	 * @param comment 삭제하려는 Comment
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(Comment comment) {
		User user = comment.getUser();
		
		if(user != null) {
			user.getComments().remove(comment);
		}
		
		ExercisePost exercisePost = comment.getExercisePost();
		
		if(exercisePost != null) {
			exercisePost.getComments().remove(comment);
		}
		
		commentRepository.delete(comment);
	}

	

}
