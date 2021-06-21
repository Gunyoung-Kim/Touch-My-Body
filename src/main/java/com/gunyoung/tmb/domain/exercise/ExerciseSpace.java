package com.gunyoung.tmb.domain.exercise;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.gunyoung.tmb.domain.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관련 운동에 대한 상세 정보를 담은 Entity
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSpace extends BaseEntity {
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "exercise_space_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 관련 운동
	 * fetch: 지연로딩 , inner join 필요시 커스텀 쿼리
	 */
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="exercise_id")
	private Exercise exercise;
	
	/**
	 * 관련 운동의 정보에 대해 유저가 운영자에게 보낸 정보수정 요청 피드백들
	 * fetch: 지연로딩
	 */
	@OneToMany(mappedBy="exerciseSpace")
	@Builder.Default
	private List<Feedback> feedbacks = new ArrayList<>();
}
