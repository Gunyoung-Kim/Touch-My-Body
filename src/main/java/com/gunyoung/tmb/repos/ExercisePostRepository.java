package com.gunyoung.tmb.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;

public interface ExercisePostRepository extends JpaRepository<ExercisePost,Long>{
	
	/**
	 * ExercisePost의 필드들로 PostForCommunityViewDTO들로 바인딩하여 가져오는 메소드, 페이징 처리 <br>
	 * INNER JOIN 활용 
	 * @param pageable
	 * @return
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, ep.createdAt, ep.viewNum) from ExercisePost ep "
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
	@Query("SELECT new com.gunyoung.tmb.dto.response.PostForCommunityViewDTO (ep.id, e.target, ep.title, u.nickName, ep.createdAt, ep.viewNum) from ExercisePost ep "
			+ "INNER JOIN ep.user u "
			+ "INNER JOIN ep.exercise e "
			+ "WHERE (ep.title LIKE %:keyword%) "
			+ "OR (ep.contents LIKE %:keyword%) "
			+ "ORDER BY ep.createdAt DESC")
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(@Param("keyword") String keyword, Pageable pageable);
	
	/**
	 * 게시글 내용이나 제목에서 키워드를 포함한느 게시글들의 개수 반환
	 * @param keyword
	 * @return
	 */
	@Query("SELECT COUNT(ep) FROM ExercisePost ep "
			+ "WHERE (ep.title LIKE %:keyword%) "
			+ "OR (ep.contents LIKE %:keyword%)")
	public long countWithTitleAndContentsKeyword(@Param("keyword") String keyword);
}
