package com.gunyoung.tmb.services.domain.exercise;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;

public interface ExercisePostService {
	public ExercisePost findById(Long id);
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber);
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(String keyword,Integer pageNumber);
	
	public ExercisePost save(ExercisePost exercisePost);
	
	public void delete(ExercisePost exercisePost);
	
	public long count();
	public long countWithTitleAndContentsKeyword(String keyword);
}
