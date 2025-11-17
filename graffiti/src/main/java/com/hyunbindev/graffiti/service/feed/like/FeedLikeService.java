package com.hyunbindev.graffiti.service.feed.like;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.constant.exception.WhisperExceptionConst;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedLikeEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.FeedBaseRepository;
import com.hyunbindev.graffiti.repository.jpa.FeedLikeRepository;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedLikeService {
	private final FeedLikeRepository feedLikeRepository;
	private final FeedBaseRepository feedBaseRepository;
	private final MemberRepository memberRepository;
	private final FeedLikeCountService feedLikeCountService;
	/**
	 * feed like
	 * @param feed
	 * @param user
	 * @return 이미 좋아하는 글일 경우 false, 아닐 경우 true
	 */
	@Transactional
	public boolean likeFeed(String userUuid, Long feedId) {
		MemberEntity user = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		FeedBaseEntity feed = feedBaseRepository.findById(feedId)
				.orElseThrow(()-> new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED));
		
		//삭제된 게시글일 경우 예외 처리
		if(feed.isDeleted())
			throw new CommonAPIException(WhisperExceptionConst.NOT_FOUND_FEED);
		
		//그룹에 포함되지 않은 사용자가 좋아요 시도시 예외 처리
		if(!user.isInGroup(feed.getGroup()))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		//언급 사용자 조회 금지시 예외 처리
		if(feed instanceof WhisperEntity whisper) {
			if(whisper.isInvisibleMention() && whisper.getMentionMembers().contains(user))
				throw new CommonAPIException(WhisperExceptionConst.FORBIDDEN);	
		}
		
		Optional<FeedLikeEntity> feedLike = feedLikeRepository.findByFeedAndLiker(feed, user);
		if(feedLike.isEmpty()) {
			feedLikeRepository.save(new FeedLikeEntity(feed,user));
			feedLikeCountService.incrementFeedLikeCount(feed);
			return true;
		}
		feedLikeRepository.delete(feedLike.get());
		feedLikeCountService.decrementFeedLikeCount(feed);
		return false;
	}
}