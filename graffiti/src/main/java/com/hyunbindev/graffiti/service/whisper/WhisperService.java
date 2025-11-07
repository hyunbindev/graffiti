package com.hyunbindev.graffiti.service.whisper;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.constant.exception.WhisperExceptionConst;
import com.hyunbindev.graffiti.data.whisper.WhisperCreateDTO;
import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity.WhisperEntityBuilder;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.GroupRepository;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.jpa.whisper.WhisperRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhisperService {
	private final WhisperRepository whisperRepository;
	private final MemberRepository memberRepository;
	private final GroupRepository groupRepository;
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
		if(!author.getGroups().contains(group))
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
				boolean belongs = mention.getGroups().stream()
						.anyMatch(g-> g.getId().equals(group.getId()));
				//언급사용자가 해당 그룹에 참여하고 있지 않을경우 예외 처리
				if(!belongs) throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
			}
			whisperEntityBuilder.mentionMembers(mentions);
		}
		whisperRepository.save(whisperEntityBuilder.build());
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
		
		WhisperEntity feed = whisperRepository.findById(feedId)
				.orElseThrow(()->new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED));
		
		//작성자 검증
		if(!feed.getAuthor().equals(userEntity))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		//삭제 처리
		feed.setDeleted(true);
	}
}
