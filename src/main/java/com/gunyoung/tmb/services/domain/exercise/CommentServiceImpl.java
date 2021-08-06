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

	/**
	 * ID로 Comment 찾기
	 * @param id 찾으려는 Comment의 id
	 * @return Comment, Null(해당 id의 Comment가 없을때)
	 * @since 11
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
	 * ID로 User와 ExercisePost 페치조인 후 Comment 반환
	 * @param id 찾으려는 Comment의 id
	 * @return Comment, Null(해당 id의 Comment가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Comment findWithUserAndExercisePostById(Long id) {
		Optional<Comment> result = commentRepository.findWithUserAndExercisePostById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * ID로 CommentLikes 페치조인 후 Comment 반환
	 * @param id 찾으려는 Comment의 id
	 * @return Comment, Null(해당 id의 Comment가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Comment findWithCommentLikesById(Long id) {
		Optional<Comment> result = commentRepository.findWithCommentLikesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * ExercisePost ID를 만족하는 Comment들 반환
	 * @param 찾으려는 Comment 들의 ExercisePost ID
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Comment> findAllByExercisePostId(Long postId) {
		return commentRepository.findAllByExercisePostIdCustom(postId);
	}
	
	/**
	 * User ID를 만족하는 Comment들 생성 오래된순으로 페이지 반환
	 * @param userId Comment들 작성자의 ID
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly= true)
	public Page<Comment> findAllByUserIdOrderByCreatedAtASC(Long userId,Integer pageNum, int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, page_size);
		return commentRepository.findAllByUserIdOrderByCreatedAtASCCustom(userId,pageRequest);
	}
	
	/**
	 * User ID를 만족하는 Comment들 생성 최신순으로 페이지 반환
	 * @param userId Comment들 작성자의 ID
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<Comment> findAllByUserIdOrderByCreatedAtDESC(Long userId,Integer pageNum, int page_size) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, page_size);
		return commentRepository.findAllByUserIdOrderByCreatedAtDescCustom(userId,pageRequest);
	}

	/**
	 * Comment 생성 및 수정
	 * @param comment 저장하려는 Comment
	 * @return 저장된 Comment 
	 * @author kimgun-yeong
	 */
	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}
	
	/**
	 * User, ExercisePost 와 연관관계 생성 후 저장 
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
	 * Comment 삭제
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
	
	/**
	 * ID를 만족하는 Comment 삭제 
	 * @param id 삭제하려는 Comment의 ID
	 * @author kimgun-yeong
	 */
	@Override
	public void deleteById(Long id) {
		Comment comment = findById(id);
		if(comment == null) 
			return ;
		delete(comment);
	}
	
	/**
	 * 해당 Comment ID의 Comment 작성자의 ID가 userID와 일치하면 삭제
	 * @param userId Comment 작성자의 ID와 비교할 User ID
	 * @param commentId 삭제하려는 Comment의 ID
	 * @author kimgun-yeong
	 */
	@Override
	public void checkIsMineAndDelete(Long userId, Long commentId) {
		Optional<Comment> comment = commentRepository.findByUserIdAndCommentId(userId, commentId);
		comment.ifPresent((c) -> {
			delete(c);
		});
	}
	
	/**
	 * User ID 만족하는 Comment들 개수 반환
	 * @param Comment들 작성자의 ID
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countByUserId(Long userId) {
		return commentRepository.countByUserId(userId);
	}

	/**
	 * 해당 exercisePost id 를 만족하는 Comment 객체들을 {@link CommentForPostViewDTO}로 변환해서 반환하는 메소드 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public List<CommentForPostViewDTO> getCommentForPostViewDTOsByExercisePostId(Long postId) {
		return commentRepository.findForCommentForPostViewDTOByExercisePostId(postId);
	}


}
