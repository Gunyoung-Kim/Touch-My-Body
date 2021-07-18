package com.gunyoung.tmb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull;
import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.AddCommentDTO;
import com.gunyoung.tmb.dto.reqeust.AddExercisePostDTO;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.ExercisePostErrorCode;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExercisePostNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.HttpRequestUtil;
import com.gunyoung.tmb.utils.PageUtil;
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
	
	private final ExercisePostService exercisePostService;
	
	private final CommentService commentService;
	
	private final UserService userService;
	
	private final ExerciseService exerciseService;
	
	private final HttpSession session;
	
	/**
	 * 커뮤니티 메인 화면 반환하는 메소드
	 * @param mav
	 * @param keyword ExercisePost 제목 및 내용의 검색 키워드
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community",method=RequestMethod.GET)
	public ModelAndView exercisePostView(@RequestParam(value="page", required = false,defaultValue="1") int page,
			@RequestParam(value="keyword",required=false)String keyword,ModelAndView mav) {
		mav.setViewName("community");
		int page_size = PageUtil.COMMUNITY_PAGE_SIZE;
		
		Page<PostForCommunityViewDTO> pageResult;
		long totalPageNum;
		
		if(keyword == null) {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOByPage(page,PageUtil.COMMUNITY_PAGE_SIZE);
			totalPageNum = exercisePostService.count()/page_size +1;
		} else {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, page,PageUtil.COMMUNITY_PAGE_SIZE);
			totalPageNum = exercisePostService.countWithTitleAndContentsKeyword(keyword)/page_size +1;
		}
		
		List<PostForCommunityViewDTO> resultList = pageResult.getContent();
		
		TargetType[] targetTypes = TargetType.values();
		
		mav.addObject("listObject",resultList);
		mav.addObject("category", "전체");
		mav.addObject("currentPage",page);
		mav.addObject("targetNames", targetTypes);
		mav.addObject("startIndex",(page/page_size)*page_size+1);
		mav.addObject("lastIndex",(page/page_size)*page_size+page_size-1 > totalPageNum ? totalPageNum : (page/page_size)*page_size+page_size-1);
		
		return mav;
	}
	
	/**
	 * 특정 부류 게시글만 반환하는 메소
	 * @param page
	 * @param keyword
	 * @param mav
	 * @param targetName
	 * @return
	 */
	@RequestMapping(value="/community/{target}",method = RequestMethod.GET)
	public ModelAndView exercisePostViewWithTarget(@RequestParam(value="page", required = false,defaultValue="1") int page
			,@RequestParam(value="keyword",required=false)String keyword,ModelAndView mav,@PathVariable("target") String targetName) {
		TargetType type;
		try {
			type = TargetType.valueOf(targetName);
		} catch(IllegalArgumentException e) {
			throw new TargetTypeNotFoundedException(TargetTypeErrorCode.TARGET_TYPE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		mav.setViewName("community");
		int page_size = PageUtil.COMMUNITY_PAGE_SIZE;
		
		Page<PostForCommunityViewDTO> pageResult;
		long totalPageNum;
		
		if(keyword == null) {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(type, page,PageUtil.COMMUNITY_PAGE_SIZE);
			totalPageNum = exercisePostService.countWithTarget(type)/page_size +1;
		} else {
			pageResult = exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(type, keyword, page,PageUtil.COMMUNITY_PAGE_SIZE);
			totalPageNum = exercisePostService.countWithTargetAndKeyword(type, keyword)/page_size +1;
		}
		
		List<PostForCommunityViewDTO> resultList = pageResult.getContent();
		
		TargetType[] targetTypes = TargetType.values();
		
		mav.addObject("listObject",resultList);
		mav.addObject("targetNames", targetTypes);
		mav.addObject("category", type.getKoreanName());
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/page_size)*page_size+1);
		mav.addObject("lastIndex",(page/page_size)*page_size+page_size-1 > totalPageNum ? totalPageNum : (page/page_size)*page_size+page_size-1);
		
		return mav;

	}
	
	/**
	 * 게시글을 보여주는 뷰 반환하는 메소드
	 * @param postId 찾고자하는 ExercisePost 의 ID
	 * @param mav
	 * @throws ExercisePostNotFoundedException 해당 ID의 ExercisePost 없으면
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}" ,method = RequestMethod.GET)
	public ModelAndView exercisePostDetailView(@PathVariable("post_id") Long postId,ModelAndView mav) {
		ExercisePostViewDTO postDTO = exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreasViewNum(postId);
		
		if(postDTO == null) {
			throw new ExercisePostNotFoundedException(ExercisePostErrorCode.EXERCISE_POST_NOT_FOUNDED_ERROR.getDescription());
		}
		
		List<CommentForPostViewDTO> comments = commentService.getCommentForPostViewDTOsByExercisePostId(postId);
		
		mav.setViewName("postView");
		mav.addObject("exercisePost",postDTO);
		mav.addObject("comments", comments);
		
		return mav;
	}
	
	/**
	 * 게시글 작성하는 화면 반환하는 메소드
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/addpost", method = RequestMethod.GET)
	public ModelAndView addExercisePostView(ModelAndView mav) {
		mav.setViewName("addExercisePost");
		
		return mav;
	}
	
	/**
	 * 게시글 추가 처리하는 메소드 <br>
	 * 정상 처리 시 커뮤니티 메인 화면으로 리다이렉트
	 * @param dto
	 * @param mav
	 * @throws UserNotFoundedException 세션에 저장된 ID에 해당하는 User 없으면
	 * @throws ExerciseNotFoundedException dto의 exerciseName에 해당하는 Exercise 없으면 
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/addpost", method = RequestMethod.POST)
	@LoginIdSessionNotNull
	public ModelAndView addExercisePost(@ModelAttribute AddExercisePostDTO dto, ModelAndView mav) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findfWithExercisePostsById(userId);
		
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Exercise exercise = exerciseService.findWithExercisePostsByName(dto.getExerciseName());
		
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_NAME_NOT_FOUNDED_ERROR.getDescription());
		}
		
		ExercisePost exercisePost = ExercisePost.builder()
				.title(dto.getTitle())
				.contents(dto.getContents())
				.build();
		
		exercisePostService.saveWithUserAndExercise(exercisePost, user, exercise);
		
		return new ModelAndView("redirect:/community");
	}
	
	/**
	 * 유저가 게시글에 댓글 추가할때 처리하는 메소드 <br>
	 * 정상 처리시 해당 게시글로 리다이렉트
	 * @param postId 댓글을 추가하려는 ExercisePost
	 * @param dto
	 * @param request
	 * @throws UserNotFoundedException 세션에 저장된 ID에 해당하는 User 없으면
	 * @throws ExercisePostNotFoundedException 해당 ID의 ExercisePost 없으면 
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/community/post/{post_id}/addComment",method = RequestMethod.POST)
	@LoginIdSessionNotNull
	public ModelAndView addCommentToExercisePost(@PathVariable("post_id") Long postId,@ModelAttribute AddCommentDTO dto,
			@RequestParam("isAnonymous") boolean isAnonymous,HttpServletRequest request) {
		
		dto.setAnonymous(isAnonymous);
		
		// 세션으로 가져온 유저 id로 유저 객체 찾기
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findWithCommentsById(userId);
		
		if(user == null)
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		
		// post_id 로 해당 ExercisePost 가져오기 
		ExercisePost exercisePost = exercisePostService.findWithCommentsById(postId);
		
		if(exercisePost == null) 
			throw new ExercisePostNotFoundedException(ExercisePostErrorCode.EXERCISE_POST_NOT_FOUNDED_ERROR.getDescription());
		
		// request IP 가져오기 
		String writerIp = HttpRequestUtil.getRemoteHost(request);
		
		Comment comment = AddCommentDTO.toComment(dto, writerIp);
		
		commentService.saveWithUserAndExercisePost(comment, user, exercisePost);
		
		return new ModelAndView("redirect:/community/post/" + postId);
				
	}
	
}