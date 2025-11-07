package com.hyunbindev.graffiti.service.whisper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.constant.exception.WhisperExceptionConst;
import com.hyunbindev.graffiti.data.whisper.WhisperCreateCommentDTO;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperCommentEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.jpa.whisper.WhisperCommentRepository;
import com.hyunbindev.graffiti.repository.jpa.whisper.WhisperRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhisperCommentService {
	private final WhisperCommentRepository whisperCommentRepository;
	private final WhisperRepository whisperRepository;
	private final MemberRepository memberRepository;
	
	/**
	 * Whiper feed 덧글 생성
	 * @author hyunbinDev
	 * 
	 * @param userUuid
	 * @param whisperFeedId
	 * @param createDto
	 */
	@Transactional
	public void createWhisperComment(String userUuid, Long whisperFeedId, WhisperCreateCommentDTO createDto) {
		MemberEntity author = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		WhisperEntity whisper = whisperRepository.findById(whisperFeedId)
				.orElseThrow(()-> new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED));
		
		//삭제된 게시글일 경우 예외 처리
		if(whisper.isDeleted())
			throw new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED);
		
		//그룹에 포함되지 않은 사용자가 덧글 작성 시도시 예외 처리
		if(!author.getGroups().contains(whisper.getGroup()))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		//언급 사용자 조회 금지시 예외 처리
		if(whisper.isInvisibleMention() && whisper.getMentionMembers().contains(author))
			throw new CommonAPIException(WhisperExceptionConst.FORBIDDEN);
		
		WhisperCommentEntity comment = WhisperCommentEntity.builder()
				//부모 게시글 설정
				.whisper(whisper)
				//작성자 설정
				.author(author)
				//내용
				.text(createDto.getText())
				.deleted(false)
				.build();
		
		whisperCommentRepository.save(comment);
	}
	/**
	 * 덧글 삭제 처리
	 * @author hyunbinDev
	 * @param userUuid
	 * @param whisperCommentId
	 */
	@Transactional
	public void deleteWhisperComment(String userUuid, Long whisperCommentId) {
		MemberEntity author = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		WhisperCommentEntity comment = whisperCommentRepository.findById(whisperCommentId)
				.orElseThrow(()-> new CommonAPIException(WhisperExceptionConst.NOT_FOUND_COMMENT));
		
		//덧글과 작성자가 일치 하지 않을 경우 예외 처리
		if(!comment.getAuthor().equals(author))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		comment.setDeleted(true);
	}
}