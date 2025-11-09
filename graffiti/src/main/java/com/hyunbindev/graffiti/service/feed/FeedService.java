package com.hyunbindev.graffiti.service.feed;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.post.PostPreViewDTO;
import com.hyunbindev.graffiti.data.post.WhisperPreViewDTO;
import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.FeedBaseRepository;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {
	private final FeedBaseRepository feedBaseRepository;
	private final MemberRepository memberRepository;
	
	/**
	 * 최신순 전체 피드 조회
	 * @param userUuid
	 * @param page
	 * @param offest
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PostPreViewDTO> getRecentPostPreviewWithPage(String userUuid, int page, int offest) {
		MemberEntity userEntity = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		Pageable pageable = PageRequest.of(page, offest, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		//사용자 그룹 리스트
		List<GroupEntity> groups = userEntity.getGroupLinks().stream()
				.map((link)->link.getGroup())
				.toList();
		
		List<FeedBaseEntity> postBaseEntitys = feedBaseRepository.findByGroupInAndDeletedFalse(groups, pageable);
		
		return postBaseEntitys.stream().map((post)->mappingPreviewDto(post,userEntity)).toList();
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
			return WhisperPreViewDTO.mappingDTO(whisper);
		}
		return null;
	}
}