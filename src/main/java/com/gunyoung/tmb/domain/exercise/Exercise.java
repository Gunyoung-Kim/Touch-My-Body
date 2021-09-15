package com.gunyoung.tmb.domain.exercise;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	 * 해당 운동으로 성장시킬 수 있는 구체적인 근육 부위들 <br>
	 * fetch: 지연로딩 
	 */
	@OneToMany(mappedBy="exercise", orphanRemoval = true)
	@Builder.Default
	private List<ExerciseMuscle> exerciseMuscles = new ArrayList<>();
	
	/**
	 * 해당 운동에 관해 유저들이 작성한 게시물들 <br>
	 * fetch: 지연로딩
	 */
	@OneToMany(mappedBy="exercise", orphanRemoval = true)
	@Builder.Default
	private List<ExercisePost> exercisePosts = new ArrayList<>();
	
	/**
	 * 관련 운동의 정보에 대해 유저가 운영자에게 보낸 정보수정 요청 피드백들 <br>
	 * fetch: 지연로딩
	 */
	@OneToMany(mappedBy="exercise", orphanRemoval = true)
	@Builder.Default
	private List<Feedback> feedbacks = new ArrayList<>();
	
	/**
	 * Exercise 인스턴스의 연관 객체 필드를 제외한 필드 정보 반환
	 * @author kimgun-yeong
	 */
	@Override
	public String toString() {
		return "[ id = " + id + ", name = " + name + ", description = " + description 
				+ ", caution = " + caution + ", movement = " + movement + ", target = " + target +" ]";
	}
}
