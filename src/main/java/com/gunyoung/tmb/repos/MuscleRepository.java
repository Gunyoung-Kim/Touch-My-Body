package com.gunyoung.tmb.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.jpa.MuscleNameAndCategoryDTO;

public interface MuscleRepository extends JpaRepository<Muscle,Long> {
	
	/**
	 * 
	 * @param name
	 * @return
	 * @author kimgun-yeong
	 */
	public Optional<Muscle> findByName(String name); 
	
	/**
	 * 해당 키워드를 이름에 포함하는 Muscle 객체들 페이지로 반환
	 * @param keyword
	 * @param pageable
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT m FROM Muscle m "
			+ "WHERE m.name LIKE %:keyword%")
	public Page<Muscle> findAllWithNameKeyword(@Param("keyword") String keyword,Pageable pageable);
	
	
	/**
	 * 그냥 findAll 쓰지 않은 이유: 추후에 muscle에 필드가 추가되도 이 두 컬럼 값만 가져오기 위함
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.jpa.MuscleNameAndCategoryDTO(m.name, m.category) FROM Muscle m")
	public List<MuscleNameAndCategoryDTO> findAllWithNamaAndCategory();
	
	
	@Query("SELECT COUNT(m) FROM Muscle m "
			+ "WHERE m.name LIKE %:keyword%")
	public long countAllWithNamekeyword(@Param("keyword") String keyword);
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean existsByName(String name);
	
}	
