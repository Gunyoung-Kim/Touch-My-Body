package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;

public interface ExercisePostRepository extends JpaRepository<ExercisePost,Long>{
	
	@Query("SELECT ep FROM ExercisePost ep "
			+ "LEFT JOIN FETCH ep.postLikes pl "
			+ "WHERE ep.id = :exercisePostId")
	public Optional<ExercisePost> findWithPostLikesById(@Param("exercisePostId") Long id); 
	
	@Query("SELECT ep FROM ExercisePost ep "
			+ "LEFT JOIN FETCH ep.comments c "
			+ "WHERE ep.id = :exercisePostId")
	public Optional<ExercisePost> findWithCommentsById(@Param("exercisePostId") Long id);
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.ExercisePostViewDTO( ep.id, ep.title, e.name, u.nickName, ep.contents, ep.viewNum, "
			+ " (SELECT COUNT(pl) FROM PostLike pl WHERE pl.exercisePost.id = :exercisePostId), "
			+ " (SELECT COUNT(c) FROM Comment c WHERE c.exercisePost.id = :exercisePostId), "
			+ " ep.createdAt) FROM ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE ep.id = :exercisePostId")
	public Optional<ExercisePostViewDTO> findForExercisePostViewDTOById(@Param("exercisePostId") Long id);
	
	/**
	 * User ID로 만족하는 ExercisePost들 가져오는 쿼리 <br>
	 * INNER JOIN을 통한 성능 향상 <br>
	 * ExercisePost 제일 먼저 생성된 순으로 가져옴
	 * @param userId
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT ep FROM ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "ON u.id = :userId "
			+ "ORDER BY ep.createdAt ASC")
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtASCCustom(@Param("userId") Long userId,Pageable pageable);
	
	/**
	 * User ID로 만족하는 ExercisePost들 가져오는 쿼리 <br>
	 * INNER JOIN을 통한 성능 향상 <br>
	 * ExercisePost 제일 최근에 생성된 순으로 가져옴
	 * @param userId
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT ep FROM ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "ON u.id = :userId "
			+ "ORDER BY ep.createdAt DESC")
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtDescCustom(@Param("userId") Long userId,Pageable pageable);
	
	/**
	 * ExercisePost의 필드들로 PostForCommunityViewDTO들로 바인딩하여 가져오는 메소드, 페이징 처리 <br>
	 * INNER JOIN 활용 
	 * @param pageable
	 * @return
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, e.name, ep.createdAt, ep.viewNum) from ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "ORDER BY ep.createdAt DESC")
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Pageable pageable);
	
	/**
	 * ExercisePost의 필드들로 PostForCommunityViewDTO들로 바인딩하여 가져오는 메소드, 페이징 처리 <br>
	 * 키워드 검색은 제목, 내용에 적용 <br>
	 * INNER JOIN 활용 
	 * @param keyword
	 * @param pageable
	 * @return
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, e.name, ep.createdAt, ep.viewNum) from ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE (ep.title LIKE %:keyword%) "
			+ "OR (ep.contents LIKE %:keyword%) "
			+ "ORDER BY ep.createdAt DESC")
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(@Param("keyword") String keyword, Pageable pageable);
	
	
	/**
	 * ExercisePost의 필드들로 PostForCommunityViewDTO들로 바인딩하여 가져오는 메소드, 페이징 처리 <br>
	 * 특정 TargetType 만족하는 것들만 가져온다 <br>
	 * INNER JOIN 활용
	 * @param target
	 * @param pageable
	 * @return
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, e.name, ep.createdAt, ep.viewNum) from ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE e.target = :target "
			+ "ORDER BY ep.createdAt DESC")
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(@Param("target") TargetType target, Pageable pageable);
	
	/**
	 * ExercisePost의 필드들로 PostForCommunityViewDTO들로 바인딩하여 가져오는 메소드, 페이징 처리 <br>
	 * 특정 TargetType 만족하는 것들만 가져온다 <br>
	 * 키워드 검색은 제목, 내용에 적용 <br>
	 * INNER JOIN 활용 
	 * @param target
	 * @param keyword
	 * @param pageable
	 * @return
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, e.name, ep.createdAt, ep.viewNum) from ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE (e.target = :target) AND "
			+ "((ep.title LIKE %:keyword%) OR (ep.contents LIKE %:keyword%)) "
			+ "ORDER BY ep.createdAt DESC")
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(@Param("target") TargetType target,@Param("keyword")String keyword ,Pageable pageable);
	
	/**
	 * 해당 User ID를 만족하는 ExercisePost 개수 반환
	 * @param userId
	 * @return
	 */
	@Query("SELECT COUNT(ep) FROM ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "ON u.id = :userId")
	public long countWithUserId(@Param("userId") Long userId);
	
	/**
	 * 게시글 내용이나 제목에서 키워드를 포함하는 게시글들의 개수 반환
	 * @param keyword
	 * @return
	 */
	@Query("SELECT COUNT(ep) FROM ExercisePost ep "
			+ "WHERE (ep.title LIKE %:keyword%) "
			+ "OR (ep.contents LIKE %:keyword%)")
	public long countWithTitleAndContentsKeyword(@Param("keyword") String keyword);
	
	/**
	 * 해당 target을 만족하는 것들만의 개수 반환
	 * @param target
	 * @return
	 */
	@Query("SELECT COUNT(ep) FROM ExercisePost ep "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE e.target = :target")
	public long countWithTarget(@Param("target") TargetType target);
	
	/**
	 * 해당 target을 만족하고 제목과 내용에 키워드를 포함하는것들만의 개수 반환
	 * @param target
	 * @param keyword
	 * @return
	 */
	@Query("SELECT COUNT(ep) FROM ExercisePost ep "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE (e.target = :target) AND "
			+ "((ep.title LIKE %:keyword%) OR (ep.contents LIKE %:keyword%))")
	public long countWithTargetAndKeyword(@Param("target") TargetType target, @Param("keyword") String keyword);
}
