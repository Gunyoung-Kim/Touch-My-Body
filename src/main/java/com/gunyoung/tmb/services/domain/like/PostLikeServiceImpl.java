package com.gunyoung.tmb.services.domain.like;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.repos.PostLikeRepository;

/**
 * PostLikeService 구현하는 클래스
 * @author kimgun-yeong
 *
 */
@Service("postLikeService")
@Transactional
public class PostLikeServiceImpl implements PostLikeService {
	
	@Autowired
	PostLikeRepository postLikeRepository;

	/**
	 * @param id 찾으려는 PostLike의 id
	 * @return PostLike, Null( 해당 id의 PostLike 없으면)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public PostLike findById(Long id) {
		Optional<PostLike> result = postLikeRepository.findById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}

	/**
	 * @param postLike 저장하려는 PostLike
	 * @return 저장된 postLike
	 * @author kimgun-yeong
	 */
	@Override
	public PostLike save(PostLike postLike) {
		return postLikeRepository.save(postLike);
	}

	/**
	 * @param postLike 삭제하려는 PostLike
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(PostLike postLike) {
		postLikeRepository.delete(postLike);
	}
	
	
}
