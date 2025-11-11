package com.hyunbindev.graffiti.service.feed;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.post.PostPreViewDTO;
import com.hyunbindev.graffiti.data.post.WhisperPreViewDTO;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.FeedBaseRepository;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.service.image.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
	private final FeedBaseRepository feedBaseRepository;
	private final MemberRepository memberRepository;
	private final ImageService imageService;
	/**
	 * 최신순 전체 피드 조회
	 * @param userUuid
	 * @param page
	 * @param offest
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PostPreViewDTO> getRecentPostPreviewWithPage(String userUuid, int page, int size) {
		MemberEntity userEntity = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		
		//사용자 그룹 리스트
		List<String> groupIds = userEntity.getGroupLinks().stream()
				.map((link)->link.getGroup().getId())
				.toList();
		
		List<FeedBaseEntity> postBaseEntitys = feedBaseRepository.findByGroupInAndDeletedFalse(groupIds, size, page*size);
		
		return postBaseEntitys.stream().map((feed)->mappingPreviewDto(feed,userEntity)).toList();
	}
	/**
	 * 게시글 미리보기 DTO 매핑 메서드
	 * @author hyunbinDev
	 * @param entity
	 * @param userEntity
	 * @return extended PostPreViewDTO
	 */
	private PostPreViewDTO mappingPreviewDto(FeedBaseEntity entity, MemberEntity userEntity) {
		//Whisper 게시글일 경우 dto 매핑
		if(entity instanceof WhisperEntity whisper) {
			if(whisper.getImageName() != null) {
				String imageUrl = imageService.getPresignedUrl(whisper.getImageName());
				return WhisperPreViewDTO.mappingDTO(whisper,userEntity,imageUrl);
			}
			return WhisperPreViewDTO.mappingDTO(whisper,userEntity,null);
		}
		return null;
	}
}