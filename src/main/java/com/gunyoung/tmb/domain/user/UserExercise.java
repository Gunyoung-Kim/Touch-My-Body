package com.gunyoung.tmb.domain.user;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.gunyoung.tmb.domain.BaseEntity;
import com.gunyoung.tmb.domain.exercise.Exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저가 작성한 개인 운동 기록을 나타내는 Entity <br>
 * (userId, date) 다중 컬럼 인덱싱 사용
 * @author kimgun-yeong
 *
 */
@Entity
@Table(indexes = @Index(name = "uIdAndDate", columnList = "user_id, date"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExercise extends BaseEntity {
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "user_exercise_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 몇 회 반복했는지 
	 */
	@Max(300)
	@Min(1)
	private Integer laps;
	
	/**
	 * 몇 세트 운동했는지
	 */
	@Max(100)
	@Min(1)
	private Integer sets;
	
	/**
	 * 몇 kg을 들었는지
	 */
	@Max(10000)
	@Min(1)
	private Integer weight;
	
	/** 
	 * 운동 기록에 추가할 말
	 * 예를 들어 스쿼트 중에서도 어떤 스쿼트를 했는지, 어디가 불편했는지 등등
	 */
	@Column(nullable = false)
	private String description;
	
	/**
	 * 해당 운동을 몇년 몇월 며칠에 했는지
	 */
	@NotNull
	@Temporal(value = TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Calendar date;
	
	/**
	 * 해당 운동 기록을 작성한 User
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	/**
	 * 어떤 종류의 운동을 했는지
	 * fetch: 지연로딩
	 */
	@ManyToOne
	@JoinColumn(name = "exercise_id")
	private Exercise exercise;
	
	/**
	 * UserExercise 인스턴스의 연관 객체 필드를 제외한 필드 정보 반환
	 * @author kimgun-yeong
	 */
	@Override
	public String toString() {
		return "[ id = " + id + ", laps = " + laps + ", sets = " + sets + ", weight = " + weight + ", description = " + description 
				+ ", date = " + date.get(Calendar.YEAR) + "." + date.get(Calendar.MONTH) + 1  + "." + date.get(Calendar.DATE) + " ]"; 
	}
}
