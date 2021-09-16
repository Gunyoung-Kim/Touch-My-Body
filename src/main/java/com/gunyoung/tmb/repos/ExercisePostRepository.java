package com.gunyoung.tmb.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;

public interface ExercisePostRepository extends JpaRepository<ExercisePost,Long>{
	
	/**
	 * User ID와 ExercisePost ID로 ExercisePost 찾기
	 * @param userId 찾으려는 ExercisePost의 User ID
	 * @param exercisePostId 찾으려는 ExercisePost의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT ep FROM ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "WHERE (ep.id = :exercisePostId) "
			+ "AND (u.id = :userId)")
	public Optional<ExercisePost> findByUserIdAndExercisePostId(@Param("userId") Long userId, @Param("exercisePostId") Long exercisePostId);
	
	/**
	 * ID로 ExercisePost 찾기 <br>
	 * PostLikes Left 페치 조인
	 * @param id 찾으려는 ExercisePost의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT ep FROM ExercisePost ep "
			+ "LEFT JOIN FETCH ep.postLikes pl "
			+ "WHERE ep.id = :exercisePostId")
	public Optional<ExercisePost> findWithPostLikesById(@Param("exercisePostId") Long id); 
	
	/**
	 * ID로 ExercisePost 찾기 <br>
	 * Comments Left 페치 조인
	 * @param id 찾으려는 ExercisePost의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT ep FROM ExercisePost ep "
			+ "LEFT JOIN FETCH ep.comments c "
			+ "WHERE ep.id = :exercisePostId")
	public Optional<ExercisePost> findWithCommentsById(@Param("exercisePostId") Long id);
	
	/**
	 * User ID를 만족하는 ExercisePost들 반환
	 * @param userId 찾으려는 ExercisePost들의 User ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT ep FROM ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "ON u.id = :userId")
	public List<ExercisePost> findAllByUserIdInQuery(@Param("userId") Long userId);
	
	/**
	 * Exercise ID를 만족하는 ExercisePost들 반환
	 * @param exerciseId 찾으려는 ExercisePost들의 Exercise ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT ep FROM ExercisePost ep "
			+ "INNER JOIN ep.exercise e "
			+ "ON e.id = :exerciseId")
	public List<ExercisePost> findAllByExerciseIdInQuery(@Param("exerciseId") Long exerciseId);
	
	/**
	 * ID로 {@link ExercisePostViewDTO} 필드들 Select후 매핑
	 * @param id 찾으려는 ExercisePost의 ID
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
	 * ExercisePost 제일 먼저 생성된 순으로 정렬 <br>
	 * 페이징 처리
	 * @param userId ExercisePost 작성한 User의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT ep FROM ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "ON u.id = :userId "
			+ "ORDER BY ep.createdAt ASC")
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtAscInPage(@Param("userId") Long userId, Pageable pageable);
	
	/**
	 * User ID로 만족하는 ExercisePost들 가져오는 쿼리 <br>
	 * ExercisePost 제일 최근에 생성된 순으로 정렬 <br>
	 * 페이징 처리
	 * @param userId ExercisePost 작성한 User의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT ep FROM ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "ON u.id = :userId "
			+ "ORDER BY ep.createdAt DESC")
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtDescInPage(@Param("userId") Long userId, Pageable pageable);
	
	/**
	 * ExercisePost의 필드들로 {@link PostForCommunityViewDTO} 들로 바인딩하여 가져오는 쿼리 <br>
	 * 페이징 처리 
	 * @param pageable
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, e.name, ep.createdAt, ep.viewNum) from ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "ORDER BY ep.createdAt DESC")
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOOrderByCreatedAtDescInPage(Pageable pageable);
	
	/**
	 * ExercisePost의 필드들로 {@link PostForCommunityViewDTO} 들로 바인딩하여 가져오는 쿼리 <br>
	 * 페이징 처리 <br>
	 * 키워드 검색은 제목, 내용에 적용
	 * @param keyword 검색하려는 ExercisePost의 제목 또는 내용
	 * @param pageable
	 * @return
	 * @author kimgun-yeong 
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, e.name, ep.createdAt, ep.viewNum) from ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE (ep.title LIKE %:keyword%) "
			+ "OR (ep.contents LIKE %:keyword%) "
			+ "OR (u.nickName LIKE %:keyword%) "
			+ "ORDER BY ep.createdAt DESC")
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordInPage(@Param("keyword") String keyword, Pageable pageable);
	
	
	/**
	 * ExercisePost의 필드들로 {@link PostForCommunityViewDTO} 들로 바인딩하여 가져오는 메소드<br>
	 * 페이징 처리 
	 * @param targetExerciePost와 연관된 Exercise의 target
	 * @param pageable
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, e.name, ep.createdAt, ep.viewNum) from ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE e.target = :target "
			+ "ORDER BY ep.createdAt DESC")
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetInPage(@Param("target") TargetType target, Pageable pageable);
	
	/**
	 * ExercisePost의 필드들로 PostForCommunityViewDTO들로 바인딩하여 가져오는 메소드, 페이징 처리 <br>
	 * 특정 TargetType 만족하는 것들만 가져온다 <br>
	 * 키워드 검색은 제목, 내용에 적용 
	 * @param target ExerciePost와 연관된 Exercise의 target
	 * @param keyword 찾으려는 ExercisePost들의 제목 또는 내용
	 * @param pageable
	 * @return
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, e.name, ep.createdAt, ep.viewNum) from ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE (e.target = :target) AND "
			+ "((ep.title LIKE %:keyword%) OR (ep.contents LIKE %:keyword%) OR (u.nickName LIKE %:keyword%)) "
			+ "ORDER BY ep.createdAt DESC")
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordInPage(@Param("target") TargetType target, @Param("keyword")String keyword, Pageable pageable);
		
	/**
	 * 해당 User ID를 만족하는 ExercisePost 개수 반환
	 * @param userId ExercisePost 작성한 User의 ID
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(ep) FROM ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "ON u.id = :userId")
	public long countWithUserId(@Param("userId") Long userId);
	
	/**
	 * 게시글 내용이나 제목에서 키워드를 포함하는 게시글들의 개수 반환
	 * @param keyword 찾으려는 ExercisePost들의 제목 또는 내용
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(ep) FROM ExercisePost ep "
			+ "WHERE (ep.title LIKE %:keyword%) "
			+ "OR (ep.contents LIKE %:keyword%)")
	public long countWithTitleAndContentsKeyword(@Param("keyword") String keyword);
	
	/**
	 * 해당 target을 만족하는 것들만의 개수 반환
	 * @param target ExerciePost와 연관된 Exercise의 target
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(ep) FROM ExercisePost ep "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE e.target = :target")
	public long countWithTarget(@Param("target") TargetType target);
	
	/**
	 * 해당 target을 만족하고 제목과 내용에 키워드를 포함하는것들만의 개수 반환
	 * @param target ExerciePost와 연관된 Exercise의 target
	 * @param keyword 찾으려는 ExercisePost들의 제목 또는 내용
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(ep) FROM ExercisePost ep "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE (e.target = :target) AND "
			+ "((ep.title LIKE %:keyword%) OR (ep.contents LIKE %:keyword%))")
	public long countWithTargetAndKeyword(@Param("target") TargetType target, @Param("keyword") String keyword);
	
	/**
	 * ID로 ExercisePost 삭제
	 * @param exercisePostId 삭제하려는 ExercisePost ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM ExercisePost ep "
			+ "WHERE ep.id = :exercisePostId")
	public void deleteByIdInQuery(@Param("exercisePostId") Long exercisePostId);
	
	/**
	 * User Id로 만족하는 ExercisePost들 일괄 삭제
	 * @param userId 삭제하려는 ExercisePost들의 User ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM ExercisePost ep "
			+ "WHERE ep.user.id = :userId")
	public void deleteAllByUserIdInQuery(@Param("userId") Long userId);
	
	/**
	 * Exercise Id로 만족하는 ExercisePost들 일괄 삭제
	 * @param exerciseId 삭제하려는 ExercisePost들의 Exercise ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM ExercisePost ep "
			+ "WHERE ep.exercise.id = :exerciseId")
	public void deleteAllByExerciseIdInQuery(@Param("exerciseId") Long exerciseId);
}
