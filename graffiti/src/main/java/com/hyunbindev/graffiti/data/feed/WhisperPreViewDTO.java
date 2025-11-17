package com.hyunbindev.graffiti.data.feed;

import java.util.List;

import com.hyunbindev.graffiti.constant.feed.FeedType;
import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Getter
@Slf4j
public class WhisperPreViewDTO extends PostPreViewDTO{
	List<MemberInfoDTO> mentionMember;
	String imageUrl;
	/**
	 * DTO mapper
	 * @param entity
	 * @return
	 */
	public static WhisperPreViewDTO mappingDTO(WhisperEntity entity, MemberEntity userEntity ,String imageUrl) {
		//PostPreViewDTO 상속 WhisperPreViewDTO builder 생성
		WhisperPreViewDTOBuilder<?,?> whisperPreViewDTOBuilder=WhisperPreViewDTO.builder()
				//공통 필드
				.id(entity.getId())
				//피드 타입 명시
				.type(FeedType.WHISPER)
				//생성 날자
				.createdAt(entity.getCreatedAt())
				.commentCount(entity.getCommentCount())
				.likeCount(entity.getLikeCount());
		//언급 대상 비공개시
		if(entity.isInvisibleMention() && entity.getMentionMembers().contains(userEntity)) {
			whisperPreViewDTOBuilder.isBlinded(true)
			.previewText("비공개 게시글 입니다.");
		}else {
			//미리보기
			String preViewText = entity.getText().substring(0, Math.min(entity.getText().length(), 50));
			//작성자
			MemberInfoDTO authorDto = new MemberInfoDTO(entity.getAuthor());
			whisperPreViewDTOBuilder.authorInfo(authorDto)
			//언급 대상자
			.mentionMember(entity.getMentionMembers().stream().map((m)-> new MemberInfoDTO(m)).toList())
			.isBlinded(false)
			//글 미리보기 50글자 제한
			//.previewText(entity.getText().substring(Math.min(entity.getText().length(), 50)));
			.previewText(preViewText)
			.imageUrl(imageUrl);
		}
		return whisperPreViewDTOBuilder.build();
	}
}
