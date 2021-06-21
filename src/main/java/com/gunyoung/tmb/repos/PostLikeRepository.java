package com.gunyoung.tmb.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.like.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike,Long>{

}
