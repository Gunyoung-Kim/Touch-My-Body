package com.gunyoung.tmb.services.domain.like;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.repos.CommentLikeRepository;

/**
 * CommentLikeService 구현하는 클래스
 * @author kimgun-yeong
 *
 */
@Service("commentLikeService")
@Transactional
public class CommentLikeServiceImpl implements CommentLikeService {

	@Autowired
	CommentLikeRepository commentLikeRepository;
	
	/**
	 * @param id 찾으려는 commentLike의 id
	 * @return CommentLike, Null(해당 id의 commentLike 없을때)
	 * @author kimgun-yeong
	 */
	@Override
	@Transactional(readOnly=true)
	public CommentLike findById(Long id) {
		Optional<CommentLike> result = commentLikeRepository.findById(id);
		if(result.isEmpty()) 
			return null;
		return result.get();
	}

	/**
	 * @param commentLike save할 CommentLike 객체
	 * @return save 된 CommentLike 객체
	 * @author kimgun-yeong
	 */
	@Override
	public CommentLike save(CommentLike commentLike) {
		return commentLikeRepository.save(commentLike);
	}

	/**
	 * @param commentLike delete할 commentLike 객체
	 * @return delete 된 CommentLike 객체
	 * @author kimgun-yeong
	 */
	@Override
	public void delete(CommentLike commentLike) {
		commentLikeRepository.delete(commentLike);
	}
	
}
