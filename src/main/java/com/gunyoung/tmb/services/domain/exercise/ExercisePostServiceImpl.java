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
	 * ID로 ExercisePost 찾기
	 * @param id 찾으려는 ExerciePost id 값
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @since 11
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
	 * ID로 PostLikes 페치조인 후 ExercisePost 반환
	 * @param id 찾으려는 ExerciePost id 값
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @since 11
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
	 * ID로 Comments 페치 조인후 ExercisePost 반환
	 * @param id 찾으려는 ExerciePost id 값
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @since 11
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
	 * UserID 를 만족하는 ExercisePost들 생성 오래된순으로 페이지 반환
	 * @param userId ExercisePost들의 작성자 ID 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtAsc(Long userId,Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllByUserIdOrderByCreatedAtASCCustom(userId,pageRequest);
	}
	
	/**
	 * UserID 를 만족하는 ExercisePost들 생성 최신순으로 페이지 반환
	 * @param userId ExercisePost들의 작성자 ID 
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtDesc(Long userId,Integer pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllByUserIdOrderByCreatedAtDescCustom(userId,pageRequest);
	}
	
	/**
	 * 모든 ExercisePost로 {@link PostForCommunityViewDTO} 생성 후 페이지 반환
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber,int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
		return exercisePostRepository.findAllForPostForCommunityViewDTOByPage(pageRequest);
	}
	
	/**
	 * 키워드를 만족하는 ExercisePost들로 {@link PostForCommunityViewDTO} 생성 후 페이지 반환
	 * @param keyword ExercisePost의 title, contents 검색 키워드
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
	 * target 을 만족하는 ExercisePost들로 {@link PostForCommunityViewDTO} 생성 후 페이지 반환
	 * @param target ExercisePost들의 target
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
	 * target,키워드를 만족하는 ExercisePost들로 {@link PostForCommunityViewDTO} 생성 후 페이지 반환
	 * @param target ExercisePost들의 target
	 * @param keyword ExercisePost의 title, contents 검색 키워드
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
	 * ExercisePost 생성 및 수정
	 * @param exercisePost 저장하려는 ExercisePost
	 * @return 저장된 ExercisePost
	 * @author kimgun-yeong
	 */
	@Override
	public ExercisePost save(ExercisePost exercisePost) {
		return exercisePostRepository.save(exercisePost);
	}

	/**
	 * User, Exercise와 연관 관계 추가 후 ExercisePost 저장
	 * @author kimgun-yeong
	 */
	@Override
	public ExercisePost saveWithUserAndExercise(ExercisePost exercisePost, User user, Exercise exericse) {
		user.getExercisePosts().add(exercisePost);
		
		exericse.getExercisePosts().add(exercisePost);
		
		exercisePost.setUser(user);
		exercisePost.setExercise(exericse);
		
		return save(exercisePost);
	}
	
	/**
	 * ExercisePost 삭제 
	 * @param exercisePost 삭제하려는 ExercisePost
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(ExercisePost exercisePost) {
		exercisePostRepository.delete(exercisePost);
	}
	
	/**
	 * ID를 만족하는 ExercisePost 삭제
	 * @param id 삭제하려는 ExercisePost의 ID
	 * @author kimgun-yeong
	 */
	@Override
	public void deleteById(Long id) {
		ExercisePost exercisePost = findById(id);
		
		if(exercisePost != null)
			delete(exercisePost);
	}
	
	/**
	 * User ID, ExercisePost ID 에 해당하는 ExercisePost 있으면 삭제 
	 * @param userId User의 ID
	 * @param exercisePostId ExercisePost의 ID
	 * @author kimgun-yeong
	 */
	@Override
	public void checkIsMineAndDelete(Long userId, Long exercisePostId) {
		Optional<ExercisePost> exercisePost = exercisePostRepository.findByUserIdAndExercisePostId(userId, exercisePostId);
		exercisePost.ifPresent((ep)-> {
			delete(ep);
		});
	}

	/**
	 * 모든 ExercisePost의 개수 반환
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long count() {
		return exercisePostRepository.count();
	}
	
	/**
	 * 해당 User ID 만족하는 ExercisePost 개수 반환
	 * @param ExercisePost들의 작성자 ID
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countWithUserId(Long userId) {
		return exercisePostRepository.countWithUserId(userId);
	}

	/**
	 * Title, Contents 검색 키워드 만족하는 ExercisePost 개수 반환
	 * @param keyword title, contents 검색 키워드
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countWithTitleAndContentsKeyword(String keyword) {
		return exercisePostRepository.countWithTitleAndContentsKeyword(keyword);
	}

	/**
	 * Exercise의 target 만족하는 ExercisePost 개수 반환
	 * @param target ExercisePost의 Exercise의 target
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countWithTarget(TargetType target) {
		return exercisePostRepository.countWithTarget(target);
	}

	/**
	 * Title, Contents 검색 키워드 와 Exercise의 target 만족하는 ExercisePost 개수 반환
	 * @param target ExercisePost의 Exercise의 target
	 * @param keyword title, contents 검색 키워드
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public long countWithTargetAndKeyword(TargetType target, String keyword) {
		return exercisePostRepository.countWithTargetAndKeyword(target, keyword);
	}

	/**
	 * ExercisePost id로 ExercisePost 가져와서 이를 통해 {@link ExercisePostViewDTO} 생성 및 반환
	 * @param id ExercisePost ID
	 * @return ExercisePostViewDTO, null(해당 id의 ExercisePost 없을때)
	 * @since 11
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
	 * ExercisePost id로 ExercisePost 가져와서 이를 통해 {@link ExercisePostViewDTO} 생성 및 반환 <br>
	 * ExercisePost viewNum(조회수) 증가
	 * @param id ExercisePost ID
	 * @return ExercisePostViewDTO, null(해당 id의 ExercisePost 없을때)
	 * @author kimgun-yeong
	 */
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
