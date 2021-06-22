package com.gunyoung.tmb.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.exercise.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise,Long>{
	public Optional<Exercise> findByName(String name);
}
