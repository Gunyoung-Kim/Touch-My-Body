package com.gunyoung.tmb.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.like.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long>{

}
