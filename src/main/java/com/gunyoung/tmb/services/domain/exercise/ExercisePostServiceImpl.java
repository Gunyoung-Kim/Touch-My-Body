package com.gunyoung.tmb.services.domain.exercise;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * ExercisePostService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("exercisePostService")
@Transactional
public class ExercisePostServiceImpl implements ExercisePostService {

	@Autowired
	ExercisePostRepository exercisePostRepository;
	
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
	 * @param pageNumber
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PageUtil.COMMUNITY_PAGE_SIZE);
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
			Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PageUtil.COMMUNITY_PAGE_SIZE);
		return exercisePostRepository.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, pageRequest);
	}
	
	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(TargetType target,
			Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PageUtil.COMMUNITY_PAGE_SIZE);
		return exercisePostRepository.findAllForPostForCommunityViewDTOWithTargetByPage(target, pageRequest);
	}

	/**
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(TargetType target,
			String keyword, Integer pageNumber) {
		PageRequest pageRuquest = PageRequest.of(pageNumber-1, PageUtil.COMMUNITY_PAGE_SIZE);
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
		exercisePostRepository.delete(exercisePost);
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
		ExercisePost exercisePost = findById(id);
		if(exercisePost == null)
			return null;
		
		ExercisePostViewDTO dto = ExercisePostViewDTO.builder()
				.postId(exercisePost.getId())
				.title(exercisePost.getTitle())
				.exerciseName(exercisePost.getExercise().getName())
				.writerName(exercisePost.getUser().getNickName())
				.contents(exercisePost.getContents())
				.viewNum(exercisePost.getViewNum())
				.likeNum(exercisePost.getPostLikes().size())
				.commentNum(exercisePost.getComments().size())
				.createdAt(localDateToStringForExercisePost(exercisePost.getCreatedAt()))
				.build();
		
		return dto;
	}
	
	private String localDateToStringForExercisePost(LocalDateTime localDateTime) {
		int year = localDateTime.getYear();
		int month = localDateTime.getMonthValue();
		int date = localDateTime.getDayOfMonth();
		int hour = localDateTime.getHour();
		int min = localDateTime.getMinute();
		
		
		//yyyy.MM.dd HH:mm
		return year +"." + month +"." + date + " " +hour +":" + min;
	}
	
}
