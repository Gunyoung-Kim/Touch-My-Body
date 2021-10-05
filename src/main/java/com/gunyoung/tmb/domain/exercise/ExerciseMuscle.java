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
	
	@NotNull
	private String muscleName;
	
	/**
	 * 관련 운동을 나타내는 Exercise 객체
	 * fetch: 지연로딩
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
	
	/**
	 * ExerciseMuscle 인스턴스의 연관 객체 필드를 제외한 필드 정보 반환
	 * @author kimgun-yeong
	 */
	@Override
	public String toString() {
		return "[ id = " + id + ", isMain = " + isMain + ", muscleName = " + muscleName +" ]";
	}

	/**
	 * Exercise, List of Muscle 를 이용해 isMain 필드 값이 true인 ExerciseMuscle List 생성 후 반환하는 정적 팩토리 메서드
	 * @param exercise 생성 하려는 ExerciseMuscle 들의 Exercise
	 * @param muscleList 생성 하려는 ExerciseMuscle 들의 Muscle 리스트
	 * @author kimgun-yeong
	 */
	public static List<ExerciseMuscle> mainOf(Exercise exercise, List<Muscle> muscleList) {
		return of(exercise, muscleList, true);
	}
	
	/**
	 * Exercise, List of Muscle 를 이용해 isMain 필드 값이 false인 ExerciseMuscle List 생성 후 반환하는 정적 팩토리 메서드
	 * @param exercise 생성 하려는 ExerciseMuscle 들의 Exercise
	 * @param muscleList 생성 하려는 ExerciseMuscle 들의 Muscle 리스트
	 * @author kimgun-yeong
	 */
	public static List<ExerciseMuscle> subOf(Exercise exercise, List<Muscle> muscleList) {
		return of(exercise, muscleList, false);
	}
	
	private static List<ExerciseMuscle> of(Exercise exercise, List<Muscle> muscleList, boolean isMain) {
		List<ExerciseMuscle> exerciseMuscleList = new ArrayList<>();
		for(Muscle muscle: muscleList) {
			ExerciseMuscle em = ExerciseMuscle.builder()
					.exercise(exercise)
					.muscleName(muscle.getName())
					.muscle(muscle)
					.isMain(isMain)
					.build();
			exerciseMuscleList.add(em);
		}
		return exerciseMuscleList; 
	}
}
