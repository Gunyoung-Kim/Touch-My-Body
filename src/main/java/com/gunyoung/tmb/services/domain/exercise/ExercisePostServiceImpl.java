package com.gunyoung.tmb.services.domain.exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.services.domain.like.PostLikeService;

import lombok.RequiredArgsConstructor;

/**
 * ExercisePostService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("exercisePostService")
@Transactional
@RequiredArgsConstructor
public class ExercisePostServiceImpl implements ExercisePostService {

	private final ExercisePostRepository exercisePostRepository;
	
	private final CommentService commentService;
	
	private final PostLikeService postLikeService;
	
	@Override
	@Transactional(readOnly=true)
	public ExercisePost findById(Long id) {
		Optional<ExercisePost> result = exercisePostRepository.findById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ExercisePost findWithPostLikesById(Long id) {
		Optional<ExercisePost> result = exercisePostRepository.findWithPostLikesById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ExercisePost findWithCommentsById(Long id) {
		Optional<ExercisePost> result = exercisePostRepository.findWithCommentsById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtAsc(Long userId,Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllByUserIdOrderByCreatedAtAscInPage(userId,pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtDesc(Long userId,Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllByUserIdOrderByCreatedAtDescInPage(userId,pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOOderByCreatedAtDESCByPage(Integer pageNumber,int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllForPostForCommunityViewDTOOrderByCreatedAtDescInPage(pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(String keyword,
			Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllForPostForCommunityViewDTOWithKeywordInPage(keyword, pageRequest);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(TargetType target,
			Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllForPostForCommunityViewDTOWithTargetInPage(target, pageRequest);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType target,
			String keyword, Integer pageNumber, int pageSize) {
		PageRequest pageRuquest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllForPostForCommunityViewDTOWithTargetAndKeywordInPage(target, keyword, pageRuquest);
	}

	@Override
	public ExercisePost save(ExercisePost exercisePost) {
		return exercisePostRepository.save(exercisePost);
	}

	@Override
	public ExercisePost saveWithUserAndExercise(ExercisePost exercisePost, User user, Exercise exericse) {
		user.getExercisePosts().add(exercisePost);
		exericse.getExercisePosts().add(exercisePost);
		
		exercisePost.setUser(user);
		exercisePost.setExercise(exericse);
		
		return save(exercisePost);
	}
	
	@Override
	public void deleteById(Long id) {
		ExercisePost exercisePost = findById(id);
		if(exercisePost != null)
			delete(exercisePost);
	}
	
	@Override
	public void checkIsMineAndDelete(Long userId, Long exercisePostId) {
		Optional<ExercisePost> exercisePost = exercisePostRepository.findByUserIdAndExercisePostId(userId, exercisePostId);
		exercisePost.ifPresent((ep)-> {
			delete(ep);
		});
	}
	
	@Override
	public void delete(ExercisePost exercisePost) {
		deleteAllOneToManyEntityForExercisePost(exercisePost);
		exercisePostRepository.deleteByIdInQuery(exercisePost.getId());
	}
	
	@Override
	public void deleteAllByUserId(Long userId) {
		List<ExercisePost> exercisePosts = exercisePostRepository.findAllByUserIdInQuery(userId);
		for(ExercisePost exercisePost: exercisePosts) {
			deleteAllOneToManyEntityForExercisePost(exercisePost);
		}
		exercisePostRepository.deleteAllByUserIdInQuery(userId);
	}
	
	@Override
	public void deleteAllByExerciseId(Long exerciseId) {
		List<ExercisePost> exercisePosts = exercisePostRepository.findAllByExerciseIdInQuery(exerciseId);
		for(ExercisePost exercisePost: exercisePosts) {
			deleteAllOneToManyEntityForExercisePost(exercisePost);
		}
		exercisePostRepository.deleteAllByExerciseIdInQuery(exerciseId);
	}
	
	private void deleteAllOneToManyEntityForExercisePost(ExercisePost exercisePost) {
		Long exercisePostId = exercisePost.getId();
		postLikeService.deleteAllByExercisePostId(exercisePostId);
		commentService.deleteAllByExercisePostId(exercisePostId);
	}

	@Override
	@Transactional(readOnly=true)
	public long count() {
		return exercisePostRepository.count();
	}
	
	@Override
	@Transactional(readOnly=true)
	public long countWithUserId(Long userId) {
		return exercisePostRepository.countWithUserId(userId);
	}

	@Override
	@Transactional(readOnly=true)
	public long countWithTitleAndContentsKeyword(String keyword) {
		return exercisePostRepository.countWithTitleAndContentsKeyword(keyword);
	}

	@Override
	@Transactional(readOnly=true)
	public long countWithTarget(TargetType target) {
		return exercisePostRepository.countWithTarget(target);
	}

	@Override
	@Transactional(readOnly=true)
	public long countWithTargetAndKeyword(TargetType target, String keyword) {
		return exercisePostRepository.countWithTargetAndKeyword(target, keyword);
	}

	@Override
	@Transactional(readOnly=true)
	public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostId(Long id) {
		Optional<ExercisePostViewDTO> result = exercisePostRepository.findForExercisePostViewDTOById(id);
		return result.orElse(null);
	}

	@Override
	public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(Long id) {
		ExercisePost exercisePost = findById(id);
		if(exercisePost == null)
			return null;
		
		exercisePost.setViewNum(exercisePost.getViewNum()+1);
		
		save(exercisePost);
		
		return getExercisePostViewDTOWithExercisePostId(id);
	}
}
