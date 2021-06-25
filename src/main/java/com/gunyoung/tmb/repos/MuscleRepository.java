package com.gunyoung.tmb.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.jpa.MuscleNameAndCategoryDTO;

public interface MuscleRepository extends JpaRepository<Muscle,Long> {
	public Optional<Muscle> findByName(String name); 
	
	/**
	 * 그냥 findAll 쓰지 않은 이유: 추후에 muscle에 필드가 추가되도 이 두 컬럼 값만 가져오기 위함
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.jpa.MuscleNameAndCategoryDTO(m.name, m.category) FROM Muscle m")
	public List<MuscleNameAndCategoryDTO> findAllWithNamaAndCategory();
}	
