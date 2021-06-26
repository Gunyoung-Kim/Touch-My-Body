package com.gunyoung.tmb.services.domain.exercise;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
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
	
}
