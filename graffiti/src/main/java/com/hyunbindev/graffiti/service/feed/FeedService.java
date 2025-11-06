package com.hyunbindev.graffiti.service.feed;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.data.post.PostPreViewDTO;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.PostBaseEntity;
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
	
	public List<PostPreViewDTO> getRecentPostPreviewWithPage(String userUuid, int page, int offest) {
		MemberEntity member = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		Pageable pageable = PageRequest.of(page, offest, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		List<PostBaseEntity> postBaseEntitys = postBaseRepository.findByGroupIn(member.getGroups(), pageable);
		
		return postBaseEntitys.stream().map(this::mappingPreviewDto).toList();
	}
	
	private PostPreViewDTO mappingPreviewDto(PostBaseEntity entity) {
		MemberInfoDTO authorDto = new MemberInfoDTO(entity.getAuthor());
		return PostPreViewDTO.builder()
				.authorInfo(authorDto)
				.createdAt(entity.getCreatedAt())
				.build();
	}
}