package com.gunyoung.tmb.domain.like;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글에 대한 좋아요를 나타내는 Entity
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike extends Like{
	
	/**
	 * id 
	 */
	@Id
	@Column(name = "comment_like_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 좋아요를 추가한 User 객체 
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	/**
	 * 해당 좋아요가 추가된 댓글
	 * fetch: 지연로딩
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="comment_id")
	private Comment comment;
	
	/**
	 * CommentLike 인스턴스의 연관 객체 필드를 제외한 필드 정보 반환
	 * @author kimgun-yeong
	 */
	@Override
	public String toString() {
		return "[ id = " + id + " ]";
	}
}
