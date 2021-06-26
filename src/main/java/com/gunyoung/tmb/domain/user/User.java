package com.gunyoung.tmb.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.gunyoung.tmb.domain.BaseEntity;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 웹 서비스의 이용자를 나타내는 Entity
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 유저의 이메일
	 */
	@Column(unique= true)
	@Email
	private String email;
	
	/**
	 * 유저의 비밀번호
	 */
	@NotNull
	@NotEmpty
	private String password;
	
	/**
	 * 유저의 이름
	 */
	@Column(length=40,nullable =false)
	@NotEmpty
	private String firstName;
	
	/**
	 * 유저의 성
	 */
	@Column(length=40,nullable =false)
	@NotEmpty
	private String lastName;
	
	/**
	 * 유저의 웹서비스에서의 닉네임
	 */
	@Column(length=10,nullable =false)
	@NotEmpty
	private String nickName;
	
	/**
	 * 유저의 권한
	 */
	@Enumerated(EnumType.STRING)
	@NotNull
	@Builder.Default
	private RoleType role = RoleType.USER;
	
	/**
	 * 유저의 개인 웨이트 트레이닝 기록들
	 * fetch: 지연로딩
	 * cascade: Remove - 유저가 삭제되면 관련 운동 기록들도 모두 삭제
	 */
	@OneToMany(mappedBy="user",cascade= {CascadeType.REMOVE})
	@Builder.Default
	List<UserExercise> userExercises = new ArrayList<>();
	
	/**
	 * 유저가 작성한 피드백들 
	 * fetch: 지연로딩
	 * cascade: Remove - 유저가 삭제되면 관련 피드백도 모두 삭제
	 */
	@OneToMany(mappedBy="user", cascade = {CascadeType.REMOVE})
	@Builder.Default
	List<Feedback> feedbacks = new ArrayList<>();
	
	/**
	 * 유저가 작성한 게시글들
	 * fetch: 지연로딩
	 * cascade: Remove - 유저가 삭제되면 관련 게시글도 모두 삭제
	 */
	@OneToMany(mappedBy="user",cascade= {CascadeType.REMOVE})
	@Builder.Default
	List<ExercisePost> exercisePosts = new ArrayList<>();
	
	/**
	 * 유저가 게시글에 추가한 좋아요들
	 * fetch: 지연로딩
	 * cascade: Remove - 유저가 삭제되면 관련 게시글 좋아요도 모두 삭제
	 */
	@OneToMany(mappedBy="user",cascade = {CascadeType.REMOVE})
	@Builder.Default
	List<PostLike> postLikes = new ArrayList<>();
	
	/**
	 * 유저가 작성한 댓글들
	 * fetch: 지연로딩
	 * cascade: Remove - 유저가 삭제되면 관련 댓글들도 모두 삭제
	 */
	@OneToMany(mappedBy="user",cascade= {CascadeType.REMOVE})
	@Builder.Default
	List<Comment> comments = new ArrayList<>();
	
	/**
	 * 유저가 댓글에 추가한 좋아요들
	 * fetch: 지연로딩
	 * cascade: Remove - 유저가 삭제되면 관련 댓글 좋아요들도 모두 삭제
	 */
	@OneToMany(mappedBy="user",cascade= {CascadeType.REMOVE})
	@Builder.Default
	List<CommentLike> commentLikes = new ArrayList<>();
	
	/**
	 * 유저의 Full Name을 반환하는 메소드
	 * @return 유저의 Full Name
	 * @author kimgun-yeong
	 */
	public String getFullName() {
		return lastName + firstName;
	}
	
}
