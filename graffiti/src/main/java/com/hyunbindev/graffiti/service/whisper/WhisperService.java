package com.hyunbindev.graffiti.service.whisper;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.constant.exception.WhisperExceptionConst;
import com.hyunbindev.graffiti.data.whisper.WhisperCreateDTO;
import com.hyunbindev.graffiti.data.whisper.WhisperDTO;
import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity.WhisperEntityBuilder;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.jpa.group.GroupRepository;
import com.hyunbindev.graffiti.repository.jpa.whisper.WhisperRepository;
import com.hyunbindev.graffiti.service.feed.FeedCommentService;
import com.hyunbindev.graffiti.service.feed.FeedLikeService;
import com.hyunbindev.graffiti.service.feed.FeedViewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhisperService {
	private final WhisperRepository whisperRepository;
	private final MemberRepository memberRepository;
	private final GroupRepository groupRepository;
	
	private final FeedViewService feedViewService;
	private final FeedLikeService feedLikeService;
	private final FeedCommentService feedCommentService;
	/**
	 * Whisper Post 생성
	 * @param userUuid
	 * @param createDto
	 */
	@Transactional
	public void createWhisperFeed(String userUuid ,WhisperCreateDTO createDto) {
		MemberEntity author = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		GroupEntity group = groupRepository.findById(createDto.getGroupUuid())
				.orElseThrow(()-> new CommonAPIException("그룹을 찾을 수 없습니다.",HttpStatus.NOT_FOUND));
		
		//그룹에 속하지 않을 시에 예외 처리
		if(!author.isInGroup(group))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		WhisperEntityBuilder<?,?> whisperEntityBuilder = WhisperEntity.builder()
				.author(author)
				.group(group)
				.text(createDto.getText())
				.deleted(false);
		
		//사용자 언급 데이터 포함시
		if(!createDto.getMentionMembers().isEmpty()) {
			List<MemberEntity> mentions = memberRepository.findAllById(createDto.getMentionMembers());
			//언급 사용자 존재시
			for(MemberEntity mention : mentions) {
				boolean belongs = mention.getGroupLinks().stream()
						.anyMatch(link-> link.getGroup().getId().equals(group.getId()));
				//언급사용자가 해당 그룹에 참여하고 있지 않을경우 예외 처리
				if(!belongs) throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
			}
			whisperEntityBuilder.mentionMembers(mentions);
		}
		whisperRepository.save(whisperEntityBuilder.build());
	}
	/**
	 * whisper feed 조회
	 * @param userUuid
	 * @param feedId
	 * @return
	 */
	@Transactional(readOnly=true)
	public WhisperDTO getWhisperFeed(String userUuid ,Long feedId) {
		MemberEntity user = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		WhisperEntity whisper = whisperRepository.findByIdWithAuthor(feedId)
				.orElseThrow(()->new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED));
		
		
		//소속 그룹 검증
		if(!user.isInGroup(whisper.getGroup()))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		//언급 검증
		if(whisper.isInvisibleMention() && whisper.getMentionMembers().contains(user))
			throw new CommonAPIException(WhisperExceptionConst.FORBIDDEN);
		
		//사용자 조회 계산
		long viewCount = feedViewService.getViewCountAndSync(feedId, userUuid);
		//좋아요 수 집계
		long likeCount = feedLikeService.getFeedCount(whisper);
		//덧글 수 집계
		long commentCount = feedCommentService.getCommentCount(whisper);
		
		return  WhisperDTO.mappingDTO(whisper,viewCount,likeCount,commentCount);
	}
	/**
	 * Whisper 게시글 소프트 삭제 처리
	 * @author hyunbinDev
	 * @param userUuid
	 * @param feedId
	 */
	@Transactional
	public void deleteWhisperFeed(String userUuid, Long feedId) {
		MemberEntity userEntity = memberRepository.findById(userUuid)
				.orElseThrow(()->new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		WhisperEntity feed = whisperRepository.findByIdWithAuthor(feedId)
				.orElseThrow(()->new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED));
		
		//작성자 검증
		if(!feed.getAuthor().equals(userEntity))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		//삭제 처리
		feed.setDeleted(true);
	}
}
