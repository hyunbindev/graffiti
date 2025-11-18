package com.hyunbindev.graffiti.service.feed.search;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.feed.PostPreViewDTO;
import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.jpa.feed.FeedBaseRepository;
import com.hyunbindev.graffiti.repository.jpa.group.GroupRepository;
import com.hyunbindev.graffiti.service.feed.FeedService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedSearchService {
	private final FeedBaseRepository feedBaseRepository;
	private final MemberRepository memberRepository;
	private final GroupRepository groupRepository;
	private final FeedService feedService;
	public List<PostPreViewDTO> searchFeedByKeyWord(String userUuid, String groupId, String keyWord, Integer size, Long lastId){
		MemberEntity member = memberRepository.findById(userUuid)
				.orElseThrow(()->new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(()->new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		if(!member.isInGroup(group))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		keyWord+="%";
		
		/**
		List<FeedBaseEntity> feedEntitys = feedBaseRepository.findByGroupIdAndTextPrefix(groupId, keyWord, lastId, size);
		
		return feedEntitys.stream()
				.map((feed)->feedService.mappingPreviewDto(feed, member))
				.toList();
				*/
		return null;
	}
}