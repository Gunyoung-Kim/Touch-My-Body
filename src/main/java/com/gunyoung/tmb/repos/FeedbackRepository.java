package com.gunyoung.tmb.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.exercise.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback,Long>{

}
