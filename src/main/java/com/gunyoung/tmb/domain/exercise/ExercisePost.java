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
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import com.gunyoung.tmb.domain.BaseEntity;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관련 운동에 대한 사용자들의 게시글을 나타내는 Entity
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExercisePost extends BaseEntity {
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "exercise_post_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 해당 게시글의 제목
	 */
	@Column(length=30)
	@NotEmpty
	private String title;
	
	/**
	 * 해당 게시글의 내용글
	 */
	@Column(columnDefinition="TEXT NOT NULL")
	@NotEmpty
	private String contents;
	
	/**
	 * 해당 게시글의 조회수 <br>
	 * default = 0 
	 */
	@Builder.Default
	private Integer viewNum = 0;
	
	/**
	 * 해당 게시글의 관련 운동
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="exercise_id")
	private Exercise exercise;
	
	/**
	 * 해당 게시글의 작성자
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	/**
	 * 해당 게시글에 추가된 댓글들 <br>
	 * fetch: 지연로딩
	 */
	@OneToMany(mappedBy="exercisePost", orphanRemoval = true)
	@Builder.Default
	private List<Comment> comments = new ArrayList<>();
	
	/**
	 * 해당 게시글에 추가된 좋아요들 <br>
	 * fetch: 지연로딩
	 */
	@OneToMany(mappedBy="exercisePost", orphanRemoval = true)
	@Builder.Default
	private List<PostLike> postLikes = new ArrayList<>();
	
	/**
	 * ExercisePost 인스턴스의 연관 객체 필드를 제외한 필드 정보 반환
	 * @author kimgun-yeong
	 */
	@Override
	public String toString() {
		return "[ id = " + id + ", title = " + title + ", contents = " + contents + ", viewNum = " + viewNum + " ]";
	}
	
	/**
	 * 해당 객체의 viewNum 필드 값을 1 증가 시킴
	 * @author kimgun-yeong
	 */
	public void increaseViewNum() {
		this.viewNum++;
	}
}
