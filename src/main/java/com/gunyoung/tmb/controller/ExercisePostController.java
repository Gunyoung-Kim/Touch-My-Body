package com.gunyoung.tmb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.SaveCommentDTO;
import com.gunyoung.tmb.dto.reqeust.SaveExercisePostDTO;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExercisePostNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.HttpRequestUtil;
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

/**
 * ExercisePost 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ExercisePostController {
	
	public static final int COMMUNITY_VIEW_PAGE_SIZE = PageSize.COMMUNITY_PAGE_SIZE.getSize();
	
	private final ExercisePostService exercisePostService;
	
	private final CommentService commentService;
	
	private final UserService userService;
	
	private final ExerciseService exerciseService;
	
	private final HttpSession session;
	
	/**
	 * 커뮤니티 메인 화면 반환하는 메소드
	 * @param keyword ExercisePost 제목 및 내용의 검색 키워드
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/community")
	public ModelAndView exercisePostView(@RequestParam(value = "page", required = false,defaultValue = "1") Integer page,
			@RequestParam(value = "keyword",required = false) String keyword, ModelAndPageView mav) {
		Page<PostForCommunityViewDTO> pageResult = getPageResultForExercisePostView(keyword, page);
		long totalPageNum = getTotalPageNumForExercisePostView(keyword);
		
		TargetType[] allTargetTypes = TargetType.values();
		
		mav.addObject("listObject",pageResult);
		mav.setPageNumbers(page, totalPageNum);
		mav.addObject("targetNames", allTargetTypes);
		mav.addObject("category", "전체");
		
		mav.setViewName("community");
		
		return mav;
	}
	
	private Page<PostForCommunityViewDTO> getPageResultForExercisePostView(String keyword, Integer page) {
		if(keyword == null) {
			return exercisePostService.findAllForPostForCommunityViewDTOOderByCreatedAtDESCByPage(page, COMMUNITY_VIEW_PAGE_SIZE);
		}	
		return exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, page, COMMUNITY_VIEW_PAGE_SIZE);
	}
	
	private long getTotalPageNumForExercisePostView(String keyword) {
		if(keyword == null) {
			return exercisePostService.count()/COMMUNITY_VIEW_PAGE_SIZE + 1;
		}
		return exercisePostService.countWithTitleAndContentsKeyword(keyword)/COMMUNITY_VIEW_PAGE_SIZE + 1;
	}
	
	/**
	 * 특정 부류 게시글만 반환하는 메소드
	 * @param keyword ExercisePost 제목 및 내용의 검색 키워드
	 * @param targetName 게시글 부류
	 * @throws TargetTypeNotFoundedException 요청 카테고리가 존재하지 않을 때
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/community/{target}")
	public ModelAndView exercisePostViewWithTarget(@RequestParam(value = "page", required = false,defaultValue = "1") Integer page
			, @RequestParam(value = "keyword",required = false)String keyword, ModelAndPageView mav, @PathVariable("target") String targetName) {
		TargetType type = getTargetTypeFromName(targetName);
		
		Page<PostForCommunityViewDTO> pageResult = getPageResultForExercisePostViewWithTarget(keyword, type, page);
		long totalPageNum = getTotalPageNumForExercisePostViewWithTarget(keyword, type);
		
		TargetType[] allTargetTypes = TargetType.values();
		
		mav.addObject("listObject",pageResult);
		mav.setPageNumbers(page, totalPageNum);
		mav.addObject("targetNames", allTargetTypes);
		mav.addObject("category", type.getKoreanName());
		
		mav.setViewName("community");
		
		return mav;
	}
	
	private TargetType getTargetTypeFromName(String targetName) {
		try {
			return TargetType.valueOf(targetName);
		} catch(IllegalArgumentException e) {
			throw new TargetTypeNotFoundedException(TargetTypeErrorCode.TARGET_TYPE_NOT_FOUNDED_ERROR.getDescription());
		}
	}
	
	private Page<PostForCommunityViewDTO> getPageResultForExercisePostViewWithTarget(String keyword, TargetType type, Integer page) {
		if(keyword == null) {
			return exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(type, page, COMMUNITY_VIEW_PAGE_SIZE);
		}
		return exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(type, keyword, page, COMMUNITY_VIEW_PAGE_SIZE);
	}
	
	private long getTotalPageNumForExercisePostViewWithTarget(String keyword, TargetType type) {
		if(keyword == null) {
			return exercisePostService.countWithTarget(type)/COMMUNITY_VIEW_PAGE_SIZE + 1;
		}
		return exercisePostService.countWithTargetAndKeyword(type, keyword)/COMMUNITY_VIEW_PAGE_SIZE + 1;
	}
	
	/**
	 * 게시글을 보여주는 뷰 반환하는 메소드
	 * @param postId 찾고자하는 ExercisePost 의 ID
	 * @throws ExercisePostNotFoundedException 해당 ID의 ExercisePost 없으면
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/community/post/{post_id}")
	public ModelAndView exercisePostDetailView(@PathVariable("post_id") Long postId, ModelAndView mav) {
		ExercisePostViewDTO postViewDTO = exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(postId);
		List<CommentForPostViewDTO> commentsInPost = commentService.getCommentForPostViewDTOsByExercisePostId(postId);
		
		mav.addObject("exercisePost",postViewDTO);
		mav.addObject("comments", commentsInPost);
		
		mav.setViewName("postView");
		
		return mav;
	}
	
	/**
	 * 게시글 작성하는 화면 반환하는 메소드
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/community/post/addpost")
	public ModelAndView addExercisePostView(ModelAndView mav) {
		mav.setViewName("addExercisePost");
		
		return mav;
	}
	
	/**
	 * 게시글 추가 처리하는 메소드 <br>
	 * 정상 처리 시 커뮤니티 메인 화면으로 리다이렉트
	 * @throws UserNotFoundedException 세션에 저장된 ID에 해당하는 User 없으면
	 * @throws ExerciseNotFoundedException dto의 exerciseName에 해당하는 Exercise 없으면 
	 * @author kimgun-yeong
	 */
	@PostMapping(value = "/community/post/addpost")
	@LoginIdSessionNotNull
	public ModelAndView addExercisePost(@ModelAttribute SaveExercisePostDTO dto, ModelAndView mav) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		User user = userService.findWithExercisePostsById(loginUserId);
		Exercise exercise = exerciseService.findWithExercisePostsByName(dto.getExerciseName());
		ExercisePost exercisePost = dto.createExercisePost();
		
		exercisePostService.saveWithUserAndExercise(exercisePost, user, exercise);
		
		return new ModelAndView("redirect:/community");
	}
	
	/**
	 * 유저가 게시글에 댓글 추가할때 처리하는 메소드 <br>
	 * 정상 처리시 해당 게시글로 리다이렉트
	 * @param postId 댓글을 추가하려는 ExercisePost
	 * @throws UserNotFoundedException 세션에 저장된 ID에 해당하는 User 없으면
	 * @throws ExercisePostNotFoundedException 해당 ID의 ExercisePost 없으면 
	 * @author kimgun-yeong
	 */
	@PostMapping(value = "/community/post/{postId}/addComment")
	@LoginIdSessionNotNull
	public ModelAndView addCommentToExercisePost(@PathVariable("postId") Long postId, @ModelAttribute SaveCommentDTO dto,
			@RequestParam("isAnonymous") boolean isAnonymous, HttpServletRequest request) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		User user = userService.findWithCommentsById(loginUserId);
		ExercisePost exercisePost = exercisePostService.findWithCommentsById(postId);
 
		String writerIp = HttpRequestUtil.getRemoteHost(request);
		Comment newComment = SaveCommentDTO.toComment(dto, writerIp);
		newComment.setAnonymous(isAnonymous);
		
		commentService.saveWithUserAndExercisePost(newComment, user, exercisePost);
		
		return new ModelAndView("redirect:/community/post/" + postId);		
	}
	
}
