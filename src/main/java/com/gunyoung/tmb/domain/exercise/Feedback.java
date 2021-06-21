package com.gunyoung.tmb.domain.exercise;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.gunyoung.tmb.domain.BaseEntity;
import com.gunyoung.tmb.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 운동 정보에 대한 유저가 느낀 수정 필요 사항을 나타내는 Entity <br>
 * 유저(role:USER) 가 운영자(role:ADMIN)에게 보냄
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback extends BaseEntity {
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "feedback_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 피드백의 제목
	 */
	@Column(length=50,nullable = false)
	@NotEmpty
	private String title;
	
	/**
	 * 피드백의 본문 내용
	 */
	@Column(columnDefinition="TEXT",nullable= false)
	@NotEmpty
	private String content;
	
	/**
	 * 피드백을 작성한 User 
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	/**
	 * 피드백이 작성된 대상 운동 정보
	 * fetch: 지로딩
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="exercise_space_id")
	private ExerciseSpace exerciseSpace;
}
