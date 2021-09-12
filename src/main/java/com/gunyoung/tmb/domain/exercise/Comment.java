package com.gunyoung.tmb.domain.exercise;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 운동 관련 게시글에 달리는 댓글을 나타내는 Entity
 * @author kimgun-yeong
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
	
	/**
	 * id 값
	 */
	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 댓글 작성자의 ip 주소
	 */
	@Column(name = "writer_ip", nullable = false)
	private String writerIp;
	
	/**
	 * 댓글 내용
	 */
	@Column(columnDefinition="TEXT", nullable = false)
	@NotEmpty
	private String contents;
	
	/**
	 * 댓글 작성을 익명으로 했는지 여부
	 */
	@Builder.Default
	private boolean isAnonymous = false;
	
	/**
	 * 댓글 작성자를 나타내는 User 객체
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	/**
	 * 댓글이 추가된 해당 게시글을 나타내는 ExercisePost 객체
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="exercise_post_id")
	private ExercisePost exercisePost;
	
	/**
	 * 댓글에 추가되는 좋아요를 나타내는 CommentLike List 객체  
	 * fetch: 지연로딩
	 * cascade: Remove - Comment 객체 삭제 시 관련 CommentLike 객체들도 삭제
	 */
	@OneToMany(mappedBy="comment",cascade= {CascadeType.REMOVE})
	@Builder.Default
	private List<CommentLike> commentLikes = new ArrayList<>();
	
	/**
	 * Comment 인스턴스의 연관 객체 필드를 제외한 필드 정보 반환
	 * @author kimgun-yeong
	 */
	@Override
	public String toString() {
		return "[ id = " + id + ", writerIp = " + writerIp + ", contents = " + contents + ", isAnonymous = " + isAnonymous +" ]"; 
	}
}
