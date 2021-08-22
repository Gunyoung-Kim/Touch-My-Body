package com.gunyoung.tmb.dto.reqeust.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.dto.reqeust.SaveCommentDTO;

/**
 * {@link SaveCommentDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SaveCommentDTO only
 * @author kimgun-yeong
 *
 */
public class SaveCommentDTOUnitTest {
	
	/*
	 * public static Comment toComment(SaveCommentDTO dto,String writerIp) 
	 */
	
	@Test
	@DisplayName("SaveCommentDTO 필드 값과 writerIp를 통해 Comment 생성 후 반환 -> 정상, WriterIp 확인")
	public void toCommentTestCheckWriterIp() {
		//Given
		SaveCommentDTO saveCommentDTO = SaveCommentDTO.builder()
				.contents("contents")
				.isAnonymous(false)
				.build();
		
		String writerIp = "127.0.0.1";
		
		//When
		Comment result = SaveCommentDTO.toComment(saveCommentDTO, writerIp);
		
		//Then
		assertEquals(writerIp, result.getWriterIp());
	}
	
	@Test
	@DisplayName("SaveCommentDTO 필드 값과 writerIp를 통해 Comment 생성 후 반환 -> 정상, Contents 확인")
	public void toCommentTestCheckContents() {
		//Given
		String contents = "contents!!";
		SaveCommentDTO saveCommentDTO = SaveCommentDTO.builder()
				.contents(contents)
				.isAnonymous(false)
				.build();
		
		String writerIp = "127.0.0.1";
		
		//When
		Comment result = SaveCommentDTO.toComment(saveCommentDTO, writerIp);
		
		//Then
		assertEquals(contents, result.getContents());
	}
	
	@Test
	@DisplayName("SaveCommentDTO 필드 값과 writerIp를 통해 Comment 생성 후 반환 -> 정상, IsAnonymous 확인")
	public void toCommentTestCheckIsAnonymous() {
		//Given
		boolean isAnonymous = true;
		SaveCommentDTO saveCommentDTO = SaveCommentDTO.builder()
				.contents("contents")
				.isAnonymous(isAnonymous)
				.build();
		
		String writerIp = "127.0.0.1";
		
		//When
		Comment result = SaveCommentDTO.toComment(saveCommentDTO, writerIp);
		
		//Then
		assertEquals(isAnonymous, result.isAnonymous());
	}
}
