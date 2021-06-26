package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.like.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long>{
	
	/**
	 * inner join 하게끔 커스텀 
	 * @param userId 찾으려는 CommentLike 의 User ID
	 * @param commentId 찾으려는 CommentLike 의 Comment ID
	 * @return 
	 * @author kimgun-yeong
	 */
	@Query("SELECT cl from CommentLike cl "
			+ "INNER JOIN cl.user u ON u.id = :userId "
			+ "INNER JOIN cl.comment c ON c.id = :commentId")
	public Optional<CommentLike> findByUserIdAndCommentIdCustom(@Param("userId") Long userId,@Param("commentId") Long commentId);
}
