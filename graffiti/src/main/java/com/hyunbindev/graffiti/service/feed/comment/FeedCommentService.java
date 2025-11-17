package com.hyunbindev.graffiti.service.feed.comment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.constant.exception.WhisperExceptionConst;
import com.hyunbindev.graffiti.data.feed.FeedCommentDTO;
import com.hyunbindev.graffiti.data.whisper.CreateCommentDTO;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedCommentEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.FeedBaseRepository;
import com.hyunbindev.graffiti.repository.jpa.FeedCommentRepository;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedCommentService {
	private final FeedCommentRepository feedCommentRepository;
	private final FeedBaseRepository feedBaseRepository;
	private final MemberRepository memberRepository;
	private final FeedCommentCountService feedCommentCountService;
	/**
	 * Whiper feed 덧글 생성
	 * @author hyunbinDev
	 * 
	 * @param userUuid
	 * @param whisperFeedId
	 * @param createDto
	 */
	@Transactional
	public void createWhisperComment(String userUuid, Long whisperFeedId, CreateCommentDTO createDto) {
		MemberEntity author = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		FeedBaseEntity feed = feedBaseRepository.findById(whisperFeedId)
				.orElseThrow(()-> new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED));
		
		//삭제된 게시글일 경우 예외 처리
		if(feed.isDeleted())
			throw new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED);
		
		//그룹에 포함되지 않은 사용자가 덧글 작성 시도시 예외 처리
		if(!author.isInGroup(feed.getGroup()))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		//언급 사용자 조회 금지시 예외 처리
		if(feed instanceof WhisperEntity whisper) {
			if(whisper.isInvisibleMention() && whisper.getMentionMembers().contains(author))
				throw new CommonAPIException(WhisperExceptionConst.FORBIDDEN);	
		}
		
		FeedCommentEntity comment = FeedCommentEntity.builder()
				//부모 게시글 설정
				.feed(feed)
				//작성자 설정
				.author(author)
				//내용
				.text(createDto.getText())
				.deleted(false)
				.build();
		//덧글 수 증가
		feedCommentCountService.incrementFeedCommentCount(feed);
		//덧글 저장
		feedCommentRepository.save(comment);
	}
	/**
	 * whisper feed comment 조회
	 * @param userUuid
	 * @param whisperId
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<FeedCommentDTO> getWhisperComments(String userUuid, Long whisperId) {
		MemberEntity user = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		FeedBaseEntity feed = feedBaseRepository.findById(whisperId)
				.orElseThrow(()->new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED));
		
		//그룹 가입 안되어 있을시
		if(!user.isInGroup(feed.getGroup()))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		//언급 사용자 조회 금지시 예외 처리
		if(feed instanceof WhisperEntity whisper) {
			if(whisper.isInvisibleMention() && whisper.getMentionMembers().contains(user))
				throw new CommonAPIException(WhisperExceptionConst.FORBIDDEN);	
		}
		//Dto mapping 후 리턴
		return feedCommentRepository.findCommentsByFeedWithAuthor(feed).stream()
				.map((entity)->FeedCommentDTO.mappingDTO(entity))
				.toList();
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
		
		FeedCommentEntity comment = feedCommentRepository.findById(whisperCommentId)
				.orElseThrow(()-> new CommonAPIException(WhisperExceptionConst.NOT_FOUND_COMMENT));
		
		//덧글과 작성자가 일치 하지 않을 경우 예외 처리
		if(!comment.getAuthor().equals(author))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		feedCommentCountService.decrementFeedCommentCount(comment.getFeed());
		
		comment.setDeleted(true);
	}
}