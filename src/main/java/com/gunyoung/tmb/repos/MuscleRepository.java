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
	 * name 필드로 Muscle 찾기 
	 * @param name 찾으려는 Muscle의 name
	 * @return
	 * @author kimgun-yeong
	 */
	public Optional<Muscle> findByName(String name); 
	
	/**
	 * 해당 키워드를 이름에 포함하는 Muscle들 찾기 <br>
	 * 페이징 처리
	 * @param keyword 찾으려는 Muscle의 이름 검색 키워드
	 * @param pageable
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT m FROM Muscle m "
			+ "WHERE m.name LIKE %:keyword%")
	public Page<Muscle> findAllWithNameKeyword(@Param("keyword") String keyword,Pageable pageable);
	
	
	/**
	 * Muscle들의 name과 category 필드들로 {@link MuscleNameAndCategoryDTO} 매핑하는 쿼리 <br> 
	 * 그냥 findAll 쓰지 않은 이유: 추후에 muscle에 필드가 추가되도 이 두 컬럼 값만 가져오기 위함
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT new com.gunyoung.tmb.dto.jpa.MuscleNameAndCategoryDTO(m.name, m.category) FROM Muscle m")
	public List<MuscleNameAndCategoryDTO> findAllWithNamaAndCategory();
	
	/**
	 * 해당 키워드를 이름에 포함하는 Muscle들 개수 찾기
	 * @param keyword 찾으려는 Muscle의 이름 검색 키워드
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT COUNT(m) FROM Muscle m "
			+ "WHERE m.name LIKE %:keyword%")
	public long countAllWithNamekeyword(@Param("keyword") String keyword);
	
	/**
	 * name 필드로 Muscle 존재하는지 여부 확인 
	 * @param name 찾으려는 Muscle의 name
	 * @return
	 * @author kimgun-yeong
	 */
	public boolean existsByName(String name);
	
}	
