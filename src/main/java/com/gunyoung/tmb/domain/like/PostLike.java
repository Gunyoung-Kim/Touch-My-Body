package com.gunyoung.tmb.domain.like;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글에 추가되는 좋아요를 나타내는 Entity
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLike extends Like {
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "post_like_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 좋아요가 추가된 게시글을 나타내는 객체
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="exercise_post_id")
	private ExercisePost exercisePost;
	
	/**
	 * 해당 좋아요를 추가한 User
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	/**
	 * PostLike 인스턴스의 연관 객체 필드를 제외한 필드 정보 반환
	 * @author kimgun-yeong
	 */
	@Override
	public String toString() {
		return "[ id = " + id + " ]";
	}
	
	/**
	 * User와 ExercisePost로 PostLike 생성 후 반환하는 정적 팩토리 메서드
	 * @author kimgun-yeong
	 */
	public static PostLike of(User user, ExercisePost exercisePost) {
		return builder()
				.user(user)
				.exercisePost(exercisePost)
				.build();
	}
}
