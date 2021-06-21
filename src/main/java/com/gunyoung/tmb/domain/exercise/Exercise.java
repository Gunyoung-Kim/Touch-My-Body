package com.gunyoung.tmb.domain.exercise;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.gunyoung.tmb.domain.BaseEntity;
import com.gunyoung.tmb.enums.TargetType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 웨이트 트레이닝 운동 종류를 나타내는 Entity
 * @author kimgun-yeong
 *
 */
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise extends BaseEntity{
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "exercise_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 해당 운동의 이름
	 */
	@NotNull
	@NotEmpty
	private String name;
	
	/**
	 * 해당 운동에 대한 설명
	 */
	@Column(columnDefinition="TEXT",nullable = false)
	private String description;
	
	/**
	 * 해당 운동할 떄 주의할 점 
	 */
	@Column(columnDefinition="TEXT")
	private String caution;
	
	/**
	 * 해당 운동하는 방법
	 */
	@Column(columnDefinition="TEXT")
	private String movement;
	
	/**
	 * 해당 운동의 주 타격 부위(ex. 가슴,어깨,등,하체)
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
	private TargetType target;
	
	/**
	 * 해당 운동으로 성장시킬 수 있는 구체적인 근육 부위들
	 */
	@OneToMany(mappedBy="exercise")
	@Builder.Default
	private List<ExerciseMuscle> exerciseMuscles = new ArrayList<>();
	
	/**
	 * 해당 운동에 관해 유저들이 작성한 게시물들
	 * fetch: 지연로딩
	 * cascade: Remove - 해당 운동이 삭제되면 관련 게시물들도 모두 삭제
	 */
	@OneToMany(mappedBy="exercise",cascade= {CascadeType.REMOVE})
	@Builder.Default
	private List<ExercisePost> exercisePosts = new ArrayList<>();
	
	/**
	 * 해당 운동에 대한 심화 정보들을 담고 있는 객체
	 * fetch: 지연로딩
	 * cascade: All
	 */
	@OneToOne(mappedBy="exercise",fetch=FetchType.LAZY,cascade = {CascadeType.ALL})
	private ExerciseSpace exerciseSpace;
	
}
