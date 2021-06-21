package com.gunyoung.tmb.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.exercise.Muscle;

public interface MuscleRepository extends JpaRepository<Muscle,Long> {

}
