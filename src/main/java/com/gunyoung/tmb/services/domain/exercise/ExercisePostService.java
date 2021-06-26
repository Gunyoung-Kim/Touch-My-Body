package com.gunyoung.tmb.services.domain.exercise;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;

public interface ExercisePostService {
	public ExercisePost findById(Long id);
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber);
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(String keyword,Integer pageNumber);
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(TargetType target,Integer pageNumber);
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType target,String keyword ,Integer pageNumber);
	
	public ExercisePost save(ExercisePost exercisePost);
	
	public void delete(ExercisePost exercisePost);
	
	public long count();
	public long countWithTitleAndContentsKeyword(String keyword);
	public long countWithTarget(TargetType target);
	public long countWithTargetAndKeyword(TargetType target, String keyword);
}