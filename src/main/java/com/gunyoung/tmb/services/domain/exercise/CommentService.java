package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;

public interface CommentService {
	
	/**
	 * ID로 Comment 찾기
	 * @param id 찾으려는 Comment의 id
	 * @return Comment, Null(해당 id의 Comment가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public Comment findById(Long id);
	
	/**
	 * ID로 User와 ExercisePost 페치조인 후 Comment 반환
	 * @param id 찾으려는 Comment의 id
	 * @return Comment, Null(해당 id의 Comment가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public Comment findWithUserAndExercisePostById(Long id);
	
	/**
	 * ID로 CommentLikes 페치조인 후 Comment 반환
	 * @param id 찾으려는 Comment의 id
	 * @return Comment, Null(해당 id의 Comment가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public Comment findWithCommentLikesById(Long id);
	
	/**
	 * ExercisePost ID를 만족하는 Comment들 반환
	 * @param 찾으려는 Comment 들의 ExercisePost ID
	 * @author kimgun-yeong
	 */
	public List<Comment> findAllByExercisePostId(Long postId);
	
	/**
	 * User ID를 만족하는 Comment들 생성 오래된순으로 페이지 반환
	 * @param userId Comment들 작성자의 ID
	 * @author kimgun-yeong
	 */
	public Page<Comment> findAllByUserIdOrderByCreatedAtAsc(Long userId,Integer pageNum, int pageSize);
	
	/**
	 * User ID를 만족하는 Comment들 생성 최신순으로 페이지 반환
	 * @param userId Comment들 작성자의 ID
	 * @author kimgun-yeong
	 */
	public Page<Comment> findAllByUserIdOrderByCreatedAtDesc(Long userId,Integer pageNum, int pageSize);
	
	/**
	 * Comment 생성 및 수정
	 * @param comment 저장하려는 Comment
	 * @return 저장된 Comment 
	 * @author kimgun-yeong
	 */
	public Comment save(Comment comment);
	
	/**
	 * User, ExercisePost 와 연관관계 생성 후 저장 
	 * @param user 댓글을 추가한 User
	 * @param exercisePost 댓글이 추가된 ExercisePost
	 * @author kimgun-yeong
	 */
	public Comment saveWithUserAndExercisePost(Comment comment,User user, ExercisePost exercisePost);
	
	/**
	 * Comment 삭제
	 * @param comment 삭제하려는 Comment
	 * @author kimgun-yeong
	 */
	public void delete(Comment comment);
	
	/**
	 * ID를 만족하는 Comment 삭제 
	 * @param id 삭제하려는 Comment의 ID
	 * @author kimgun-yeong
	 */
	public void deleteById(Long id);
	
	/**
	 * User ID로 만족하는 Comment들 일괄 삭제
	 * @param userId 삭제하려는 Comment들의 User ID
	 * @author kimgun-yeong
	 */
	public void deleteAllByUserId(Long userId);
	
	/**
	 * 해당 Comment ID의 Comment 작성자의 ID가 userID와 일치하면 삭제
	 * @param userId Comment 작성자의 ID와 비교할 User ID
	 * @param commentId 삭제하려는 Comment의 ID
	 * @author kimgun-yeong
	 */
	public void checkIsMineAndDelete(Long userId, Long commentId);
	
	/**
	 * User ID 만족하는 Comment들 개수 반환
	 * @param Comment들 작성자의 ID
	 * @author kimgun-yeong
	 */
	public long countByUserId(Long userId);
	
	/**
	 * 해당 exercisePost id 를 만족하는 Comment 객체들을 {@link CommentForPostViewDTO}로 변환해서 반환하는 메소드 
	 * @author kimgun-yeong
	 */
	public List<CommentForPostViewDTO> getCommentForPostViewDTOsByExercisePostId(Long postId);
}
