package com.gunyoung.tmb.domain.exercise;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.gunyoung.tmb.domain.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관련 운동을 통해 성장시킬 수 있는 근육을 나타내는 Entity
 * Exercise 와 Muscle을 연결시켜주는 역할
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMuscle extends BaseEntity{
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "exercise_muscle_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 해당 근육이 관련 운동의 주 자극 운동인가(true), 부 자극운동인가의 여부(false)
	 */
	@NotNull
	private boolean isMain;
	
	/**
	 * 관련 운동을 나타내는 Exercise 객체
	 * fetch: 지로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="exercise_id")
	private Exercise exercise;
	
	/**
	 * 관련 근육을 나타내는 Muscle 
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="muscle_id")
	private Muscle muscle;
}
