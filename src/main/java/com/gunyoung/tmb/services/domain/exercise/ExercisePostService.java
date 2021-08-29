package com.gunyoung.tmb.services.domain.exercise;

import org.springframework.data.domain.Page;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;

public interface ExercisePostService {
	
	/**
	 * ID로 ExercisePost 찾기
	 * @param id 찾으려는 ExerciePost id 값
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public ExercisePost findById(Long id);
	
	/**
	 * ID로 PostLikes 페치조인 후 ExercisePost 반환
	 * @param id 찾으려는 ExerciePost id 값
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public ExercisePost findWithPostLikesById(Long id);
	
	/**
	 * ID로 Comments 페치 조인후 ExercisePost 반환
	 * @param id 찾으려는 ExerciePost id 값
	 * @return ExercisePost, NUll(해당 id의 ExercisePost가 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public ExercisePost findWithCommentsById(Long id);
	
	/**
	 * UserID 를 만족하는 ExercisePost들 생성 오래된순으로 페이지 반환
	 * @param userId ExercisePost들의 작성자 ID 
	 * @author kimgun-yeong
	 */
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtAsc(Long userId,Integer pageNumber, int pageSize);
	
	/**
	 * UserID 를 만족하는 ExercisePost들 생성 최신순으로 페이지 반환
	 * @param userId ExercisePost들의 작성자 ID 
	 * @author kimgun-yeong
	 */
	public Page<ExercisePost> findAllByUserIdOrderByCreatedAtDesc(Long userId,Integer pageNumber, int pageSize);
	
	/**
	 * 모든 ExercisePost로 {@link PostForCommunityViewDTO} 생성 후 페이지 반환
	 * @author kimgun-yeong
	 */
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOOderByCreatedAtDESCByPage(Integer pageNumber,int pageSize);
	
	/**
	 * 키워드를 만족하는 ExercisePost들로 {@link PostForCommunityViewDTO} 생성 후 페이지 반환
	 * @param keyword ExercisePost의 title, contents 검색 키워드
	 * @author kimgun-yeong
	 */
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(String keyword,Integer pageNumber, int pageSize);
	
	/**
	 * target 을 만족하는 ExercisePost들로 {@link PostForCommunityViewDTO} 생성 후 페이지 반환
	 * @param target ExercisePost들의 target
	 * @author kimgun-yeong
	 */
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(TargetType target,Integer pageNumber, int pageSize);
	
	/**
	 * target,키워드를 만족하는 ExercisePost들로 {@link PostForCommunityViewDTO} 생성 후 페이지 반환
	 * @param target ExercisePost들의 target
	 * @param keyword ExercisePost의 title, contents 검색 키워드
	 * @author kimgun-yeong
	 */
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType target,String keyword ,Integer pageNumber, int pageSize);
	
	/**
	 * ExercisePost 생성 및 수정
	 * @param exercisePost 저장하려는 ExercisePost
	 * @return 저장된 ExercisePost
	 * @author kimgun-yeong
	 */
	public ExercisePost save(ExercisePost exercisePost);
	
	/**
	 * User, Exercise와 연관 관계 추가 후 ExercisePost 저장
	 * @author kimgun-yeong
	 */
	public ExercisePost saveWithUserAndExercise(ExercisePost exercisePost, User user, Exercise exericse);
	
	/**
	 * ExercisePost 삭제 
	 * @param exercisePost 삭제하려는 ExercisePost
	 * @author kimgun-yeong
	 */
	public void delete(ExercisePost exercisePost);
	
	/**
	 * ID를 만족하는 ExercisePost 삭제
	 * @param id 삭제하려는 ExercisePost의 ID
	 * @author kimgun-yeong
	 */
	public void deleteById(Long id);
	
	/**
	 * User ID, ExercisePost ID 에 해당하는 ExercisePost 있으면 삭제 
	 * @param userId User의 ID
	 * @param exercisePostId ExercisePost의 ID
	 * @author kimgun-yeong
	 */
	public void checkIsMineAndDelete(Long userId, Long exercisePostId);
	
	/**
	 * 모든 ExercisePost의 개수 반환
	 * @author kimgun-yeong
	 */
	public long count();
	
	/**
	 * 해당 User ID 만족하는 ExercisePost 개수 반환
	 * @param ExercisePost들의 작성자 ID
	 * @author kimgun-yeong
	 */
	public long countWithUserId(Long userId);
	
	/**
	 * Title, Contents 검색 키워드 만족하는 ExercisePost 개수 반환
	 * @param keyword title, contents 검색 키워드
	 * @author kimgun-yeong
	 */
	public long countWithTitleAndContentsKeyword(String keyword);
	
	/**
	 * Exercise의 target 만족하는 ExercisePost 개수 반환
	 * @param target ExercisePost의 Exercise의 target
	 * @author kimgun-yeong
	 */
	public long countWithTarget(TargetType target);
	
	/**
	 * Title, Contents 검색 키워드 와 Exercise의 target 만족하는 ExercisePost 개수 반환
	 * @param target ExercisePost의 Exercise의 target
	 * @param keyword title, contents 검색 키워드
	 * @author kimgun-yeong
	 */
	public long countWithTargetAndKeyword(TargetType target, String keyword);
	
	/**
	 * ExercisePost id로 ExercisePost 가져와서 이를 통해 {@link ExercisePostViewDTO} 생성 및 반환
	 * @param id ExercisePost ID
	 * @return ExercisePostViewDTO, null(해당 id의 ExercisePost 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostId(Long id);
	
	/**
	 * ExercisePost id로 ExercisePost 가져와서 이를 통해 {@link ExercisePostViewDTO} 생성 및 반환 <br>
	 * ExercisePost viewNum(조회수) 증가
	 * @param id ExercisePost ID
	 * @return ExercisePostViewDTO, null(해당 id의 ExercisePost 없을때)
	 * @author kimgun-yeong
	 */
	public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(Long id);
}