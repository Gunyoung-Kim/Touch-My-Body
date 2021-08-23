package com.gunyoung.tmb.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 매니저 화면에 노출되는 게시글 목록 구성하기 위해 사용되는 객체
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExercisePostForManageViewDTO {
	private Long postId;
	private String title;
	private String writer;
	private LocalDateTime createdAt;
	private Integer viewNum;
	
	/**
	 * ExercisePost 컬렉션과 User를 통해 ExercisePostForManageViewDTO 리스트 반환
	 * @author kimgun-yeong
	 */
	public static List<ExercisePostForManageViewDTO> of(Iterable<ExercisePost> exercisePosts, User user) {
		List<ExercisePostForManageViewDTO> dtos = new ArrayList<>();
		for(ExercisePost ep: exercisePosts) {
			ExercisePostForManageViewDTO dto = ExercisePostForManageViewDTO.builder()
					.postId(ep.getId())
					.title(ep.getTitle())
					.writer(user.getNickName())
					.createdAt(ep.getCreatedAt())
					.viewNum(ep.getViewNum())
					.build();
			dtos.add(dto);
		}
		return dtos;
	}
}
