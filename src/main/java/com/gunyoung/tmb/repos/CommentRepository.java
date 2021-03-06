package com.gunyoung.tmb.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;

public interface CommentRepository extends JpaRepository<Comment,Long> {
	
	/**
	 * User ID, Comment ID 로 Comment 찾기 
	 * @param userId 찾으려는 Comment 작성 User ID
	 * @param commentId 찾으려는 Comment ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "WHERE (c.id = :commentId) "
			+ "AND (u.id = :userId)")
	public Optional<Comment> findByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);
	
	/**
	 * ID로 Comment 찾기 <br>
	 * User, ExercisePost INNER 페치 조인 
	 * @param id 찾으려는 Comment의 ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Comment c "
			+ "JOIN FETCH c.user u "
			+ "JOIN FETCH c.exercisePost ep "
			+ "WHERE c.id = :commentId")
	public Optional<Comment> findWithUserAndExercisePostById(@Param("commentId") Long id);
	
	/**
	 * ID로 Comment 찾기 <br>
	 * CommentLike LEFT 페치 조인
	 * @param id 찾으려는 Comment의 ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Comment c "
			+ "LEFT JOIN FETCH c.commentLikes cl "
			+ "WHERE c.id = :commentId")
	public Optional<Comment> findWithCommentLikesById(@Param("commentId") Long id);
	
	/**
	 * ExercisePostID로 {@link CommentForPostViewDTO} 필드들 Select 후 객체에 매핑
	 * @param postId 찾으려는 Comment들이 작성된 ExercisePost의 ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.CommentForPostViewDTO(c.id, c.writerIp, c.contents, c.isAnonymous, u.nickName, c.createdAt, "
			+ "(SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.id = c.id)) FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "WHERE c.exercisePost.id = :postId")
	public List<CommentForPostViewDTO> findForCommentForPostViewDTOByExercisePostId(@Param("postId") Long postId);
	
	/**
	 * User ID로 해당하는 Comment들 반환
	 * @param userId 찾으려는 Comment들의 User ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "ON u.id = :userId")
	public List<Comment> findAllByUserIdInQuery(@Param("userId") Long userId);
	
	/**
	 * ExercisePost ID로 만족하는 Comment들 가져오는 쿼리 <br>
	 * Comment 생성된지 오래된순으로 정렬 
	 * @param postId 찾으려는 Comment들이 작성된 ExercisePost의 ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Comment c "
			+ "INNER JOIN c.exercisePost ep "
			+ "ON ep.id = :postId "
			+ "ORDER BY c.createdAt ASC")
	public List<Comment> findAllByExercisePostIdInQuery(@Param("postId") Long postId);
	
	/** 
	 * User ID로 만족하는 Comment들 가져오는 쿼리 <br>
	 * Comment 제일 먼저 생성된 순으로 정렬 <br>
	 * 페이징 처리
	 * @param userId 찾으려는 Comment들을 작성한 User의 ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "ON u.id = :userId "
			+ "ORDER BY c.createdAt ASC")
	public Page<Comment> findAllByUserIdOrderByCreatedAtAscInPage(@Param("userId") Long userId, Pageable pageable);
	
	/**
	 * User ID로 만족하는 Comment들 가져오는 쿼리 <br>
	 * Comment 제일 최근에 생성된 순으로 정렬 <br>
	 * 페이징 처리
	 * @param userId 찾으려는 Comment들을 작성한 User의 ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "ON u.id = :userId "
			+ "ORDER BY c.createdAt DESC")
	public Page<Comment> findAllByUserIdOrderByCreatedAtDescInPage(@Param("userId") Long userId, Pageable pageable);
	
	/**
	 * User ID로 만족하는 Comment들 개수 가져오기 
	 * @param userId 찾으려는 Comment들을 작성한 User의 ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(c) FROM Comment c "
			+ "INNER JOIN c.user u "
			+ "ON u.id = :userId ")
	public long countByUserId(@Param("userId") Long userId);
	
	/**
	 * ID로 Comment 삭제
	 * @param commentId 삭제하려는 Comment ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM Comment c "
			+ "WHERE c.id = :commentId")
	public void deleteByIdInQuery(@Param("commentId") Long commentId);
	
	/**
	 * User ID로 만족하는 Comment들 일괄 삭제
	 * @param userId 삭제하려는 Comment들의 User ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM Comment c "
			+ "WHERE c.user.id = :userId")
	public void deleteAllByUserIdInQuery(@Param("userId") Long userId);
	
	/**
	 * ExercisePost ID로 만족하는 Comment들 일괄 삭제
	 * @param exercisePostId 삭제하려는 Comment들의 ExercisePost ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM Comment c "
			+ "WHERE c.exercisePost.id = :exercisePostId")
	public void deleteAllByExercisePostIdInQuery(@Param("exercisePostId") Long exercisePostId);
}
