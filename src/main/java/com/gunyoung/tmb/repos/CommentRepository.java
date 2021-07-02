package com.gunyoung.tmb.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;

public interface CommentRepository extends JpaRepository<Comment,Long> {
	
	@Query("SELECT c FROM Comment c "
			+ "JOIN FETCH c.user u "
			+ "JOIN FETCH c.exercisePost ep "
			+ "WHERE c.id = :commentId")
	public Optional<Comment> findWithUserAndExercisePostById(@Param("commentId") Long id);
	
	@Query("SELECT c FROM Comment c "
			+ "LEFT JOIN FETCH c.commentLikes cl "
			+ "WHERE c.id = :commentId")
	public Optional<Comment> findWithCommentLikesById(@Param("commentId") Long id);
	
	@Query("SELECT new com.gunyoung.tmb.dto.response.CommentForPostViewDTO(c.id, c.writerIp, c.contents, c.isAnonymous, u.nickName, c.createdAt, "
			+ "(SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.id = c.id)) FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "WHERE c.exercisePost.id = :postId")
	public List<CommentForPostViewDTO> findForCommentForPostViewDTOByExercisePostId(@Param("postId")Long postId);
	
	/**
	 * ExercisePost ID로 만족하는 Comment들 가져오는 쿼리 <br>
	 * INNER JOIN을 통한 성능 향상
	 * @param postId
	 * @return
	 */
	@Query("SELECT c FROM Comment c "
			+ "INNER JOIN c.exercisePost ep "
			+ "ON ep.id = :postId "
			+ "ORDER BY c.createdAt ASC")
	public List<Comment> findAllByExercisePostIdCustom(@Param("postId")Long postId);
	
	/** 
	 * User ID로 만족하는 Comment들 가져오는 쿼리 <br>
	 * INNER JOIN을 통한 성능 향상 <br>
	 * Comment 제일 먼저 생성된 순으로 가져옴
	 * @param userId
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "ON u.id = :userId "
			+ "ORDER BY c.createdAt ASC")
	public Page<Comment> findAllByUserIdOrderByCreatedAtASCCustom(@Param("userId") Long userId,Pageable pageable);
	
	/**
	 * User ID로 만족하는 Comment들 가져오는 쿼리 <br>
	 * INNER JOIN을 통한 성능 향상 <br>
	 * Comment 제일 최근에 생성된 순으로 가져옴
	 * @param userId
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "ON u.id = :userId "
			+ "ORDER BY c.createdAt DESC")
	public Page<Comment> findAllByUserIdOrderByCreatedAtDescCustom(@Param("userId") Long userId,Pageable pageable);
	
	
	/**
	 * User ID로 만족하는 Comment들 개수 가져오기 <br>
	 * INNER JOIN을 통한 성능 향상 
	 * @param userId
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(c) FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "ON u.id = :userId ")
	public long countByUserId(@Param("userId") Long userId);
	
}
