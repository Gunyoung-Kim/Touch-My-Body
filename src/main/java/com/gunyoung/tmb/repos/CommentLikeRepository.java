package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.like.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long>{
	
	/**
	 * hiberante은 페치 조인 대상으로 별칭 허용
	 * @param userId 찾으려는 CommentLike 의 User ID
	 * @param commentId 찾으려는 CommentLike 의 Comment ID
	 * @return 
	 * @author kimgun-yeong
	 */
	@Query("SELECT cl from CommentLike cl "
			+ "JOIN FETCH cl.user u "
			+ "JOIN FETCH cl.comment c "
			+ "WHERE (u.id = :userId) "
			+ "AND (c.id = :commentId)")
	public Optional<CommentLike> findByUserIdAndCommentIdCustom(@Param("userId") Long userId,@Param("commentId") Long commentId);
}
