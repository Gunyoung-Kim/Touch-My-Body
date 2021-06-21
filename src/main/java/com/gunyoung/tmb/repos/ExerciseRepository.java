package com.gunyoung.tmb.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.exercise.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise,Long>{

}
