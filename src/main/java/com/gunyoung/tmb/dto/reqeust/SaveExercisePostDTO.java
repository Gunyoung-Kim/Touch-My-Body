package com.gunyoung.tmb.dto.reqeust;

import com.gunyoung.tmb.domain.exercise.ExercisePost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 추가할때 사용되는 객체
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveExercisePostDTO {
	private String title;
	private String contents;
	private String exerciseName;
	
	/**
	 * 이 객체를 통해 ExercisePost 생성 후 반환
	 * @author kimgun-yeong
	 */
	public ExercisePost createExercisePost() {
		ExercisePost exercisePost = ExercisePost.builder()
				.title(this.title)
				.contents(this.contents)
				.build();
		return exercisePost;
	}
}
