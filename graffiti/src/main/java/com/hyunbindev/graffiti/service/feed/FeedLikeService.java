package com.hyunbindev.graffiti.service.feed;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedLikeEntity;
import com.hyunbindev.graffiti.repository.jpa.FeedLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedLikeService {
	private final FeedLikeRepository feedLikeRepository;
	/**
	 * feed like
	 * @param feed
	 * @param user
	 * @return 이미 좋아하는 글일 경우 false, 아닐 경우 true
	 */
	public boolean likeFeed(FeedBaseEntity feed, MemberEntity user) {
		Optional<FeedLikeEntity> feedLike = feedLikeRepository.findByFeedAndLiker(feed, user);
		if(feedLike.isEmpty()) {
			feedLikeRepository.save(new FeedLikeEntity(feed,user));
			return true;
		}
		feedLikeRepository.delete(feedLike.get());
		return false;
	}
	/**
	 * feed like count
	 * @param feed
	 * @return
	 */
	public long getFeedCount(FeedBaseEntity feed) {
		return feedLikeRepository.countByFeed(feed);
	}
}