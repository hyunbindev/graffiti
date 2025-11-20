package com.hyunbindev.graffiti.service.feed;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.feed.PostPreViewDTO;
import com.hyunbindev.graffiti.data.feed.SecretPreViewDTO;
import com.hyunbindev.graffiti.data.feed.WhisperPreViewDTO;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.secret.SecretEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.FeedLikeRepository;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.jpa.feed.FeedBaseRepository;
import com.hyunbindev.graffiti.repository.jpa.group.GroupRepository;
import com.hyunbindev.graffiti.service.image.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
	private final MemberRepository memberRepository;
	private final ImageService imageService;
	private final FeedBaseRepository feedBaseRepository;
	private final FeedLikeRepository feedLikeRepository;
	//private final FeedBaseCustomRepsotory feedBaseCustomRepsotory;
	/**
	 * 최신순 전체 피드 조회
	 * @param userUuid
	 * @param page
	 * @param offest
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PostPreViewDTO> getRecentPostPreviewWithPage(String userUuid, Long lastId, int size) {
		MemberEntity userEntity = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		
		//사용자 그룹 리스트
		List<String> groupIds = userEntity.getGroupLinks().stream()
				.map((link)->link.getGroup().getId())
				.toList();
		
		if(lastId==null)
			lastId = Long.MAX_VALUE;
		
		List<FeedBaseEntity> feedBaseEntitys = feedBaseRepository.findByDeletedIsFalseAndGroupIdInOrderByIdDesc(groupIds, size, lastId);
		
		
		
		return feedBaseEntitys.stream().map((feed)->mappingPreviewDto(feed,userEntity)).toList();
	}
	/**
	 * 게시글 미리보기 DTO 매핑 메서드
	 * @author hyunbinDev
	 * @param entity
	 * @param userEntity
	 * @return extended PostPreViewDTO
	 */
	public PostPreViewDTO mappingPreviewDto(FeedBaseEntity entity, MemberEntity userEntity) {
		//Whisper 게시글일 경우 dto 매핑
		
		boolean isLiked = feedLikeRepository.existsByFeedIdAndLikerId(entity.getId(), userEntity.getId());
		if(entity instanceof WhisperEntity whisper) {
	        // ⭐️ 방어 로직 적용 (Whisper DTO 매핑 메서드 내부로 이동 권장)
	        if(whisper.getImageName() != null) {
	            String imageUrl = imageService.getPresignedUrl(whisper.getImageName());
	            return WhisperPreViewDTO.mappingDTO(whisper,userEntity,imageUrl,isLiked);
	        }
	        return WhisperPreViewDTO.mappingDTO(whisper,userEntity,null,isLiked);
	    }
	    
	    // Secret 게시글일 경우 dto 매핑
	    if(entity instanceof SecretEntity secret) {
	        // ⭐️ 방어 로직 적용 (Secret DTO 매핑 메서드 내부로 이동 권장)
	        return SecretPreViewDTO.mappingDTO(secret,isLiked);
	    }
		return null;
	}
}