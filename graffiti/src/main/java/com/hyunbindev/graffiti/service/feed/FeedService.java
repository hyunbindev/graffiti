package com.hyunbindev.graffiti.service.feed;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.constant.feed.FeedType;
import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.data.post.PostPreViewDTO;
import com.hyunbindev.graffiti.data.post.VotePreViewDTO;
import com.hyunbindev.graffiti.data.post.WhisperPreViewDTO;
import com.hyunbindev.graffiti.data.post.WhisperPreViewDTO.WhisperPreViewDTOBuilder;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.PostBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.jpa.PostBaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
	private final PostBaseRepository postBaseRepository;
	private final MemberRepository memberRepository;
	
	
	@Transactional(readOnly=true)
	public List<PostPreViewDTO> getRecentPostPreviewWithPage(String userUuid, int page, int offest) {
		MemberEntity userEntity = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		Pageable pageable = PageRequest.of(page, offest, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		List<PostBaseEntity> postBaseEntitys = postBaseRepository.findByGroupInAndDeletedFalse(userEntity.getGroups(), pageable);
		
		return postBaseEntitys.stream().map((post)->mappingPreviewDto(post,userEntity)).toList();
	}
	/**
	 * 게시글 미리보기 전처리 메서드
	 * @author hyunbinDev
	 * @param entity
	 * @param userEntity
	 * @return extended PostPreViewDTO
	 */
	private PostPreViewDTO mappingPreviewDto(PostBaseEntity entity, MemberEntity userEntity) {
		//Whisper 게시글일 경우 전처리
		if(entity instanceof WhisperEntity whisper) {
			WhisperPreViewDTOBuilder<?,?> whisperPreViewDTOBuilder =WhisperPreViewDTO.builder()
					.id(entity.getId())
					//공통 필드
					//피드 타입 명시
					.type(FeedType.WHISPER)
					//생성 날자
					.createdAt(entity.getCreatedAt());
			
			//언급 대상 비공개시
			if(whisper.isInvisibleMention() && whisper.getMentionMembers().contains(userEntity)) {
				whisperPreViewDTOBuilder.isBlinded(true)
				.previewText("비공개 게시글 입니다.");
			}else {
				//작성자
				MemberInfoDTO authorDto = new MemberInfoDTO(whisper.getAuthor());
				whisperPreViewDTOBuilder.authorInfo(authorDto)
				//언급 대상자
				.mentionMember(whisper.getMentionMembers().stream().map((m)-> new MemberInfoDTO(m)).toList())
				.isBlinded(false)
				//글 미리보기 50글자 제한
				.previewText(whisper.getText().substring(Math.min(whisper.getText().length(), 50)));
			}
			return whisperPreViewDTOBuilder.build();
		}
		
		return null;
	}
}