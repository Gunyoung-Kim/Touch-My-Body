package com.gunyoung.tmb.services.domain.exercise;

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
	
	/**
	 * @param id 찾으려는 ExerciePost id 값
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public ExercisePost findById(Long id) {
		Optional<ExercisePost> result = exercisePostRepository.findById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public ExercisePost findWithPostLikesById(Long id) {
		Optional<ExercisePost> result = exercisePostRepository.findWithPostLikesById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * 게시글에 달린 댓글과 페치조인해서 가져오는 메소드
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public ExercisePost findWithCommentsById(Long id) {
		Optional<ExercisePost> result = exercisePostRepository.findWithCommentsById(id);
		if(result.isEmpty())
			return null;
		return result.get();
	}
	
	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtAsc(Long userId,Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllByUserIdOrderByCreatedAtASCCustom(userId,pageRequest);
	}
	
	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtDesc(Long userId,Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllByUserIdOrderByCreatedAtDescCustom(userId,pageRequest);
	}
	
	/**
	 * @param pageNumber
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber,int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllForPostForCommunityViewDTOByPage(pageRequest);
	}
	
	/**
	 * @param keyword
	 * @param pageNumber
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(String keyword,
			Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, pageRequest);
	}
	
	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(TargetType target,
			Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllForPostForCommunityViewDTOWithTargetByPage(target, pageRequest);
	}

	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType target,
			String keyword, Integer pageNumber, int pageSize) {
		PageRequest pageRuquest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(target, keyword, pageRuquest);
	}


	/**
	 * @param exercisePost 저장하려는 ExercisePost
	 * @return 저장된 ExercisePost
	 * @author kimgun-yeong
	 */
	@Override
	public ExercisePost save(ExercisePost exercisePost) {
		return exercisePostRepository.save(exercisePost);
	}

	/**
	 * @param exercisePost 삭제하려는 ExercisePost
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(ExercisePost exercisePost) {
		User user = exercisePost.getUser();
		
		if(user != null) {
			user.getExercisePosts().remove(exercisePost);
		}
		
		Exercise exercise = exercisePost.getExercise();
		
		if(exercise != null) {
			exercise.getExercisePosts().remove(exercisePost);
		}
		
		exercisePostRepository.delete(exercisePost);
	}
	
	/**
	 * @author kimgun-yeong
	 */
	@Override
	public void deleteById(Long id) {
		ExercisePost exercisePost = findById(id);
		
		if(exercisePost != null)
			delete(exercisePost);
	}

	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long count() {
		return exercisePostRepository.count();
	}
	
	/**
	 * 해당 User ID 만족하는 ExercisePost 개수 반환
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countWithUserId(Long userId) {
		return exercisePostRepository.countWithUserId(userId);
	}

	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countWithTitleAndContentsKeyword(String keyword) {
		return exercisePostRepository.countWithTitleAndContentsKeyword(keyword);
	}

	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countWithTarget(TargetType target) {
		return exercisePostRepository.countWithTarget(target);
	}

	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countWithTargetAndKeyword(TargetType target, String keyword) {
		return exercisePostRepository.countWithTargetAndKeyword(target, keyword);
	}

	/**
	 * ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환
	 * @param id ExercisePost ID
	 * @return ExercisePostViewDTO, null(해당 id의 ExercisePost 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostId(Long id) {
		Optional<ExercisePostViewDTO> result = exercisePostRepository.findForExercisePostViewDTOById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}

	/**
	 * ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 <br>
	 * ExercisePost viewNum(조회수) 증가
	 * @param id ExercisePost ID
	 * @return ExercisePostViewDTO, null(해당 id의 ExercisePost 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostIdAndIncreasViewNum(Long id) {
		ExercisePost exercisePost = findById(id);
		if(exercisePost == null)
			return null;
		
		exercisePost.setViewNum(exercisePost.getViewNum()+1);
		
		save(exercisePost);
		
		return getExercisePostViewDTOWithExercisePostId(id);
	}
}
