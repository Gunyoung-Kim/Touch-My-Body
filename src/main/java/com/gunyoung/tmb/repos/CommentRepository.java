package com.gunyoung.tmb.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.exercise.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
