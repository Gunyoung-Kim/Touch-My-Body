package com.gunyoung.tmb.services.domain.exercise;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;

public interface ExercisePostService {
	public ExercisePost findById(Long id);
	
	public ExercisePost findWithPostLikesById(Long id);
	public ExercisePost findWithCommentsById(Long id);
	
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtAsc(Long userId,Integer pageNumber, int pageSize);
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtDesc(Long userId,Integer pageNumber, int pageSize);
	
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber,int pageSize);
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(String keyword,Integer pageNumber, int pageSize);
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(TargetType target,Integer pageNumber, int pageSize);
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType target,String keyword ,Integer pageNumber, int pageSize);
	
	public ExercisePost save(ExercisePost exercisePost);
	public ExercisePost saveWithUserAndExercise(ExercisePost exercisePost, User user, Exercise exericse);
	
	public void delete(ExercisePost exercisePost);
	public void deleteById(Long id);
	public void checkIsMineAndDelete(Long userId, Long exercisePostId);
	
	public long count();
	public long countWithUserId(Long userId);
	public long countWithTitleAndContentsKeyword(String keyword);
	public long countWithTarget(TargetType target);
	public long countWithTargetAndKeyword(TargetType target, String keyword);
	
	public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostId(Long id);
	public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(Long id);
}